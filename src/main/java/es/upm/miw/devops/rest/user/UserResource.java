package es.upm.miw.devops.rest.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    public static final String USERS = "/users";

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> find(
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "billable", required = false) Boolean billable) {
        return this.userService.find(new UserFindCriteria(active, mobile, billable))
                .map(UserDto::of)
                .toList();
    }
}
