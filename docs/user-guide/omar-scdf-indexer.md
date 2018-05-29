# omar-scdf-indexer
The Indexer is a Spring Cloud Data Flow (SCDF) Processor.
This means it:
1. Receives a message on a Spring Cloud input stream using RabbitMQ.
2. Performs an operation on the data.
3. Sends the result on a Spring Cloud output stream using RabbitMQ to a listening SCDF Processor or SCDF Sink.

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
