package dev.cxl.iam_service.application.service.impl;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.domain.repository.ClientsRepositoryDomain;
import dev.cxl.iam_service.infrastructure.entity.ClientsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientsServiceImpl {
    private final ClientsRepositoryDomain clientsRepository;

    public void checkClientExists(String clientId, String clientSecret) {
        log.info("checkClientExists {}", clientId);
        log.info("checkClientExists {}", clientsRepository.findAll());
        ClientsEntity clients =
                clientsRepository.findById(clientId).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXITS));
        if (!clients.getClientSecret().equalsIgnoreCase(clientSecret)) {
            throw new AppException(ErrorCode.CLIENT_FAILED);
        }
    }
}
