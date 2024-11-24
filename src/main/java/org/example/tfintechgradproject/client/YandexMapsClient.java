package org.example.tfintechgradproject.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.exception.exceptions.ExternalServiceUnavailable;
import org.example.tfintechgradproject.dto.YandexMapsAddressWithCoordinatesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class YandexMapsClient {

    @Value("${yandexMapsApi.apiKey}")
    private String apiKey;

    private final RestClient restClient;

    public YandexMapsAddressWithCoordinatesResponse getLocationInfo(String address) {
        return acquireLocationInfo(address.replaceAll(" ", "+"));
    }

    public YandexMapsAddressWithCoordinatesResponse getLocationInfo(Double longitude, Double latitude) {
        return acquireLocationInfo(longitude + " " + latitude);
    }

    @RateLimiter(name = "yandexMapsApi", fallbackMethod = "rateLimiterFallback")
    @CircuitBreaker(name = "yandexMapsApi", fallbackMethod = "circuitBreakerFallback")
    private YandexMapsAddressWithCoordinatesResponse acquireLocationInfo(String geocode) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("geocode", geocode)
                        .queryParam("results", "1")
                        .queryParam("format", "json")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(YandexMapsAddressWithCoordinatesResponse.class);
    }

    private YandexMapsAddressWithCoordinatesResponse rateLimiterFallback(Exception e) {
        throw new ExternalServiceUnavailable("Too many requests");
    }

    private YandexMapsAddressWithCoordinatesResponse circuitBreakerFallback(Exception e) {
        throw new ExternalServiceUnavailable("Service is unavailable");
    }
}
