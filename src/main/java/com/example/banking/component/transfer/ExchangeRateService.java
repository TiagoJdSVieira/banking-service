package com.example.banking.component.transfer;

import com.example.banking.config.ApplicationProperties;
import com.example.banking.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public double getExchangeRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) return 1d;

        ExchangeRateResponse rates = getExchangeRates(fromCurrency);
        Double rate = rates.getRates().get(toCurrency);

        if (rate == null) {
            throw new ResourceNotFoundException("Exchange rate not found");
        }

        return rate;
    }

    private ExchangeRateResponse getExchangeRates(String currency) {
        final String url = applicationProperties.exchangeRateUrl() + currency;
        return restTemplate.getForObject(url, ExchangeRateResponse.class);
    }
}
