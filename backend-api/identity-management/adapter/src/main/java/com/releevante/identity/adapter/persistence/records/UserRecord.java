/* (C)2024 */
package com.releevante.identity.adapter.persistence.records;

import com.releevante.identity.domain.model.AccountId;
import com.releevante.identity.domain.model.User;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users", schema = "core")
@Getter
@Setter
@NoArgsConstructor
public class UserRecord extends PersistableEntity {
  @Id private String id;
  private String fullName;
  private String accountId;
  private String email;
  private String phoneNumber;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  public static UserRecord from(User user) {
    var record = new UserRecord();
    record.setId(user.id());
    record.setFullName(user.fullName());
    record.setAccountId(user.accountId().value());
    record.setCreatedAt(user.createdAt());
    record.setUpdatedAt(user.updatedAt());
    record.setEmail(user.email());
    record.setPhoneNumber(user.phone());
    return record;
  }

  public User toDomain() {
    return User.builder()
        .id(id)
        .fullName(fullName)
        .accountId(AccountId.of(accountId))
        .email(email)
        .phone(phoneNumber)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .build();
  }
}
