package io.ossim.omar.scdf.stager

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Processor
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.SendTo

/**
 * Created by slallier on 6/8/2017
 *
 * The OmarScdfStagerApplication is a purpose built indexing app for integration with a full SCDF stack.
 */
@SpringBootApplication
@EnableBinding(Processor.class)
@Slf4j
class OmarScdfIndexerApplication
{
    // Stager settings
    // Turn build histograms and overviews off so it only indexes the image

    @Value('${stager.add.raster.url:https://omar-dev.ossim.io:443/omar-stager/dataManager/addRaster?}')
    private String addRasterUrl

    @Value('${stager.build.histograms:false}')
    private boolean buildHistograms

    @Value('${stager.build.overviews:false}')
    private boolean buildOverviews

    @Value('${stager.background:false}')
    private boolean background

    /**
     * The main entry point of the SCDF Sqs application.
     * @param args
     */
    static final void main(String[] args)
    {
        SpringApplication.run OmarScdfIndexerApplication, args
    }

    /**
     * The method that handles the index request when a filename is received
     * @param message the message containing the image filename
     */
    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    final String handleStageRequest(final Message<?> message)
    {
        log.debug("Received message ${message} containing the name of a file to index")

        if (null != message.payload)
        {
            // Json to return
            JsonBuilder indexedFile = new JsonBuilder()
            boolean indexedSuccessfully = false

            // Parse filename from message
            final def parsedJson = new JsonSlurper().parseText(message.payload)
            final String filename = parsedJson.filename

            // If the stager successfully created histogram/overview, attempt to index
            if (parsedJson.stagedSuccessfully == true)
            {

                // index image by calling the existing stager on omar-dev
                log.debug("Indexing image ${filename}")

                HashMap params = [
                        filename                     : filename,
                        buildHistograms              : buildHistograms,
                        buildOverviews               : buildOverviews,
                        background                   : background
                ]

                log.debug("Indexing params:\n ${params}")

                indexedSuccessfully = indexImage(params)
            }

            // Return filename and result of indexing request
            indexedFile(
                    filename : filename,
                    indexedSuccessfully : indexedSuccessfully
            )

            log.debug("Sending result to output stream -- ${indexedFile.toString()}")
            return indexedFile.toString()
        }
        else
        {
            log.warn("Received null payload for message: ${message}")
            return null
        }
    }

    /**
    * Method to index image using the params Map
    * @return boolean stating whether the image was indexed successfully or not
    */
    final private boolean indexImage(HashMap params)
    {
        // Index using curl request
        String addRasterFinalURL = "${addRasterUrl}" +
                "filename=${params.filename}" +
                "&buildHistograms=${params.buildHistograms}" +
                "&buildOverviews=${params.buildOverviews}" +
                "&background=${params.background}"

        // Do URL request
        log.debug("Sending stager request: ${addRasterFinalURL}")

        URL url = new URL(addRasterFinalURL)
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection()
        httpURLConnection.setRequestMethod("POST")
        httpURLConnection.connect()

        String response = httpURLConnection.getResponseMessage()
        int responseCode = httpURLConnection.getResponseCode()
        log.debug("Response from stager: ${response}")
        log.debug("Response code from stager: ${responseCode}")

        return responseCode == HttpURLConnection.HTTP_OK
    }
}
