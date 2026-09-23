package es.upm.miw.devops.rest.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    public static final String USERS = "/users";
    public static final String USER_ID = "/{id}";
    public static final String USER_ACTIVE = "/active";

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

    @GetMapping(UserResource.USER_ID)
    public UserDto read(@PathVariable UUID id) {
        return UserDto.of(this.userService.read(id));
    }

    @PutMapping(UserResource.USER_ID + UserResource.USER_ACTIVE)
    public void updateActive(
            @PathVariable UUID id,
            @RequestBody Boolean active) {
        this.userService.updateActive(id, active);
    }

    @PutMapping(UserResource.USER_ID)
    public void update(
            @PathVariable UUID id,
            @RequestBody UserDto userDto) {
        this.userService.update(id, userDto);
    }

    @PatchMapping
    public void updateActiveBatch(
            @RequestBody List<UserActivePatchDto> updates) {
        this.userService.updateActiveBatch(updates);
    }

    @DeleteMapping(UserResource.USER_ID)
    public void delete(@PathVariable UUID id) {
        this.userService.delete(id);
    }
}
