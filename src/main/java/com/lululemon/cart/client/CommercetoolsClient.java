package com.lululemon.cart.client;

import com.lululemon.cart.config.CommercetoolsConfigurationReader;
import io.sphere.sdk.client.SphereAccessTokenSupplier;
import io.sphere.sdk.client.SphereAsyncHttpClientFactory;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class CommercetoolsClient {

    public static Map<String, SphereClient> client = new HashMap<>();

    /**
     * Instantiates a new client manager.
     */
    private CommercetoolsClient() {
    }

    /**
     * Get Client.
     *
     * @param configurationReader the credential reader
     * @return the client
     */
    public SphereClient getClient(CommercetoolsConfigurationReader configurationReader) {
        String projectKey = configurationReader.getProjectKey();
        if (client.get(projectKey) == null) {
            SphereClient sphereClient = getSphereClient(configurationReader);
            log.info("Sphere client is created.");
            client.put(configurationReader.getProjectKey(),sphereClient);
            return sphereClient;
        } else {
            return client.get(projectKey);
        }
    }

    /**
     * Creates a blocking sphere client.
     *
     * @param configurationReader the credential reader
     * @return Sphere client
     */
    private SphereClient getSphereClient(CommercetoolsConfigurationReader configurationReader) {
        SphereClientConfig clientConfig = loadCtpClientConfig(configurationReader);
        HttpClient httpClient = new SphereAsyncHttpClientFactory().getClient();
        log.info("Created SphereAsyncHttpClientFactory().getClient()");
        SphereAccessTokenSupplier sphereAccessTokenSupplier = SphereAccessTokenSupplier
                .ofAutoRefresh(clientConfig, httpClient, true);
        log.info("Calling SphereClient.of method of creating the instance.");
        return SphereClient.of(clientConfig, httpClient, sphereAccessTokenSupplier);
    }

    private SphereClientConfig loadCtpClientConfig(CommercetoolsConfigurationReader configurationReader) {
        if (configurationReader != null) {
            log.info("Commercetools configuration loaded for project={} ,", configurationReader.getProjectKey());
            if (configurationReader.getClientSecret() == null) {
                log.debug("Commercetools client secret is null");
            } else {
                log.debug("Commercetools client secret length is {}", configurationReader.getClientSecret().length());
            }
        } else {
            log.info("Commercetools project secrets and properties are not loaded and initialized");
        }
        return SphereClientConfig.of(Objects.requireNonNull(configurationReader).getProjectKey(),
                configurationReader.getClientId(), configurationReader.getClientSecret(),
                configurationReader.getAuthUrl(), configurationReader.getApiUrl());
    }
}
