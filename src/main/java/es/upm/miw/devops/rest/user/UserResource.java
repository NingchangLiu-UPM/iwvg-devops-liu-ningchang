package es.upm.miw.devops.rest.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {

    public static final String USER = "/user";
    public static final String USER_ID = "/{id}";

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping(UserResource.USER_ID)
    public void delete(@PathVariable UUID id) {
        this.userService.delete(id);
    }
}
