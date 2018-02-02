# Lightstep Guice Starter

A Guice configuration module forOpenTracing with Lightstep.

```
        <dependency>
            <groupId>org.zalando</groupId>
            <artifactId>tracing-lightstep-guice-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        
        <!-- Followed by any other optional opentracing-contrib packages needed: -->
        
        <dependency>
            <groupId>io.opentracing.contrib</groupId>
            <artifactId>opentracing-web-servlet-filter</artifactId>
            <version>VERSION</version>
        </dependency>
        
        <!-- ... -->
```

And configure the following properties:

```
TRACER_LIGHTSTEP_ENABLED=true
TRACER_LIGHTSTEP_COMPONENT_NAME=<your application name>
TRACER_LIGHTSTEP_COLLECTOR_HOST=<your collector url>
TRACER_LIGHTSTEP_COLLECTOR_PORT=443
TRACER_LIGHTSTEP_ACCESS_TOKEN=<your access token - use an encrypted secret reference in your deployment manifest>
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

And include when creating your injector:

```
Guice.createInjector(Module1.class, Module2.class, ...LightstepModule.class..., ModuleN.class)
```

And @Inject where needed:

```
@Inject
Tracer tracer;

...

@Inject
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
