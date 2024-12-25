package dev.cxl.iam_service.presentation.rest;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.cxl.iam_service.application.dto.response.APIResponse;
import dev.cxl.iam_service.application.service.PhotoService;

@RestController
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping
    public APIResponse<String> uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        photoService.uploadPhoto(file);
        return APIResponse.<String>builder().result("upload photo success").build();
    }
}
