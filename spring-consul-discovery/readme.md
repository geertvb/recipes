# Consul metadata

## Build

```shell
mvn clean install
```

## Run

### Start consul

```shell
docker compose up -d
```

### Start applications

Address Service
```shell
cd address-service
mvn spring-boot:run
```

Person Service
```shell
cd person-service
mvn spring-boot:run
```

### Test

```http request
GET http://localhost:8500/v1/catalog/services
Accept: application/json
Content-Type: application/json
```

```http request
GET http://localhost:8500/v1/catalog/service/address-service
Accept: application/json
Content-Type: application/json
```

```http request
GET http://localhost:8500/v1/catalog/service/person-service
Accept: application/json
Content-Type: application/json
```
