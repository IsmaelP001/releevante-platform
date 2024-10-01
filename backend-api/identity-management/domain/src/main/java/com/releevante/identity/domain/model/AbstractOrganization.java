/* (C)2024 */
package com.releevante.identity.domain.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.releevante.types.ImmutableExt;
import java.time.ZonedDateTime;
import org.immutables.value.Value;

@Value.Immutable()
@JsonDeserialize(as = Organization.class)
@JsonSerialize(as = Organization.class)
@ImmutableExt
public abstract class AbstractOrganization {
  abstract OrgId id();

  abstract String name();

  // abstract LoginAccount account();

  abstract String type();

  abstract boolean isActive();

  abstract ZonedDateTime createdAt();

  abstract ZonedDateTime updatedAt();

  public Organization checkIsActive() {
    if (!isActive()) {
      throw new RuntimeException("Org not configured");
    }
    return (Organization) this;
  }
}
