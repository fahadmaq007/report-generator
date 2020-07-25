package com.maqs.reportgenerator.controller;

import com.maqs.reportgenerator.model.ReportFormat;
import com.maqs.reportgenerator.exception.ServiceException;
import com.maqs.reportgenerator.helper.ResponseHelper;
import com.maqs.reportgenerator.model.JsonDocument;
import com.maqs.reportgenerator.services.ReportService;
import com.maqs.reportgenerator.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
@RequestMapping(value = "")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/generate" , method = RequestMethod.POST,
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void generateReport(@RequestPart(value = "template", required = true) MultipartFile templateFile,
                               @RequestPart(value = "data", required = true) JsonDocument inputData,
                               @RequestParam(value = "outputFileName", required = false) String outputFileName,
                               @RequestParam(value = "format", required = false) ReportFormat format,
                               HttpServletResponse response) throws ServiceException {
        log.info("generating report templateFile " + templateFile);
        log.info("format " + format);
        try {
            if (format == null) {
                format = ReportFormat.pdf;
            }
            File templateDoc = Util.getFile(templateFile);
            File file = reportService.generateReport(templateDoc, inputData, outputFileName, format);
            ResponseHelper.serveFile(response, file, format);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
