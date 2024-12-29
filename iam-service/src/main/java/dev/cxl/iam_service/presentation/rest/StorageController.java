package dev.cxl.iam_service.presentation.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.evo.common.client.storage.StorageClient;
import com.evo.common.dto.response.APIResponse;
import com.evo.common.dto.response.FilesSearchRequest;

import dev.cxl.iam_service.application.configuration.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage")
public class StorageController {
    private final StorageClient storageClient;

    @PostMapping("/public")
    public APIResponse<String> createFiles(@RequestPart("file") List<MultipartFile> files) {
        String ownerID = SecurityUtils.getAuthenticatedUserID();
        return storageClient.uploadFile(files, ownerID);
    }

    @GetMapping("/public/{fileID}")
    public APIResponse<?> getFilePub(@PathVariable("fileID") String fileID) {
        return storageClient.getFilePub(fileID);
    }

    @GetMapping("/public/view/{fileID}")
    public ResponseEntity<InputStreamResource> getFileView(
            @PathVariable("fileID") String fileID,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam(value = "ratio", required = false) Double ratio) {
        // Gọi Feign Client để lấy ảnh từ Storage
        ResponseEntity<byte[]> response = storageClient.getFileViewPublic(fileID, width, height, ratio);

        // Lấy body từ response (byte[])
        byte[] imageBytes = response.getBody();

        // Chuyển byte[] thành InputStreamResource
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(imageBytes));
        // Tạo lại headers và trả về phản hồi
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png"); // Đảm bảo Content-Type là đúng
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileID + ".png");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(imageBytes.length)
                .body(resource);
    }

    @PreAuthorize("hasPermission('FILE','CREATE')")
    @PostMapping("/private")
    public APIResponse<String> createFilesPrivate(@RequestPart("file") List<MultipartFile> files) {
        String ownerID = SecurityUtils.getAuthenticatedUserID();
        return storageClient.createFiles(files, ownerID);
    }

    @PreAuthorize("hasPermission('FILE','VIEW')")
    @GetMapping("/private/{fileID}")
    public APIResponse<?> getFilePrivate(@PathVariable("fileID") String fileID) {
        return storageClient.getFilePrivate(fileID);
    }

    @PreAuthorize("hasPermission('FILE','VIEW')")
    @GetMapping("/private/view/{fileID}")
    public ResponseEntity<InputStreamResource> getFileViewPrivate(
            @PathVariable("fileID") String fileID,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam(value = "ratio", required = false) Double ratio)
            throws IOException { // Gọi Feign Client để lấy ảnh từ Storage
        ResponseEntity<byte[]> response = storageClient.getFileViewPrivate(fileID, width, height, ratio);

        // Lấy body từ response (byte[])
        byte[] imageBytes = response.getBody();

        // Chuyển byte[] thành InputStreamResource
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(imageBytes));
        // Tạo lại headers và trả về phản hồi
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png"); // Đảm bảo Content-Type là đúng
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileID + ".png");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(imageBytes.length)
                .body(resource);
    }

    @PreAuthorize("hasPermission('FILE','DELETE')")
    @PostMapping("/private/{fileID}/deleted")
    public APIResponse<String> deleteFilePrivate(@PathVariable("fileID") String fileID) {

        return storageClient.deleteFilePrivate(fileID);
    }

    @PreAuthorize("hasPermission('FILE','VIEW')")
    @GetMapping("/private/{fileID}/download")
    public ResponseEntity<?> download(@PathVariable("fileID") String fileId) {
        ResponseEntity<Resource> response = storageClient.downloadFile(fileId);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @PreAuthorize("hasPermission('FILE','VIEW')")
    @GetMapping("/private")
    public APIResponse<?> getFiles(@ModelAttribute FilesSearchRequest request) throws IOException {
        log.info("request: {}", request);
        return storageClient.getFiles(request);
    }
}
