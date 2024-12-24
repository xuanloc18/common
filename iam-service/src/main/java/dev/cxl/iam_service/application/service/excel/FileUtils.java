package dev.cxl.iam_service.application.service.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static MultipartFile convertToMultipartFile(ByteArrayInputStream stream, String filename)
            throws IOException {
        byte[] content = stream.readAllBytes(); // Đọc tất cả byte từ InputStream

        // Trả về CustomMultipartFile
        return new CustomMultipartFile(content, filename);
    }
}
