package com.poly.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.services.ExchangeRateService;

@CrossOrigin("*")
@RestController
public class ExchangeRateController {
	@Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/api/exchange-rate")
    public Map<String, Object> getExchangeRate(@RequestParam(defaultValue = "USD") String fromCurrency, @RequestParam(defaultValue = "VND") String toCurrency) {
        double rate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        Map<String, Object> response = new HashMap<>();
        response.put("fromCurrency", fromCurrency);
        response.put("toCurrency", toCurrency);
        response.put("rate", rate);
        return response;
    }
}
