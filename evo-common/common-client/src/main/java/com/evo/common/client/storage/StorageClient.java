package com.evo.common.client.storage;

import com.evo.common.config.FeignClientConfiguration;
import com.evo.common.dto.response.APIResponse;
import com.evo.common.dto.response.Files;
import com.evo.common.dto.response.FilesSearchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@FeignClient(
        url = "http://localhost:8099/api/storage",
        name = "storage",
        contextId = "storage-service",
        configuration = FeignClientConfiguration.class)

public interface StorageClient {

    @PostMapping(value = "/public", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    APIResponse<String> uploadFile(
            @RequestPart("file") List<MultipartFile> files, @RequestParam("ownerID") String ownerID);

    @GetMapping("/public/{fileID}")
    APIResponse<Files> getFilePub(@PathVariable("fileID") String fileID);

    @GetMapping("/public/view/{fileID}")
    ResponseEntity<byte[]> getFileViewPublic(
            @PathVariable("fileID") String fileID,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam(value = "ratio", required = false) Double ratio);

    // PRIVATE
    @PostMapping(value = "/private", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    APIResponse<String> createFiles(
            @RequestPart("file") List<MultipartFile> files, @RequestParam("ownerID") String ownerID);

    @PostMapping(value = "/private/import",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String importFiles(
            @RequestPart("file") MultipartFile files, @RequestParam("ownerID") String ownerID);

    @GetMapping("/private/{fileID}")
    APIResponse<Files> getFilePrivate(@PathVariable("fileID") String fileID);

    @GetMapping("/private/view/{fileID}")
    ResponseEntity<byte[]> getFileViewPrivate(
            @PathVariable("fileID") String fileID,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam(value = "ratio", required = false) Double ratio);

    @PostMapping("/private/{fileID}/deleted")
    APIResponse<String> deleteFilePrivate(@PathVariable("fileID") String fileID);

    @GetMapping("/private/{fileID}/download")
    ResponseEntity<Resource> downloadFile(@PathVariable("fileID") String fileId);

    @GetMapping("/public/view-profile/{fileID}")
    ResponseEntity<Resource> viewProfile(@PathVariable("fileID") String fileId);

    @PostMapping(value = "/private/get")
    APIResponse<?> getFiles(@RequestBody FilesSearchRequest request);

    @PostMapping(value = "/public/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    APIResponse<String> createProfile(
            @RequestPart("file") MultipartFile files, @RequestParam("ownerID") String ownerID);
}