package com.releevante.identity.adapter.persistence.records;

import com.releevante.identity.domain.model.*;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "smart_library_access_ctrl", schema = "core")
@Getter
@Setter
@NoArgsConstructor
public class SmartLibraryAccessControlRecord extends PersistableEntity {
  @Id private String id;
  private String orgId;
  private String slid;
  private String credential;
  private String credentialType;
  private String userId;
  private boolean isActive;
  private boolean isSync;
  private ZonedDateTime expiresAt;
  private int accessDueDays;

  public static SmartLibraryAccessControlRecord fromDomain(SmartLibraryAccess access) {
    var record = new SmartLibraryAccessControlRecord();
    record.setId(access.id());
    record.setSlid(access.slid());
    record.setActive(access.isActive());
    record.setCreatedAt(access.createdAt());
    record.setUpdatedAt(access.updatedAt());
    record.setOrgId(access.orgId().value());
    record.setCredential(access.credential().value().value());
    record.setCredentialType(access.credential().key().value());
    record.setExpiresAt(access.expiresAt());
    record.setAccessDueDays(access.accessDueDays());
    record.setSync(access.isSync());
    record.setUserId(access.userId());
    return record;
  }

  public SmartLibraryAccess toDomain() {
    return SmartLibraryAccess.builder()
        .id(id)
        .orgId(OrgId.of(orgId))
        .slid(slid)
        .isActive(isActive)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .accessDueDays(accessDueDays)
        .expiresAt(expiresAt)
        .userId(userId)
        .isSync(isSync)
        .credential(
            AccessCredential.builder()
                .value(AccessCredentialValue.of(credential))
                .key(AccessCredentialKey.of(credentialType))
                .build())
        .build();
  }
}
