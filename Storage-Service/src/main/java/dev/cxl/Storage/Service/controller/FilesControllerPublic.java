package dev.cxl.Storage.Service.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dev.cxl.Storage.Service.dto.response.APIResponse;
import dev.cxl.Storage.Service.entity.Files;
import dev.cxl.Storage.Service.service.FileServices;
import dev.cxl.Storage.Service.service.FileUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class FilesControllerPublic {

    private final FileUtils fileUtils;

    private final FileServices fileServices;

    @PostMapping()
    public APIResponse<String> createFiles(
            @RequestParam("file") List<MultipartFile> files, @RequestParam("ownerID") String ownerID)
            throws IOException {
        fileServices.createMoreFile(files, ownerID, true);
        return APIResponse.<String>builder().result("Thành công").build();
    }

    @PostMapping("/profile")
    public APIResponse<String> createProfile(
            @RequestPart("file") MultipartFile files, @RequestParam("ownerID") String ownerID) throws IOException {
        return APIResponse.<String>builder()
                .result(fileServices.createProfile(files, ownerID))
                .build();
    }

    @GetMapping("/view-profile/{fileID}")
    public ResponseEntity<?> viewProfile(@PathVariable("fileID") String fileID) throws IOException {

        return fileServices.downloadFile(fileID);
    }

    @GetMapping("/{fileID}")
    public APIResponse<Files> getFilePub(@PathVariable("fileID") String fileID) {
        fileUtils.checkPublic(fileID);
        return APIResponse.<Files>builder().result(fileServices.getFile(fileID)).build();
    }

    @GetMapping("/view/{fileID}")
    public ResponseEntity<InputStreamResource> getFileView(
            @PathVariable("fileID") String fileID,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam(value = "ratio", required = false) Double ratio)
            throws MalformedURLException, IOException {
        fileUtils.checkPublic(fileID);
        return fileServices.viewFile(fileID, width, height, ratio);
    }
}
