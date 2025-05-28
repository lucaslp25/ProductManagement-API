package com.lucasdev.productmanagementapi.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Inject the api key of application.properties
    @Value("${EXCHANGERATE_API_KEY}")
    private String exchangeRateApiKey;

    // Inject the api url of application.properties
    @Value("${exchangerate.api.baseurl}")
    private String exchangeRateApiBaseUrl;

    //uses bean notation because this is external library that are being added to program
    @Bean(name = "exchangeRateApiClient")
    public WebClient exchangeRateApiClient(){
        return WebClient.builder().baseUrl(exchangeRateApiBaseUrl
                + exchangeRateApiKey
                + "/").build();
    }

}
