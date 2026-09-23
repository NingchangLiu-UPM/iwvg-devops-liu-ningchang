package es.upm.miw.devops.functionaltests;

import es.upm.miw.devops.rest.user.Province;
import es.upm.miw.devops.rest.user.Role;
import es.upm.miw.devops.rest.user.SeederForDev;
import es.upm.miw.devops.rest.user.User;
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
    void testUpdate() {
        User before = this.userRepository.findById(SeederForDev.USER_OPERATOR_ID).orElseThrow();

        UserDto updateDto = new UserDto();
        updateDto.setId(SeederForDev.USER_OPERATOR_ID);
        updateDto.setMobile("+34611009903");
        updateDto.setFirstName("OperatorUpdated");
        updateDto.setFamilyName("SupportUpdated");
        updateDto.setEmail("operator.updated@upm.es");
        updateDto.setIdentity("A21990103");
        updateDto.setAddress("Plaza del Operador Actualizado 5");
        updateDto.setCity("Valencia");
        updateDto.setProvince(Province.VALENCIA);
        updateDto.setPostalCode(46002);
        updateDto.setRole(Role.OPERATOR);
        updateDto.setActive(Boolean.FALSE);

        try {
            this.webTestClient.put()
                    .uri("/users/{id}", SeederForDev.USER_OPERATOR_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody().isEmpty();

            UserDto updated = this.webTestClient.get()
                    .uri("/users/{id}", SeederForDev.USER_OPERATOR_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .returnResult()
                    .getResponseBody();

            assertThat(updated).isNotNull();
            assertThat(updated.getId()).isEqualTo(SeederForDev.USER_OPERATOR_ID);
            assertThat(updated.getMobile()).isEqualTo("+34611009903");
            assertThat(updated.getFirstName()).isEqualTo("OperatorUpdated");
            assertThat(updated.getFamilyName()).isEqualTo("SupportUpdated");
            assertThat(updated.getEmail()).isEqualTo("operator.updated@upm.es");
            assertThat(updated.getIdentity()).isEqualTo("A21990103");
            assertThat(updated.getAddress()).isEqualTo("Plaza del Operador Actualizado 5");
            assertThat(updated.getCity()).isEqualTo("Valencia");
            assertThat(updated.getProvince()).isEqualTo(Province.VALENCIA);
            assertThat(updated.getPostalCode()).isEqualTo(46002);
            assertThat(updated.getRole()).isEqualTo(Role.OPERATOR);
            assertThat(updated.getActive()).isFalse();

            assertThat(updated.getRegistrationDate()).isEqualTo(before.getRegistrationDate());
        } finally {
            UserDto restoreDto = new UserDto();
            restoreDto.setId(SeederForDev.USER_OPERATOR_ID);
            restoreDto.setMobile("+34611000203");
            restoreDto.setFirstName("Operator");
            restoreDto.setFamilyName("Support");
            restoreDto.setEmail("operator@upm.es");
            restoreDto.setIdentity("A21020103");
            restoreDto.setAddress("Plaza del Operador 5");
            restoreDto.setCity("Barcelona");
            restoreDto.setProvince(Province.BARCELONA);
            restoreDto.setPostalCode(8001);
            restoreDto.setRole(Role.OPERATOR);
            restoreDto.setActive(Boolean.TRUE);

            this.webTestClient.put()
                    .uri("/users/{id}", SeederForDev.USER_OPERATOR_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(restoreDto)
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Test
    void testUpdateNotFound() {
        UserDto dto = new UserDto();
        dto.setMobile("+34611009999");
        dto.setFirstName("Ghost");
        dto.setFamilyName("Endpoint");
        dto.setEmail("ghost-endpoint@upm.es");
        dto.setIdentity("E99999999");
        dto.setAddress("Nowhere Endpoint");
        dto.setCity("Nowhere");
        dto.setProvince(Province.MADRID);
        dto.setPostalCode(28000);
        dto.setRole(Role.CUSTOMER);
        dto.setActive(Boolean.TRUE);

        this.webTestClient.put()
                .uri("/users/aaaaaaaa-bbbb-cccc-dddd-eeeeffff9997")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isNotFound();

        assertThat(this.userRepository.existsById(SeederForDev.USER_OPERATOR_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_CUSTOMER_ID)).isTrue();
    }

    @Test
    void testUpdatePathIdIsAuthoritative() {
        UserDto dto = new UserDto();
        dto.setId(SeederForDev.USER_CUSTOMER_ID);
        dto.setMobile("+34611009904");
        dto.setFirstName("EndpointPathWins");
        dto.setFamilyName("EndpointFamilyWins");
        dto.setEmail("endpoint-pathwins@upm.es");
        dto.setIdentity("Q99990104");
        dto.setAddress("Calle del Endpoint Path Wins 1");
        dto.setCity("Sevilla");
        dto.setProvince(Province.SEVILLA);
        dto.setPostalCode(41002);
        dto.setRole(Role.MANAGER);
        dto.setActive(Boolean.FALSE);

        try {
            this.webTestClient.put()
                    .uri("/users/{id}", SeederForDev.USER_CUSTOMER_INCOMPLETE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(dto)
                    .exchange()
                    .expectStatus().isOk();

            UserDto pathUpdated = this.webTestClient.get()
                    .uri("/users/{id}", SeederForDev.USER_CUSTOMER_INCOMPLETE_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .returnResult()
                    .getResponseBody();

            assertThat(pathUpdated).isNotNull();
            assertThat(pathUpdated.getId()).isEqualTo(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
            assertThat(pathUpdated.getFirstName()).isEqualTo("EndpointPathWins");
            assertThat(pathUpdated.getEmail()).isEqualTo("endpoint-pathwins@upm.es");
            assertThat(pathUpdated.getRole()).isEqualTo(Role.MANAGER);

            UserDto bodyTarget = this.webTestClient.get()
                    .uri("/users/{id}", SeederForDev.USER_CUSTOMER_ID)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .returnResult()
                    .getResponseBody();

            assertThat(bodyTarget).isNotNull();
            assertThat(bodyTarget.getFirstName()).isEqualTo("Customer");
            assertThat(bodyTarget.getEmail()).isEqualTo("customer@upm.es");
        } finally {
            UserDto restoreDto = new UserDto();
            restoreDto.setId(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
            restoreDto.setMobile(null);
            restoreDto.setFirstName("Customer");
            restoreDto.setFamilyName("Incomplete");
            restoreDto.setEmail(null);
            restoreDto.setIdentity(null);
            restoreDto.setAddress(null);
            restoreDto.setCity(null);
            restoreDto.setProvince(null);
            restoreDto.setPostalCode(null);
            restoreDto.setRole(Role.CUSTOMER);
            restoreDto.setActive(Boolean.TRUE);

            this.webTestClient.put()
                    .uri("/users/{id}", SeederForDev.USER_CUSTOMER_INCOMPLETE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(restoreDto)
                    .exchange()
                    .expectStatus().isOk();
        }
    }
}