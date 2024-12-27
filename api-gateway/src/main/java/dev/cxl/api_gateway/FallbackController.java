package dev.cxl.api_gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/iam")
    public String fallbackIamService() {
        return "IAM service is currently unavailable. Please try again later.";
    }

    @RequestMapping("/storage")
    public String fallbackStorageService() {
        return "Storage service is currently unavailable. Please try again later.";
    }
}
