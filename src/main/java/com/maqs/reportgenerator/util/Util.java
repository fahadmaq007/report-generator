package com.maqs.reportgenerator.util;

import com.maqs.reportgenerator.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class Util {
    public static String getTempDirectory() {
        String tmpdir = System.getProperty("java.io.tmpdir");
        if (tmpdir == null) {
            tmpdir = File.separator;
        }
        if (!tmpdir.endsWith(File.separator)) {
            tmpdir += File.separator;
        }
        return tmpdir;
    }

    public static File getFile(MultipartFile file) throws ServiceException {
        File f = null;
        if (!file.isEmpty()) {
            BufferedOutputStream buffStream = null;
            try {
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                String userHome = getTempDirectory();
                f = new File(userHome + fileName);
                buffStream = new BufferedOutputStream(new FileOutputStream(f));
                buffStream.write(bytes);
                log.debug("file is uploaded at " + f.getAbsolutePath());
            } catch (Exception e) {
                throw new ServiceException(e.getMessage(), e);
            } finally {
                if (buffStream != null) {
                    try {
                        buffStream.close();
                    } catch (IOException e) {
                        throw new ServiceException(e.getMessage(), e);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("file is empty");
        }
        return f;
    }
}
