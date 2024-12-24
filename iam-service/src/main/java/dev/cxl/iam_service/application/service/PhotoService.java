package dev.cxl.iam_service.application.service;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.cxl.iam_service.domain.entity.User;
import dev.cxl.iam_service.infrastructure.respository.UserRespository;

@Service
public class PhotoService {

    private final UserRespository userRespository;

    private final UtilUserService utilUser;

    public PhotoService(UserRespository userRespository, UtilUserService utilUser) {
        this.userRespository = userRespository;
        this.utilUser = utilUser;
    }

    public void uploadPhoto(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("");
        }
        byte[] bytes = file.getBytes();
        String avatar = Base64.encodeBase64String(bytes);
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = utilUser.finUserId(id);
        user.setAvatar(avatar);
        userRespository.save(user);
    }
}
