package es.upm.miw.devops.rest.user;

import java.util.UUID;

public class UserActivePatchDto {

    private UUID id;
    private Boolean active;

    public UserActivePatchDto() {
        // empty
    }

    public UserActivePatchDto(UUID id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
