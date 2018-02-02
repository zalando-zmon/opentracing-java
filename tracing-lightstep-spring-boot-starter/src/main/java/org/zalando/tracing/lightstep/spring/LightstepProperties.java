package org.zalando.tracing.lightstep.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author abeverage
 */
@ConfigurationProperties("tracer.lightstep")
public class LightstepProperties {

    private String componentName;
    private String collectorHost;
    private int collectorPort;
    private String collectorProtocol;
    private String accessToken;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
