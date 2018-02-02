# Lightstep Spring-Boot Starter

An autoconfiguration package for OpenTracing with Lightstep.

```
        <dependency>
            <groupId>org.zalando</groupId>
            <artifactId>tracing-lightstep-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        
        <!-- Followed by any other optional opentracing-contrib packages needed: -->
        <dependency>
            <groupId>io.opentracing.contrib</groupId>
            <artifactId>opentracing-spring-cloud-starter</artifactId>
            <version>VERSION</version>
        </dependency>
```

And configure the following properties:
```
tracer.lightstep.enabled=true
tracer.lightstep.component_name=<your application name>
tracer.lightstep.collector_host=<your collector url>
tracer.lightstep.collector_port=443
tracer.lightstep.access_token=<your access token - use an encrypted secret reference in your deployment manifest>
```

Or add to your Kubernetes manifest:
```
            - name: TRACER_LIGHTSTEP_ENABLED
              value: "true"
            - name: TRACER_LIGHTSTEP_COMPONENT_NAME
              value: {{ application }}
            - name: TRACER_LIGHTSTEP_COLLECTOR_HOST
              value: "<collector url>"
            - name: TRACER_LIGHTSTEP_COLLECTOR_PORT
              value: "443"
            - name: TRACER_LIGHTSTEP_COLLECTOR_PROTOCOL
              value: "https"
            - name: TRACER_LIGHTSTEP_ACCESS_TOKEN
              valueFrom:
                secretKeyRef:
                  name: {{ application }}-secrets
                  key: lightstep-token
```

This will create a Lightstep Tracer @Bean that can now be consumed by other OpenTracing autoconfiguration classes, such as https://github.com/opentracing-contrib/java-spring-cloud

And can be @Autowired where needed:

```
@Autowired
Tracer tracer;

...

@Autowired
public TracedClass(Tracer tracer, ...) {
  ...
}
```

Or use the GlobalTracer holder:

```
//  Will return a NoopTracer until the LightstepTracer is configured.
//  Eliminates the need for initialization dependency management if capturing 100% of traces is not required:
TracedClass tracedClass = new TracedClass(GlobalTracer.get(), ...);
```
