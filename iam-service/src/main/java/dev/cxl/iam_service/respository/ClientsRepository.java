package dev.cxl.iam_service.respository;

import dev.cxl.iam_service.entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, String> {
}
