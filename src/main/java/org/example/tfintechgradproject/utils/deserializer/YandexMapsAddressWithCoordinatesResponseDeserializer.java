package org.example.tfintechgradproject.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;

import java.io.IOException;

public class YandexMapsAddressWithCoordinatesResponseDeserializer extends StdDeserializer<YandexMapsLocationResponse> {
    protected YandexMapsAddressWithCoordinatesResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    public YandexMapsAddressWithCoordinatesResponseDeserializer() {
        this(null);
    }

    @Override
    public YandexMapsLocationResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        node = node.at("/response/GeoObjectCollection/featureMember").get(0).path("GeoObject");

        YandexMapsLocationResponse response = new YandexMapsLocationResponse();
        response.setAddress(node.at("/metaDataProperty/GeocoderMetaData/Address/formatted").textValue());
        response.setCoordinates(node.at("/Point/pos").textValue());

        return response;
    }
}
