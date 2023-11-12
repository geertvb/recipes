## Objectives

Use Kafka connectors to
- Backup Kafka topics to S3 (Minio)
- Improve message observability
  - Index messages of all topics to OpenSearch
  - Create a trace of Kafka messages
  - Create metrics of Kafka messages

## Backup Kafka topics to Minio

### Challenges

- Find a practical storage bucket and folder structure
- Choose a practical file format
- Capture all metadata: partition, offset, timestamp, headers ...

### Choices

- File format: json lines (jsonl)
- Create a Kafka connect plugin and implement a Transformation.
    See: https://www.confluent.io/blog/kafka-connect-single-message-transformation-tutorial-with-examples/

## Observability

### Challenges

- Index all data and metadata
- Avoid too many fields error
- Convert protobuf to json
- Manage indexes and retention
- Capture special fields
  - Timestamp
  - Producer service/application
  - traceId

### Choices

- Use https://github.com/Aiven-Open/opensearch-connector-for-apache-kafka

## Compose

```shell
docker-compose up -d
```

## Test

Check Kafka connect
```http request
GET http://localhost:8083/
```

Check connectors
```http request
GET http://localhost:8083/connectors
```

Minio admin console
```http request
http://localhost:9001/minio/
```
Login: admin/welcome1