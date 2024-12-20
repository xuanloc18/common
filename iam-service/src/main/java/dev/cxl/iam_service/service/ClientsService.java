package dev.cxl.iam_service.service;

import org.springframework.stereotype.Service;

import com.evo.common.exception.AppException;
import com.evo.common.exception.ErrorCode;

import dev.cxl.iam_service.entity.Clients;
import dev.cxl.iam_service.respository.ClientsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientsService {
    private final ClientsRepository clientsRepository;

    public void checkClientExists(String clientId, String clientSecret) {
        Clients clients =
                clientsRepository.findById(clientId).orElseThrow(() -> new AppException(ErrorCode.CLIENT_NOT_EXITS));
        if (!clients.getClientSecret().equalsIgnoreCase(clientSecret)) {
            throw new AppException(ErrorCode.CLIENT_FAILED);
        }
    }
}
