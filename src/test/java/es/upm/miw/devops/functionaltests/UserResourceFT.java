package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.rest.user.Province;
import es.upm.miw.devops.rest.user.Role;
import es.upm.miw.devops.rest.user.User;
import es.upm.miw.devops.rest.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserResourceFT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        String mobile = "+34612" + (1_000_000 + (int) (Math.random() * 9_000_000));

        User user = new User(
                id,
                mobile,
                "Delete",
                "Endpoint",
                "delete-endpoint-" + id + "@upm.es",
                "DEP" + id.toString().substring(0, 8),
                "Calle del Borrado Endpoint 2",
                "Barcelona",
                Province.BARCELONA,
                8002,
                "test_pass",
                Role.CUSTOMER,
                LocalDate.of(2024, 1, 1),
                Boolean.TRUE
        );

        this.userRepository.save(user);
        assertThat(this.userRepository.existsById(id)).isTrue();

        this.webTestClient.delete()
                .uri("/user/{id}", id)
                .exchange()
                .expectStatus().isOk();

        assertThat(this.userRepository.existsById(id)).isFalse();
    }
}
