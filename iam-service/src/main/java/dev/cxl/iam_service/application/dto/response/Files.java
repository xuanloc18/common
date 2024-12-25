package dev.cxl.iam_service.application.dto.response;

import dev.cxl.iam_service.infrastructure.entity.AuditableEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Files extends AuditableEntity {
    String iD;
    String fileName;
    String fileType;
    Long fileSize;
    String filePath;
    String fileVersion;
    Boolean visibility;
    String ownerId;
    Boolean deleted;
}
