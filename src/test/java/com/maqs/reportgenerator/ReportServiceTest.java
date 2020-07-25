package com.maqs.reportgenerator;

import com.maqs.reportgenerator.exception.ServiceException;
import com.maqs.reportgenerator.model.JsonDocument;
import com.maqs.reportgenerator.model.ReportFormat;
import com.maqs.reportgenerator.services.ReportService;
import com.maqs.reportgenerator.services.ReportServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
    private ObjectMapper objectMapper;

    private ReportService reportService;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        reportService = new ReportServiceImpl();
    }

    @Test
    public void testReportGeneration_generatePdf() throws IOException, ServiceException {
        String filename = "resources/data/site.json";
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        JsonDocument site = objectMapper.readValue(data, JsonDocument.class);
//        site.init();
        File templateFile = new File("resources/templates/SiteReportTemplate.docx");
        File outFile = reportService.generateReport(templateFile, site, "site-report", ReportFormat.pdf);
        log.debug("generated output file at " + outFile.getAbsolutePath());
        Assertions.assertThat(outFile).exists();
        Assertions.assertThat(outFile.getName()).endsWith(".pdf");
    }

    @Test
    public void testReportGeneration_generateDocx() throws IOException, ServiceException {
        String filename = "resources/data/site.json";
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        JsonDocument site = objectMapper.readValue(data, JsonDocument.class);
//        site.init();
        File templateFile = new File("resources/templates/SiteReportTemplate.docx");
        File outFile = reportService.generateReport(templateFile, site, "site-report", ReportFormat.docx);
        log.debug("generated output file at " + outFile.getAbsolutePath());
        Assertions.assertThat(outFile).exists();
        Assertions.assertThat(outFile.getName()).endsWith(".docx");
    }

    @Test
    public void testReportGeneration_DemoTemplate_generatePdf() throws IOException, ServiceException {
        String filename = "resources/data/demo.json";
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        JsonDocument site = objectMapper.readValue(data, JsonDocument.class);
//        site.init();
        File templateFile = new File("resources/templates/DemoTemplate.docx");
        File outFile = reportService.generateReport(templateFile, site, "Demo-Report", ReportFormat.pdf);
        log.debug("generated output file at " + outFile.getAbsolutePath());
        Assertions.assertThat(outFile).exists();
        Assertions.assertThat(outFile.getName()).endsWith(".pdf");
    }
}
