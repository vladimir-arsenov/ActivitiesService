package org.example.tfintechgradproject.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.example.tfintechgradproject.dto.YandexMapsLocationResponse;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.IOException;

public class YandexMapsAddressWithCoordinatesResponseDeserializer extends StdDeserializer<YandexMapsLocationResponse> {

    private WKTReader wktReader;

    protected YandexMapsAddressWithCoordinatesResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    public YandexMapsAddressWithCoordinatesResponseDeserializer() {
        this(null);
    }

    @Override
    public YandexMapsLocationResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (wktReader == null) {
            wktReader = (WKTReader) deserializationContext.findInjectableValue("wktReader", null, null);
        }
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        node = node.at("/response/GeoObjectCollection/featureMember").get(0).path("GeoObject");

        var response = new YandexMapsLocationResponse();
        response.setAddress(node.at("/metaDataProperty/GeocoderMetaData/Address/formatted").textValue());
        String textCoordinates = node.at("/Point/pos").textValue();
        try {
            response.setCoordinates((Point) wktReader.read("POINT (%s)".formatted(textCoordinates)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
