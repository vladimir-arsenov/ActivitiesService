package org.example.tfintechgradproject.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.tfintechgradproject.dto.request.CreateActivityRequestDto;
import org.example.tfintechgradproject.dto.request.PatchActivityRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(extensionScanningEnabled = true)
@AutoConfigureMockMvc
@Testcontainers
@WithMockUser(username = "user@user")
class ActivityRequestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        configureFor(wireMockExtension.getPort());
    }

    @BeforeEach
    void setUp() throws IOException {
        Path jsonPath = new ClassPathResource("json/yandex_maps_response.json").getFile().toPath();
        var json = Files.readString(jsonPath);
        stubFor(any(urlPathEqualTo("/1.x"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(json)
                )
        );
    }

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicPort())
            .build();

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:12-3.0").asCompatibleSubstituteFor("postgres")
    )
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("yandexMapsApi.url", () -> wireMockExtension.baseUrl() + "/1.x");
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void getClosestActivityRequests_validParams_shouldReturnRequests() throws Exception {
        mockMvc.perform(get("/api/v1/activity-request")
                .param("activity", "1")
                .param("location", "13.13 4.0")
                .param("radius", "3000"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void getPreview_validId_shouldReturnPreview() throws Exception {
        mockMvc.perform(get("/api/v1/activity-request/{id}/preview", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void get_validId_shouldReturnActivityRequest() throws Exception {
        mockMvc.perform(get("/api/v1/activity-request/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void get_invalidId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/activity-request/{id}", 999L))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "admin@admin")
    @WithUserDetails(value = "admin@admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void get_wrongUser_shouldReturnActivityRequest() throws Exception {
        mockMvc.perform(get("/api/v1/activity-request/{id}", 4L))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void patch_validRequest_shouldUpdateActivityRequest() throws Exception {
        PatchActivityRequestDto patchDto = new PatchActivityRequestDto();

        mockMvc.perform(patch("/api/v1/activity-request/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void patch_invalidId_shouldReturnNotFound() throws Exception {
        PatchActivityRequestDto patchDto = new PatchActivityRequestDto();

        mockMvc.perform(patch("/api/v1/activity-request/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "admin@admin", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void join_validId_shouldJoinActivityRequest() throws Exception {
        mockMvc.perform(post("/api/v1/activity-request/{id}/join", 1))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    void add_validRequest_shouldAddActivityRequest() throws Exception {
        CreateActivityRequestDto createDto = new CreateActivityRequestDto(1L, "134 12.0342",
                LocalDateTime.now().plusDays(1),  LocalDateTime.now().plusDays(2), "comment", 5);

        mockMvc.perform(post("/api/v1/activity-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void add_invalidRequest_shouldReturnBadRequest() throws Exception {
        CreateActivityRequestDto createDto = new CreateActivityRequestDto();

        mockMvc.perform(post("/api/v1/activity-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "user@user", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void delete_validId_shouldDeleteActivityRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/activity-request/{id}", 1))
                .andExpect(status().isOk());
    }
}