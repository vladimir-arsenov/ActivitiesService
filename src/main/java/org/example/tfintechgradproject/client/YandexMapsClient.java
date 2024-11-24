package org.example.tfintechgradproject.client;

import lombok.RequiredArgsConstructor;
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

    public YandexMapsAddressWithCoordinatesResponse getCorrectAddressWithCoordinates(String address) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("geocode", address.replaceAll(" ", "+"))
                        .queryParam("results", "1")
                        .queryParam("format", "json")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(YandexMapsAddressWithCoordinatesResponse.class);
    }

    public YandexMapsAddressWithCoordinatesResponse getCorrectAddressWithCoordinates(Double longitude, Double latitude) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("geocode", longitude + " " + latitude)
                        .queryParam("results", "1")
                        .queryParam("format", "json")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(YandexMapsAddressWithCoordinatesResponse.class);
    }

}
