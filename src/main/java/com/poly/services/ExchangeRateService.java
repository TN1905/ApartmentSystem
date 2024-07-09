package com.poly.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExchangeRateService {
	@Value("${exchangerate.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public double getExchangeRate(String fromCurrency, String toCurrency) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(API_URL + apiKey + "/latest/" + fromCurrency);

        Map<String, Object> response = restTemplate.getForObject(uriBuilder.toUriString(), Map.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("conversion_rates");

        return rates.get(toCurrency);
    }
}
