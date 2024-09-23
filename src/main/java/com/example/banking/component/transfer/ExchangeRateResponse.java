package com.example.banking.component.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRateResponse {

    @JsonProperty("base")
    private String base;

    @JsonProperty("rates")
    private Map<String, Double> rates;
}
