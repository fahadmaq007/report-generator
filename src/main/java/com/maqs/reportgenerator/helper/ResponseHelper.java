package com.maqs.reportgenerator.helper;


import com.maqs.reportgenerator.model.ReportFormat;
import com.maqs.reportgenerator.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ResponseHelper {
	
	public static void serveFile(HttpServletResponse response, File file, ReportFormat format) throws ServiceException {
		String filename = file.getName();
		if (ReportFormat.pdf == format) {
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		} else if (ReportFormat.docx == format) {
			response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		}
		log.debug("serving file {} of Content-Type : {} & Disposition: {}",
				filename,
				response.getHeader("Content-Type"),
				response.getHeader("Content-Disposition"));
		try {
			InputStream is = new FileInputStream(file);
			if (is != null) {
				org.apache.commons.io.IOUtils.copy(is,
						response.getOutputStream());
				is.close();
				response.flushBuffer();
			}
		} catch (IOException ex) {
			throw new ServiceException(ex.getMessage(), ex);
		}
	}

}
