package es.upm.miw.devops.rest.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

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
}