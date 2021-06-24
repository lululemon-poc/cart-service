package com.lululemon.cart.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Getter
@ConfigurationProperties
@PropertySources(
        @PropertySource(value = "classpath:secrets.properties")
)
public class CommercetoolsConfigurationReader {

    @Autowired
    private Environment env;
    private String projectKey;
    private String clientId;
    private String clientSecret;
    private String authUrl;
    private String apiUrl;

    public String getProjectKey() {
        return projectKey;
    }


    public String getClientId() {
        return clientId;
    }


    public String getClientSecret() {
        return clientSecret;
    }


    public String getAuthUrl() {
        return authUrl;
    }


    public String getApiUrl() {
        return apiUrl;
    }


    public CommercetoolsConfigurationReader loadConfigurationByCountry(final String country) {
        this.projectKey = env.getProperty(country.toUpperCase() + ".ctp.projectKey");
        this.clientId = env.getProperty(country.toUpperCase() + ".ctp.clientId");
        this.clientSecret = env.getProperty(country.toUpperCase() + ".ctp.clientSecret");
        this.authUrl = env.getProperty(country.toUpperCase() + ".ctp.authUrl");
        this.apiUrl = env.getProperty(country.toUpperCase() + ".ctp.apiUrl");
        return this;
    }

    @PostConstruct
    public void init() {
        loadConfigurationByCountry("US");
    }
}