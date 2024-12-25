package dev.cxl.iam_service.application.service;

import java.io.IOException;

import dev.cxl.iam_service.domain.repository.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.cxl.iam_service.infrastructure.entity.User;
import dev.cxl.iam_service.infrastructure.persistent.JpaUserRepository;

@Service
public class PhotoService {

    private final UserRepository userRepository;

    private final UtilUserService utilUser;

    public PhotoService(JpaUserRepository userRespository, UserRepository userRepository, UtilUserService utilUser) {
        this.userRepository = userRepository;
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
        userRepository.save(user);
    }
}
