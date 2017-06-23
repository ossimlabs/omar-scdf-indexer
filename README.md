# omar-scdf-indexer
The Indexer is a Spring Cloud Data Flow (SCDF) Processor.
This means it:
1. Receives a message on a Spring Cloud input stream using Kafka.
2. Performs an operation on the data.
3. Sends the result on a Spring Cloud output stream using Kafka to a listening SCDF Processor or SCDF Sink.

## Purpose
The Indexer receives a JSON message from the Stager containing the filename and path of a staged image, as well as the result of the stage. If the histogram and overview were successfully created, the Indexer then attempts to index the image. Finally, the Stager passes the message along to a Sink, uses the final result to report the pipeline success.

## JSON Input Example (from the Stager)
```json
{
   "filename":"/data/2017/06/22/09/933657b1-6752-42dc-98d8-73ef95a5e780/12345/SCDFTestImages/tiff/14SEP12113301-M1BS-053951940020_01_P001.TIF",
   "stagedSuccessfully":true
}
```

## JSON Output Example (to the Sink)
```json
{
   "filename":"/data/2017/06/22/09/933657b1-6752-42dc-98d8-73ef95a5e780/12345/SCDFTestImages/tiff/14SEP12113301-M1BS-053951940020_01_P001.TIF",
   "indexedSuccessfully":true
}
```
