package org.twinker.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record Employee(int id, Boolean isActive, String firstName, String lastName, String phone, String email) {
}
