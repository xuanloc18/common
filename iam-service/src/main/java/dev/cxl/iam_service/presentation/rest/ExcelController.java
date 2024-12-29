package dev.cxl.iam_service.presentation.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.service.excel.ExportService;
import dev.cxl.iam_service.application.service.excel.ImportService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("excel")
public class ExcelController {
    private final ImportService importService;
    private final ExportService exportService;

    @PostMapping("/import")
    public APIResponse<List<String>> importUsers(@RequestPart("file") MultipartFile file) {
        List<String> errors;
        try {
            errors = importService.importUsers(file);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return APIResponse.<List<String>>builder().result(errors).build();
    }

    @GetMapping("/export")
    public APIResponse<Boolean> exportUsers() throws IOException {
        return APIResponse.<Boolean>builder()
                .result(exportService.exportToStorage())
                .build();
    }
}
