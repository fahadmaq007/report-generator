package com.maqs.reportgenerator.services;

import com.maqs.reportgenerator.model.Photo;
import com.maqs.reportgenerator.model.ReportFormat;
import com.maqs.reportgenerator.exception.ServiceException;
import com.maqs.reportgenerator.model.JsonDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    public ReportServiceImpl() {
    }

    @Override
    public File generateReport(File templateDoc, JsonDocument inputData,
                               String outputFileName, ReportFormat format) throws ServiceException {
        File outputFile = null;
        File outDocxFile = null;
        try {
            if (outputFileName == null) {
                outputFileName = "generated_" + templateDoc.getName();
            }
            // 1) Load Docx file by filling Velocity template engine and cache
            // it to the registry
            InputStream in = new FileInputStream( templateDoc );
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport( in,
                    TemplateEngineKind.Velocity );

            FieldsMetadata metadata = report.createFieldsMetadata();
            metadata.setBehaviour( NullImageBehaviour.KeepImageTemplate );
            metadata.load( "photos", Photo.class, true );
            metadata.addFieldAsList("photos");
            report.setFieldsMetadata(metadata);

            // 2) Create context Java model
            IContext context = report.createContext();
            for (String key : inputData.keySet()) {
                context.put( key, inputData.get(key) );
            }
            List<Map<String, Object>> jsonPhotos = (List<Map<String, Object>>) inputData.get("photos");
            List<Photo> photos = loadPhotos(jsonPhotos);
            context.put( "photos", photos );
            // 3) Generate report by merging Java model with the Docx
            String docxPath = ReportFormat.docx.getFilePath(outputFileName);
            outDocxFile = new File( docxPath );
            OutputStream out = new FileOutputStream( outDocxFile );
            report.process( context, out );

            if (ReportFormat.pdf == format) {
                String pdfPath = format.getFilePath(outputFileName);
                outputFile = convertDocxToPdf(docxPath, pdfPath);
            } else {
                outputFile = outDocxFile;
            }
        } catch ( Exception e ) {
            throw new ServiceException(e.getMessage(), e);
        }
        return outputFile;
    }

    private List<Photo> loadPhotos(List<Map<String, Object>> jsonPhotos) throws IOException {
        if (jsonPhotos == null || jsonPhotos.isEmpty()) {
            return null;
        }
        List<Photo> photos = new ArrayList<>();
        Random random = new Random();
        for (Map<String, Object> jsonPhoto : jsonPhotos) {
            int num = random.nextInt(3);
            String path = String.format("resources/images/cons%d.jpg", num);
            Photo photo = new Photo();
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            photo.setBytes(bytes);
            photo.setLatitude((Double) jsonPhoto.get("latitude"));
            photo.setLongitude((Double) jsonPhoto.get("longitude"));
            String googleUrl = "https://www.google.com/maps/@%s,%s,15z";
            String url = String.format(googleUrl, photo.getLatitude(), photo.getLongitude());
            photo.setUrl(url);
            photos.add(photo);
        }
        return photos;
    }

    private File convertDocxToPdf(String inPath, String outPath) throws Exception {
        long startTime = System.currentTimeMillis();
        InputStream is = null;
        File outFile = null;
        try {
            is = new FileInputStream(inPath);
            // 1) Load docx with POI XWPFDocument
            XWPFDocument document = new XWPFDocument( is );

            // 2) Convert POI XWPFDocument 2 PDF with iText
            outFile = new File( outPath );
            OutputStream out = new FileOutputStream( outFile );
            PdfOptions options = PdfOptions.create().fontEncoding( "windows-1250" );
            PdfConverter.getInstance().convert( document, out, options );
        } finally {
            if (is != null) {
                is.close();
            }
        }
        log.debug( "Generated pdf in " + ( System.currentTimeMillis() - startTime ) + " ms." );
        return outFile;
    }

    public Map<String, Object>  getData() throws Exception {
        Map<String, Object> data = null;
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] dataBytes = Files.readAllBytes(Paths.get(getClass().getResource("/data/site.json").toURI()));
        data = objectMapper.readValue(dataBytes, Map.class);
        return data;
    }
}
