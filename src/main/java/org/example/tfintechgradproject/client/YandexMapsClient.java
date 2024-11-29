package org.example.tfintechgradproject.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;
import org.example.tfintechgradproject.exception.exceptions.ExternalServiceUnavailable;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
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
    private final WKTReader wktReader;


    public YandexMapsLocationResponse getLocationInfo(String location) {
        if (location.matches("^[-+]?\\d+(\\.\\d+)?\\s+[-+]?\\d+(\\.\\d+)?$")) { // 'location' is coordinates
            return deserialize(acquireLocationInfo(location), location);
        } else { // 'location' is address
            return deserialize(acquireLocationInfo(location.replaceAll(" ", "+")), null);
        }
    }

    @RateLimiter(name = "yandexMapsApi", fallbackMethod = "rateLimiterFallback")
    @CircuitBreaker(name = "yandexMapsApi", fallbackMethod = "circuitBreakerFallback")
    private JsonNode acquireLocationInfo(String geocode) {
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
                .body(JsonNode.class);
    }

    public YandexMapsLocationResponse deserialize(JsonNode node, String coordinates) {
        node = node.at("/response/GeoObjectCollection/featureMember").get(0).path("GeoObject");

        var response = new YandexMapsLocationResponse();
        response.setAddress(node.at("/metaDataProperty/GeocoderMetaData/Address/formatted").textValue());
        if (coordinates == null) {
            coordinates = node.at("/Point/pos").textValue();
        }
        try {
            response.setCoordinates((Point) wktReader.read("POINT (%s)".formatted(coordinates)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private YandexMapsLocationResponse rateLimiterFallback(Exception e) {
        throw new ExternalServiceUnavailable("Too many requests");
    }

    private YandexMapsLocationResponse circuitBreakerFallback(Exception e) {
        throw new ExternalServiceUnavailable("Service is unavailable");
    }
}
