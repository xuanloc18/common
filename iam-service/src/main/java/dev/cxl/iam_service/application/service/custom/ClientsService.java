package dev.cxl.iam_service.application.service.custom;

import com.evo.common.exception.AppException;

public interface ClientsService {

    void checkClientExists(String clientId, String clientSecret) throws AppException;
}
