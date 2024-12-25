package dev.cxl.iam_service.domain.domainentity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntityDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    @CreatedBy
    protected String createdBy;
    @CreatedDate
    protected Instant createdAt = Instant.now();
    @LastModifiedBy
    protected String lastModifiedBy;
    @LastModifiedDate
    protected Instant lastModifiedAt = Instant.now();
}
