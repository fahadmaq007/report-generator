package com.maqs.reportgenerator.model;

import com.maqs.reportgenerator.util.Util;

public enum ReportFormat {
    docx,

    pdf;

    public String getFilePath(String fileName) {
        return Util.getTempDirectory() + "/" + fileName + "." + this.name();
    }
}
