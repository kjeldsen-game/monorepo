package com.kjeldsen.auth.authorization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "internal")
public class InternalRequestsProperties {

    private String origin;
    private Api api;
    private List<Endpoint> endpoints;

    @Getter
    @Setter
    public static class Api {
        private String key;
    }

    @Getter
    @Setter
    public static class Endpoint {
        private String path;
        private String method;
    }
}
