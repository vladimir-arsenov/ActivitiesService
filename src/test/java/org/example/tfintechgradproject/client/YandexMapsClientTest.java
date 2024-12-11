package org.example.tfintechgradproject.client;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.tfintechgradproject.dto.response.YandexMapsLocationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(extensionScanningEnabled = true)
public class YandexMapsClientTest {

    @Autowired
    private YandexMapsClient apiClient;

    @Autowired
    private WKTReader wktReader;



    @BeforeAll
    public static void setUp() {
        configureFor(wireMockExtension.getPort());
    }

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("yandexMapsApi.url", () -> wireMockExtension.baseUrl() + "/1.x");
    }


    @Test
    public void test_getLocationInfo() throws ParseException, IOException {
        var response = new YandexMapsLocationResponse("Дубай, бульвар Мухаммед Бин Рашид, 1", (Point) wktReader.read("POINT(25.197300 55.274243)"));
        var address = "Address";
        Path jsonPath = new ClassPathResource("json/yandex_maps_response.json").getFile().toPath();
        var json = Files.readString(jsonPath);
        stubFor(any(urlPathEqualTo("/1.x"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(json)
                        )
        );

        var result = apiClient.getLocationInfo(address);
        assertEquals(response, result);

    }
}
