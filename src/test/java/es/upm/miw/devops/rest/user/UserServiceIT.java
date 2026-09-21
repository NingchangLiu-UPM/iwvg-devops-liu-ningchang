package es.upm.miw.devops.rest.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        String mobile = "+34611" + (1_000_000 + (int) (Math.random() * 9_000_000));

        User user = new User(
                id,
                mobile,
                "Delete",
                "Me",
                "delete-me-" + id + "@upm.es",
                "DEL" + id.toString().substring(0, 8),
                "Calle del Borrado 1",
                "Madrid",
                Province.MADRID,
                28001,
                "test_pass",
                Role.CUSTOMER,
                LocalDate.of(2024, 1, 1),
                Boolean.TRUE
        );

        this.userRepository.save(user);
        assertThat(this.userRepository.existsById(id)).isTrue();

        this.userService.delete(id);

        assertThat(this.userRepository.existsById(id)).isFalse();
    }

    @Test
    void testRead() {
        User user = this.userService.read(SeederForDev.USER_MANAGER_ID);

        assertThat(user.getId()).isEqualTo(SeederForDev.USER_MANAGER_ID);
        assertThat(user.getMobile()).isEqualTo("+34611000202");
        assertThat(user.getFirstName()).isEqualTo("Manager");
        assertThat(user.getRole()).isEqualTo(Role.MANAGER);
        assertThat(user.getActive()).isTrue();
    }

    @Test
    void testReadNotFound() {
        UUID nonExistentId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff9999");

        assertThatThrownBy(() -> this.userService.read(nonExistentId))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(this.userRepository.existsById(SeederForDev.USER_ADMIN_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_MANAGER_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_OPERATOR_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_CUSTOMER_ID)).isTrue();
    }

    @Test
    void testUpdateActive() {
        User before = this.userService.read(SeederForDev.USER_OPERATOR_ID);
        assertThat(before.getActive()).isTrue();

        try {
            this.userService.updateActive(SeederForDev.USER_OPERATOR_ID, false);

            User updated = this.userService.read(SeederForDev.USER_OPERATOR_ID);
            assertThat(updated.getActive()).isFalse();
            assertThat(updated.getRole()).isEqualTo(Role.OPERATOR);
            assertThat(updated.getMobile()).isEqualTo("+34611000203");
        } finally {
            this.userService.updateActive(SeederForDev.USER_OPERATOR_ID, true);

            User restored = this.userService.read(SeederForDev.USER_OPERATOR_ID);
            assertThat(restored.getActive()).isTrue();
        }
    }

    @Test
    void testUpdateActiveNotFound() {
        UUID nonExistentId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff9998");

        assertThatThrownBy(() -> this.userService.updateActive(nonExistentId, false))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(this.userRepository.existsById(SeederForDev.USER_OPERATOR_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_CUSTOMER_ID)).isTrue();
    }

    @Test
    void testFindBillableTrue() {
        List<UUID> ids = this.userService.find(new UserFindCriteria(null, null, true))
                .map(User::getId)
                .collect(Collectors.toList());

        assertThat(ids)
                .contains(SeederForDev.USER_CUSTOMER_COMPLETE_ID,
                        SeederForDev.USER_CUSTOMER_INACTIVE_ID)
                .doesNotContain(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
    }

    @Test
    void testFindBillableFalse() {
        List<UUID> ids = this.userService.find(new UserFindCriteria(null, null, false))
                .map(User::getId)
                .collect(Collectors.toList());

        assertThat(ids)
                .contains(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID)
                .doesNotContain(SeederForDev.USER_CUSTOMER_COMPLETE_ID,
                        SeederForDev.USER_CUSTOMER_INACTIVE_ID,
                        SeederForDev.USER_CUSTOMER_ID);
    }

    @Test
    void testFindActiveFalseAndBillableTrue() {
        List<UUID> ids = this.userService.find(new UserFindCriteria(false, null, true))
                .map(User::getId)
                .collect(Collectors.toList());

        assertThat(ids)
                .contains(SeederForDev.USER_CUSTOMER_INACTIVE_ID)
                .doesNotContain(SeederForDev.USER_CUSTOMER_COMPLETE_ID,
                        SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
    }
}