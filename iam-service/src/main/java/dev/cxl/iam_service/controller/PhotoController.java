package dev.cxl.iam_service.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.cxl.iam_service.dto.response.APIResponse;
import dev.cxl.iam_service.service.PhotoService;

@RestController
@RequestMapping("/photo")
public class PhotoController {
    @Autowired
    PhotoService photoService;

    @PostMapping
    public APIResponse<String> uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        photoService.uploadPhoto(file);
        return APIResponse.<String>builder().result("upload photo success").build();
    }
}
