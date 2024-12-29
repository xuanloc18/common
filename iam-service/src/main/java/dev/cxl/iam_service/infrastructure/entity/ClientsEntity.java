package dev.cxl.iam_service.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class ClientsEntity {
    @Id
    private String clientId;

    private String clientSecret;
}
