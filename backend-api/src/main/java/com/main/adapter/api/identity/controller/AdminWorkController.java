package com.main.adapter.api.identity.controller;

import com.main.adapter.api.response.CustomApiResponse;
import com.main.adapter.api.response.HttpErrorResponse;
import com.main.application.identity.IdentityServiceFacade;
import com.releevante.identity.application.dto.AccountIdDto;
import com.releevante.identity.application.dto.OrgDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
public class AdminWorkController {
  final IdentityServiceFacade identityServiceFacade;

  public AdminWorkController(IdentityServiceFacade identityServiceFacade) {
    this.identityServiceFacade = identityServiceFacade;
  }

  @Operation(
      summary = "Add new organization",
      description = "Create a new organization, only admin users can do")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid data supplied",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = HttpErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = HttpErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden access",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = HttpErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = HttpErrorResponse.class))
            })
      })
  @PostMapping("/org")
  Mono<CustomApiResponse<AccountIdDto>> register(@RequestBody OrgDto org) {
    return identityServiceFacade.create(org).map(CustomApiResponse::from);
  }
}
