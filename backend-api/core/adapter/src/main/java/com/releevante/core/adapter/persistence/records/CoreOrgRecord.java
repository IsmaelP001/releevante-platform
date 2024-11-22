package com.releevante.core.adapter.persistence.records;

import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "org", schema = "core")
@Getter
@Setter
@NoArgsConstructor
public class CoreOrgRecord extends PersistableEntity {
  @Id private String id;
  private String name;
  private String type;
  private boolean isActive;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CoreOrgRecord that = (CoreOrgRecord) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
