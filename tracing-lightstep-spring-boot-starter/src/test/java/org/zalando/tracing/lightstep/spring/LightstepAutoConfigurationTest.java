package org.zalando.tracing.lightstep.spring;

import com.lightstep.tracer.jre.JRETracer;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.opentracing.ActiveSpan;
import io.opentracing.NoopTracer;
import io.opentracing.Tracer;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;


/**
 * @author abeverage
 */
@SpringBootTest(classes = { LightstepAutoConfiguration.class })
@RunWith(DataProviderRunner.class)
@TestPropertySource(properties = {
        "tracer.lightstep.enabled=true",
        "tracer.lightstep.access_token=foobarbaz",
        "tracer.lightstep.collector_host=iamthedomain",
        "tracer.lightstep.collector_port=8088",
        "tracer.lightstep.collector_protocol=http",
        "tracer.lightstep.component_name=component"
})
public class LightstepAutoConfigurationTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private Tracer tracer;

    @Autowired
    private LightstepProperties config;

    @Test
    public void testAccessTokenComesFromConfiguration() {
        assertThat(config.getAccessToken(), is("foobarbaz"));
        assertThat(config.getCollectorHost(), is("iamthedomain"));
        assertThat(config.getCollectorPort(), is(8088));
        assertThat(config.getCollectorProtocol(), is("http"));
        assertThat(config.getComponentName(), is("component"));
    }

    @Test
    public void tracerIsLightstepTracer() {
        assertThat(tracer, instanceOf(JRETracer.class));
    }

    @Test
    public void unreachableTracerDoesNotThrow() throws InterruptedException {

        try (ActiveSpan activeSpan = tracer.buildSpan("better-not-throw").startActive()) {
            activeSpan.log("foo");
        }

        Thread.sleep(2000);
    }

    @Test
    @UseDataProvider("invalidConfigurationProvider")
    public void invalidConfigurationReturnsNoopTracer(
            String accessToken, String protocol, String url, int port, String componentName) {

        LightstepProperties badProperties = new LightstepProperties();
        badProperties.setAccessToken(accessToken);
        badProperties.setCollectorHost(url);
        badProperties.setCollectorPort(port);
        badProperties.setCollectorProtocol(protocol);
        badProperties.setComponentName(componentName);

        LightstepAutoConfiguration autoConfigurator = new LightstepAutoConfiguration();
        Tracer tracer = autoConfigurator.tracer(badProperties);
        assertThat(tracer, instanceOf(NoopTracer.class));
    }

    @DataProvider
    public static Object[][] invalidConfigurationProvider() {
        return new Object[][] {
                //  Empty tokens and empty component names are valid configuration:
                { null, "https", "valid-url.com", 8080, "name" },
                { "token", "bad-protocol", "valid-url.com", 8080, "name" },
                { "token", "https", "!!!!", 8080, "name" },
                { "token", "bad-protocol", "valid-url.com", 0, "name" },
        };
    }
}
