# omar-scdf-indexer

## Description
The OMAR SCDF indexer application is a Spring Cloud Data Flow processor service involved in ingesting imagery. It receives an image's message data, adds the metadata to the database, and forwards the message down the stream.

## Purpose
The Indexer receives a JSON message from the Image Info app containing the filename and path of an image which has been staged, as well as the metadata parsed from the Image Info app. The Indexer then attempts to add the metadata to the database. If successful, it sends a JSON message to the Sink indicating success.

## JSON Input Example (from the Image Info app)
```json
{
   "filename":"/data/2017/06/22/09/933657b1-6752-42dc-98d8-73ef95a5e780/12345/SCDFTestImages/tiff/14SEP12113301-M1BS-053951940020_01_P001.TIF",
   "metadataCreated":true
}
```

## JSON Output Example (to the Sink)
```json
{
   "filename":"/data/2017/06/22/09/933657b1-6752-42dc-98d8-73ef95a5e780/12345/SCDFTestImages/tiff/14SEP12113301-M1BS-053951940020_01_P001.TIF",
   "indexedSuccessfully":true
}
```
