package com.releevante.core.domain.tags;

import com.releevante.types.ImmutableObject;
import java.util.Collections;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableObject
public interface AbstractAuthorizedOrigin {
  String id();

  String type();

  @Value.Default
  default List<String> roles() {
    return Collections.emptyList();
  }
}
