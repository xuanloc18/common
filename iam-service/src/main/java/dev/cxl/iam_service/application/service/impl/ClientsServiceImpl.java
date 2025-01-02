package dev.cxl.iam_service.application.service.impl;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.application.service.custom.ClientsService;
import dev.cxl.iam_service.domain.domainentity.Clients;
import dev.cxl.iam_service.domain.repository.ClientRepositoryDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {
    private final ClientRepositoryDomain clientsRepository;

    public void checkClientExists(String clientId, String clientSecret) {
        log.info("checkClientExists {}", clientId + clientSecret);
        log.info(
                "checkClientExists {}",
                clientsRepository.findById(clientId).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXITS)));
        Clients clients =
                clientsRepository.findById(clientId).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXITS));
        log.info("checkClientExists {}", clients);
        if (!clients.getClientSecret().equalsIgnoreCase(clientSecret)) {
            throw new AppException(ErrorCode.CLIENT_FAILED);
        }
    }
}
