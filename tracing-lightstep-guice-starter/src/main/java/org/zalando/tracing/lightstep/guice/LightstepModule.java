package org.zalando.tracing.lightstep.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.lightstep.tracer.jre.JRETracer;
import com.lightstep.tracer.shared.Options;
import io.opentracing.NoopTracerFactory;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author abeverage
 */
public class LightstepModule extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(LightstepModule.class);

    @Override
    protected void configure() {
        bind(LightstepProperties.class).in(Singleton.class);
    }

    @SuppressWarnings("unused")
    @Provides
    @Singleton
    public Tracer getTracer(LightstepProperties tracerConfig) {

        checkNotNull(tracerConfig);

        if (!GlobalTracer.isRegistered()) {
            synchronized (this) {
                if (!GlobalTracer.isRegistered()) {
                    try {
                        Options options = new Options.OptionsBuilder()
                                .withAccessToken(tracerConfig.getAccessToken())
                                .withCollectorHost(tracerConfig.getCollectorHost())
                                .withCollectorPort(tracerConfig.getCollectorPort())
                                .withCollectorProtocol(tracerConfig.getCollectorProtocol())
                                .withComponentName(tracerConfig.getComponentName())
                                .build();

                        Tracer tracer = new JRETracer(options);
                        log.info("Initialized Lightstep Tracer");
                        GlobalTracer.register(tracer);
                    } catch (Exception ex) {
                        //  Catch all.  We do not want invalid tracer configuration to bring down an entire application.
                        //  The config class contains secrets, and out of order config values is a common failure.  We
                        //  should not display any exception content beyond its class (still helpful):
                        log.error("Invalid Lightstep configuration.  Returning a NoopTracer: {}", ex.getClass());

                        //  Common configuration error - wrong dependencies regarding ALPN support:
                        if (!isNullOrEmpty(ex.getMessage()) && ex.getMessage().contains("Jetty ALPN/NPN has not been properly configured")) {
                            log.error(ex.getMessage());
                        }

                        GlobalTracer.register(NoopTracerFactory.create());
                    }
                }
            }
        }

        return GlobalTracer.get();
    }
}
