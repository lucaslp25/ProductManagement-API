package com.lucasdev.productmanagementapi.integrations;

import com.lucasdev.productmanagementapi.DTO.ExchangeRateApiResponseDTO;
import com.lucasdev.productmanagementapi.exceptions.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;

@Component("exchangeRateIntegrationClient") //changing component name for avoid conflicts
public class ExchangeRateApiClient {

    //WebClient declaration for doing the HTTP callings
    private final WebClient exchangeRateApiClient;

    // our Bean name!! ensure the API name tha we wanted
    public ExchangeRateApiClient(@Qualifier("exchangeRateApiClient") WebClient exchangeRateApiClient){
        this.exchangeRateApiClient = exchangeRateApiClient;
    }

    //this method get the conversion rate between the base currency and the target currency
    public BigDecimal getConversionRate(String baseCurrencyCode, String targetCurrencyCode) {

        try {
            ExchangeRateApiResponseDTO dto = exchangeRateApiClient.get()
                    .uri("/pair/{baseCode}/{targetCode}", baseCurrencyCode, targetCurrencyCode)
                    .retrieve().bodyToMono(ExchangeRateApiResponseDTO.class).block();

            if (dto == null || dto.getConversionrate() == null) {

                System.err.println("Error. external api response null");
                throw new ServiceUnavailableException("Failed to retrieve conversion rate from external API. Response was null or missing 'conversion_rate'.");
            }
            return dto.getConversionrate();

        } catch (WebClientResponseException e) {
            System.err.println("External currency API error: (HTTP Status: " + e.getStatusCode() + "): " + e.getResponseBodyAsString());
            throw new ServiceUnavailableException("External currency API error: " + e.getResponseBodyAsString());
        }catch (Exception e){
            System.err.println("Failed to connect to external currency API: " + e.getMessage());
            throw new ServiceUnavailableException("Failed to connect to external currency API: " + e.getMessage());
        }
    }
}
