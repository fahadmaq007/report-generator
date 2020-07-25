package com.maqs.reportgenerator.model;

import com.maqs.reportgenerator.exception.ServiceException;
import com.maqs.reportgenerator.model.JsonDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class JsonDocumentTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJsonDocument_createInstance() throws IOException, ServiceException {
        String filename = "resources/data/site.json";
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        JsonDocument site = objectMapper.readValue(data, JsonDocument.class);
//        site.init();

        Assertions.assertThat(site).isNotNull();
        Assertions.assertThat(site.orgId()).isNotNull();
        Assertions.assertThat(site.type()).isNotNull();
//        Assertions.assertThat(site.photos()).isNotEmpty();
    }
}
