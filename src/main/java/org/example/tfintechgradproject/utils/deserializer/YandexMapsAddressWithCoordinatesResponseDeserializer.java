package org.example.tfintechgradproject.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.tfintechgradproject.dto.YandexMapsAddressWithCoordinatesResponse;

import java.io.IOException;

public class YandexMapsAddressWithCoordinatesResponseDeserializer extends StdDeserializer<YandexMapsAddressWithCoordinatesResponse> {
    protected YandexMapsAddressWithCoordinatesResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    public YandexMapsAddressWithCoordinatesResponseDeserializer() {
        this(null);
    }

    @Override
    public YandexMapsAddressWithCoordinatesResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        node = node.at("/response/GeoObjectCollection/featureMember").get(0).path("GeoObject");
        YandexMapsAddressWithCoordinatesResponse response = new YandexMapsAddressWithCoordinatesResponse();

        response.setAddress(node.at("/metaDataProperty/GeocoderMetaData/Address/formatted").textValue());
        String[] pos = node.at("/Point/pos").textValue().split(" ");
        response.setLongitude(Double.valueOf(pos[0]));
        response.setLatitude(Double.valueOf(pos[1]));

        return response;
    }
}
