package dev.cxl.iam_service.application.service.impl;

import dev.cxl.iam_service.infrastructure.respository.impl.ClientsRepositoryImpl;
import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.service.custom.ClientsService;
import dev.cxl.iam_service.infrastructure.entity.ClientsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {
    private final ClientsRepositoryImpl clientsRepository;

    public void checkClientExists(String clientId, String clientSecret) {
        log.info("checkClientExists {}", clientId);
        ClientsEntity clients =
                clientsRepository.findById(clientId).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXITS));
        if (!clients.getClientSecret().equalsIgnoreCase(clientSecret)) {
            throw new AppException(ErrorCode.CLIENT_FAILED);
        }
    }
}
