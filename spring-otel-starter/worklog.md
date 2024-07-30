# Worklog

See https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/

## Step 0 - Infrastructure

Docker compose
- opentelemetry collector
- Elastic
- Kibana

## Step 1 - Add OTEL

Add dependency management for `opentelemetry-instrumentation-bom` in pom.xml
```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.opentelemetry.instrumentation</groupId>
                <artifactId>opentelemetry-instrumentation-bom</artifactId>
                <version>2.6.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

Add dependency for `opentelemetry-spring-boot-starter` in pom.xml
```xml
    <dependencies>
        <dependency>
            <groupId>io.opentelemetry.instrumentation</groupId>
            <artifactId>opentelemetry-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
```

Add otel configuration to application.yaml
```yaml
otel:
  propagators:
    - tracecontext
    - b3
  resource:
    attributes:
      deployment.environment: dev
      service:
        name: cart
        namespace: shop
```

```text
Caused by: io.opentelemetry.sdk.autoconfigure.spi.ConfigurationException: Unrecognized value for otel.propagators: b3. Make sure the artifact including the propagator is on the classpath.
	at io.opentelemetry.sdk.autoconfigure.PropagatorConfiguration.getPropagator(PropagatorConfiguration.java:70) ~[opentelemetry-sdk-extension-autoconfigure-1.40.0.jar:1.40.0]
	at io.opentelemetry.sdk.autoconfigure.PropagatorConfiguration.configurePropagators(PropagatorConfiguration.java:51) ~[opentelemetry-sdk-extension-autoconfigure-1.40.0.jar:1.40.0]
```

See: https://opentelemetry.io/docs/specs/otel/context/api-propagators/#b3-requirements

Adding `opentelemetry-extension-trace-propagators` fixes the problem
```xml
        <dependency>
            <groupId>io.opentelemetry</groupId>
            <artifactId>opentelemetry-extension-trace-propagators</artifactId>
            <version>1.40.0</version>
        </dependency>
```

Add spring boot tracing configuration in application.yaml:
- Consuming: w3c, single b3 and multi b3 headers
- Producing: w3c and single b3 headers
```yaml
management:
  tracing:
    enabled: true
    propagation:
      consume: [w3c,b3,b3_multi]
      produce: [w3c,b3]
    sampling:
      probability: 1
```


```text
java.io.IOException: unexpected end of stream on http://localhost:4318/...
	at okhttp3.internal.http1.Http1ExchangeCodec.readResponseHeaders(Http1ExchangeCodec.kt:210) ~[okhttp-4.12.0.jar:na]
	at okhttp3.internal.connection.Exchange.readResponseHeaders(Exchange.kt:110) ~[okhttp-4.12.0.jar:na]
...
Caused by: java.io.EOFException: \n not found: limit=0 content=…
	at okio.RealBufferedSource.readUtf8LineStrict(RealBufferedSource.kt:335) ~[okio-jvm-3.6.0.jar:na]
```