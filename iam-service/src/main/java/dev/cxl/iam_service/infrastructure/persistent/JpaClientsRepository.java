package dev.cxl.iam_service.infrastructure.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.cxl.iam_service.infrastructure.entity.Clients;

@Repository
public interface JpaClientsRepository extends JpaRepository<Clients, String> {}
