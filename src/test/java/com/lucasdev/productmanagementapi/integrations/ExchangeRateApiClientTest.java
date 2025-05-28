package com.lucasdev.productmanagementapi.integrations;

import com.lucasdev.productmanagementapi.exceptions.ServiceUnavailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@ActiveProfiles("test") //using the tests properties and my environment variable
class ExchangeRateApiClientTest {

    //Spring will inject the instance of our external API client here.
    @Autowired
    private ExchangeRateApiClient exchangeRateApiClient;

    @Test //Test as a Junit

    //name of test
    @DisplayName("Must search the conversion rate with successfully for valid currencies")
    void shouldSuccessfullyFetchConversionRateForValidCurrencies() {
        // Arrange... here i define the data of my test
        String baseCurrency = "BRL";
        String targetCurrency = "USD";

        // Act... here i execute the method that i want to test
        BigDecimal conversionRate = exchangeRateApiClient.getConversionRate(baseCurrency, targetCurrency);

        //assert... verify if the result is the expected
        assertNotNull(conversionRate, "the conversion rate cannot be null");

        // here verify if the conversion results is positive!
        assertTrue(conversionRate.compareTo(BigDecimal.ZERO) > 0, "the currency rate must be greater than zero");
    }

    @Test
    @DisplayName("Must throw ServiceUnavailableException for currency invalid code")
    void shouldThrowServiceUnavailableExceptionForInvalidCurrencyCodes() {

        String baseCurrency = "INVALID";
        String targetCurrency = "XYZ";

        ServiceUnavailableException thrown = assertThrows(ServiceUnavailableException.class, () -> {
            exchangeRateApiClient.getConversionRate(baseCurrency, targetCurrency);
        }, "A ServiceUnavailableException must be throw for invalid currency code");
    }
}

//first test with jUnit OK!!