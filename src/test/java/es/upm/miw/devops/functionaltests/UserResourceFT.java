package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.rest.user.Province;
import es.upm.miw.devops.rest.user.Role;
import es.upm.miw.devops.rest.user.SeederForDev;
import es.upm.miw.devops.rest.user.User;
import es.upm.miw.devops.rest.user.UserActivePatchDto;
import es.upm.miw.devops.rest.user.UserDto;
import es.upm.miw.devops.rest.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
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
                .uri("/users/{id}", id)
                .exchange()
                .expectStatus().isOk();

        assertThat(this.userRepository.existsById(id)).isFalse();
    }

    @Test
    void testRead() {
        this.webTestClient.get()
                .uri("/users/{id}", SeederForDev.USER_MANAGER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(userDto -> {
                    assertThat(userDto.getId()).isEqualTo(SeederForDev.USER_MANAGER_ID);
                    assertThat(userDto.getMobile()).isEqualTo("+34611000202");
                    assertThat(userDto.getFirstName()).isEqualTo("Manager");
                    assertThat(userDto.getRole()).isEqualTo(Role.MANAGER);
                    assertThat(userDto.getActive()).isTrue();
                });

        assertThat(this.userRepository.existsById(SeederForDev.USER_MANAGER_ID)).isTrue();
    }

    @Test
    void testReadNotFound() {
        this.webTestClient.get()
                .uri("/users/aaaaaaaa-bbbb-cccc-dddd-eeeeffff9999")
                .exchange()
                .expectStatus().isNotFound();

        assertThat(this.userRepository.existsById(SeederForDev.USER_ADMIN_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_CUSTOMER_ID)).isTrue();
    }

    @Test
    void testUpdateActive() {
        User before = this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow();
        assertThat(before.getActive()).isTrue();

        try {
            this.webTestClient.put()
                    .uri("/users/{id}/active", SeederForDev.USER_CUSTOMER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(false)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().isEmpty();

            User updated = this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow();
            assertThat(updated.getActive()).isFalse();
            assertThat(updated.getRole()).isEqualTo(Role.CUSTOMER);
            assertThat(updated.getMobile()).isEqualTo("+34611000204");
        } finally {
            this.webTestClient.put()
                    .uri("/users/{id}/active", SeederForDev.USER_CUSTOMER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(true)
                    .exchange()
                    .expectStatus().isOk();

            User restored = this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow();
            assertThat(restored.getActive()).isTrue();
        }
    }

    @Test
    void testUpdateActiveNotFound() {
        this.webTestClient.put()
                .uri("/users/aaaaaaaa-bbbb-cccc-dddd-eeeeffff9998/active")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(false)
                .exchange()
                .expectStatus().isNotFound();

        assertThat(this.userRepository.existsById(SeederForDev.USER_OPERATOR_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_CUSTOMER_ID)).isTrue();
    }

    @Test
    void testFindBillableTrue() {
        List<UserDto> users = this.webTestClient.get()
                .uri("/users?billable=true")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(users)
                .extracting(UserDto::getId)
                .contains(SeederForDev.USER_CUSTOMER_COMPLETE_ID,
                        SeederForDev.USER_CUSTOMER_INACTIVE_ID)
                .doesNotContain(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
    }

    @Test
    void testFindBillableFalse() {
        List<UserDto> users = this.webTestClient.get()
                .uri("/users?billable=false")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(users)
                .extracting(UserDto::getId)
                .contains(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID)
                .doesNotContain(SeederForDev.USER_CUSTOMER_COMPLETE_ID,
                        SeederForDev.USER_CUSTOMER_INACTIVE_ID,
                        SeederForDev.USER_CUSTOMER_ID);
    }

    @Test
    void testFindActiveFalseAndBillableTrue() {
        List<UserDto> users = this.webTestClient.get()
                .uri("/users?active=false&billable=true")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(users)
                .extracting(UserDto::getId)
                .contains(SeederForDev.USER_CUSTOMER_INACTIVE_ID)
                .doesNotContain(SeederForDev.USER_CUSTOMER_COMPLETE_ID,
                        SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
    }

    @Test
    void testUpdateActiveBatch() {
        Boolean originalManagerActive = this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive();
        Boolean originalCustomerActive = this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow().getActive();

        try {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.FALSE),
                            new UserActivePatchDto(SeederForDev.USER_CUSTOMER_ID, Boolean.FALSE)
                    ))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().isEmpty();

            UserDto managerAfter = this.webTestClient.get()
                    .uri("/users/{id}", SeederForDev.USER_MANAGER_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .returnResult()
                    .getResponseBody();

            UserDto customerAfter = this.webTestClient.get()
                    .uri("/users/{id}", SeederForDev.USER_CUSTOMER_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .returnResult()
                    .getResponseBody();

            assertThat(managerAfter).isNotNull();
            assertThat(managerAfter.getActive()).isFalse();
            assertThat(managerAfter.getFirstName()).isEqualTo("Manager");
            assertThat(managerAfter.getEmail()).isEqualTo("manager@upm.es");
            assertThat(managerAfter.getRole()).isEqualTo(Role.MANAGER);
            assertThat(managerAfter.getRegistrationDate()).isEqualTo(LocalDate.of(2021, 5, 12));

            assertThat(customerAfter).isNotNull();
            assertThat(customerAfter.getActive()).isFalse();
            assertThat(customerAfter.getFirstName()).isEqualTo("Customer");
            assertThat(customerAfter.getEmail()).isEqualTo("customer@upm.es");
            assertThat(customerAfter.getRole()).isEqualTo(Role.CUSTOMER);
            assertThat(customerAfter.getRegistrationDate()).isEqualTo(LocalDate.of(2023, 3, 15));
        } finally {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive),
                            new UserActivePatchDto(SeederForDev.USER_CUSTOMER_ID, originalCustomerActive)
                    ))
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Test
    void testUpdateActiveBatchRollbackOnMissingUser() {
        Boolean originalManagerActive = this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive();
        UUID nonExistentId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff9996");

        try {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.FALSE)
                    ))
                    .exchange()
                    .expectStatus().isOk();

            assertThat(this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive())
                    .isEqualTo(Boolean.FALSE);

            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.TRUE),
                            new UserActivePatchDto(nonExistentId, Boolean.FALSE)
                    ))
                    .exchange()
                    .expectStatus().isNotFound();

            assertThat(this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive())
                    .isEqualTo(Boolean.FALSE);

            assertThat(this.userRepository.existsById(SeederForDev.USER_CUSTOMER_ID)).isTrue();
        } finally {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive)
                    ))
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Test
    void testUpdateActiveBatchEmptyArray() {
        Boolean originalManagerActive = this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive();
        Boolean originalCustomerActive = this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow().getActive();

        try {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().isEmpty();

            assertThat(this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive())
                    .isEqualTo(originalManagerActive);
            assertThat(this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow().getActive())
                    .isEqualTo(originalCustomerActive);
        } finally {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive)
                    ))
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Test
    void testUpdateActiveBatchDuplicateIdLastWriteWins() {
        Boolean originalManagerActive = this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive();

        try {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.FALSE),
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.TRUE)
                    ))
                    .exchange()
                    .expectStatus().isOk();

            assertThat(this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive())
                    .isEqualTo(Boolean.TRUE);
        } finally {
            this.webTestClient.patch()
                    .uri("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(List.of(
                            new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive)
                    ))
                    .exchange()
                    .expectStatus().isOk();
        }
    }
}