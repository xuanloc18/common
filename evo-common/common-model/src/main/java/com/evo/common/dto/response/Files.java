package com.evo.common.dto.response;


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
