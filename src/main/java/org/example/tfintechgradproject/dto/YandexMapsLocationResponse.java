package org.example.tfintechgradproject.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tfintechgradproject.utils.deserializer.YandexMapsAddressWithCoordinatesResponseDeserializer;

@JsonDeserialize(using = YandexMapsAddressWithCoordinatesResponseDeserializer.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YandexMapsLocationResponse {
    private String address;
    private String coordinates;
}
