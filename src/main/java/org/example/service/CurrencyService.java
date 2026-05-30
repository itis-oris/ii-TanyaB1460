package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);
    private static final String API_URL = "https://open.er-api.com/v6/latest/RUB";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "currency_rates", key = "'rates'")
    public Map<String, Double> getRates() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 0.011);
        rates.put("EUR", 0.010);

        Request request = new Request.Builder().url(API_URL).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonNode ratesNode = objectMapper.readTree(response.body().string()).path("rates");
                if (ratesNode.has("USD")) rates.put("USD", ratesNode.get("USD").asDouble());
                if (ratesNode.has("EUR")) rates.put("EUR", ratesNode.get("EUR").asDouble());
            }
        } catch (Exception e) {
            log.error("Ошибка получения курсов валют: {}", e.getMessage());
        }

        return rates;
    }
}