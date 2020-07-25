package com.maqs.reportgenerator.services;

import com.maqs.reportgenerator.model.ReportFormat;
import com.maqs.reportgenerator.exception.ServiceException;
import com.maqs.reportgenerator.model.JsonDocument;

import java.io.File;

public interface ReportService {

    File generateReport(File templateDoc, JsonDocument inputData,
                        String outputFileName, ReportFormat format) throws ServiceException;
}
