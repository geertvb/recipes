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