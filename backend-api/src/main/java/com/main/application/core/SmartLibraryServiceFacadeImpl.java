package com.main.application.core;

import com.releevante.core.application.dto.SmartLibraryDto;
import com.releevante.core.application.dto.SmartLibrarySyncDto;
import com.releevante.core.application.service.SmartLibraryService;
import com.releevante.types.AccountPrincipal;
import com.releevante.types.Slid;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SmartLibraryServiceFacadeImpl implements SmartLibraryServiceFacade {
  private final SmartLibraryService smartLibraryService;

  public SmartLibraryServiceFacadeImpl(SmartLibraryService smartLibraryService) {
    this.smartLibraryService = smartLibraryService;
  }

  @Override
  public Mono<Boolean> synchronize(SmartLibrarySyncDto synchronizeDto) {
    return smartLibraryService.synchronize(synchronizeDto);
  }

  @Override
  public Flux<SmartLibraryDto> validateAccess(AccountPrincipal principal, List<Slid> sLids) {
    return smartLibraryService.validateAccess(principal, sLids);
  }
}
