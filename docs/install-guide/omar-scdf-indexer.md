# omar-scdf-indexer

## Pre-Req

Spring Cloud Dataflow Server for Openshift, MariaDB/MySQL/Postgres and RabbitMQ is deployed into a Openshift Environment.

## Sample application.properties file
```
spring.cloud.stream.bindings.input.destination=image-info
spring.cloud.stream.bindings.input.consumer.concurrency=1
spring.cloud.stream.RabbitMQ.binder.autoAddPartitions=true

spring.cloud.stream.bindings.output.destination=image-indexed
spring.cloud.stream.bindings.output.content.type=application/json
server.port=0


logging.level.org.springframework.web=ERROR
logging.level.io.ossim.omar.scdf.stager=DEBUG

# Don't use Cloud Formation
cloud.aws.stack.auto=false

# Stager url
stager.add.raster.url=https://omar-dev.ossim.io:443/omar-stager/dataManager/addRaster?

# Indexing properties
stager.build.histograms=false
stager.build.overviews=false
stager.background=false
```
