package org.zalando.tracing.lightstep.guice;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * @author abeverage
 */
public class LightstepProperties {

    private static final String ACCESS_TOKEN = "tracer.lightstep.access_token";
    private static final String COLLECTOR_HOST = "tracer.lightstep.collector_host";
    private static final String COLLECTOR_PORT = "tracer.lightstep.collector_port";
    private static final String COLLECTOR_PROTOCOL = "tracer.lightstep.collector_protocol";
    private static final String COMPONENT_NAME = "tracer.lightstep.component_name";

    @Inject(optional = true)
    @Named(ACCESS_TOKEN)
    private String accessToken;

    @Inject(optional = true)
    @Named(COLLECTOR_HOST)
    private String collectorHost;

    @Inject(optional = true)
    @Named(COLLECTOR_PORT)
    private int collectorPort;

    @Inject(optional = true)
    @Named(COLLECTOR_PROTOCOL)
    private String collectorProtocol;

    @Inject(optional = true)
    @Named(COMPONENT_NAME)
    private String componentName;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCollectorHost() {
        return collectorHost;
    }

    public void setCollectorHost(String collectorHost) {
        this.collectorHost = collectorHost;
    }

    public int getCollectorPort() {
        return collectorPort;
    }

    public void setCollectorPort(int collectorPort) {
        this.collectorPort = collectorPort;
    }

    public String getCollectorProtocol() {
        return collectorProtocol;
    }

    public void setCollectorProtocol(String collectorProtocol) {
        this.collectorProtocol = collectorProtocol;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
