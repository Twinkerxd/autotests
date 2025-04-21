package org.twinker.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private int id;
    private String name;
    private String description;
    private boolean isActive;

    public String toString() {
        return String.format(
                "id: %d%nname: %s%ndescription: %s%nisActive: %b",
                getId(), getName(), getDescription(), isActive()
        );
    }
}
