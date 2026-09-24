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

    @Test
    void testUpdate() {
        User before = this.userService.read(SeederForDev.USER_MANAGER_ID);
        UUID originalRegistrationDateAnchor = before.getId();
        String originalPassword = before.getPassword();

        UserDto updateDto = new UserDto();
        updateDto.setId(SeederForDev.USER_MANAGER_ID);
        updateDto.setMobile("+34611009901");
        updateDto.setFirstName("ManagerUpdated");
        updateDto.setFamilyName("LeadUpdated");
        updateDto.setEmail("manager.updated@upm.es");
        updateDto.setIdentity("A21990102");
        updateDto.setAddress("Avenida del Manager Actualizado 10");
        updateDto.setCity("Barcelona");
        updateDto.setProvince(Province.BARCELONA);
        updateDto.setPostalCode(8002);
        updateDto.setRole(Role.MANAGER);
        updateDto.setRegistrationDate(LocalDate.of(2099, 1, 1));
        updateDto.setActive(Boolean.FALSE);

        try {
            this.userService.update(SeederForDev.USER_MANAGER_ID, updateDto);

            User updated = this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow();
            assertThat(updated.getId()).isEqualTo(SeederForDev.USER_MANAGER_ID);
            assertThat(updated.getId()).isEqualTo(originalRegistrationDateAnchor);
            assertThat(updated.getMobile()).isEqualTo("+34611009901");
            assertThat(updated.getFirstName()).isEqualTo("ManagerUpdated");
            assertThat(updated.getFamilyName()).isEqualTo("LeadUpdated");
            assertThat(updated.getEmail()).isEqualTo("manager.updated@upm.es");
            assertThat(updated.getIdentity()).isEqualTo("A21990102");
            assertThat(updated.getAddress()).isEqualTo("Avenida del Manager Actualizado 10");
            assertThat(updated.getCity()).isEqualTo("Barcelona");
            assertThat(updated.getProvince()).isEqualTo(Province.BARCELONA);
            assertThat(updated.getPostalCode()).isEqualTo(8002);
            assertThat(updated.getRole()).isEqualTo(Role.MANAGER);
            assertThat(updated.getActive()).isFalse();
            assertThat(updated.getPassword()).isEqualTo(originalPassword);
            assertThat(updated.getRegistrationDate()).isEqualTo(before.getRegistrationDate());
        } finally {
            UserDto restoreDto = new UserDto();
            restoreDto.setId(SeederForDev.USER_MANAGER_ID);
            restoreDto.setMobile("+34611000202");
            restoreDto.setFirstName("Manager");
            restoreDto.setFamilyName("Lead");
            restoreDto.setEmail("manager@upm.es");
            restoreDto.setIdentity("A21020102");
            restoreDto.setAddress("Avenida del Manager 10");
            restoreDto.setCity("Madrid");
            restoreDto.setProvince(Province.MADRID);
            restoreDto.setPostalCode(28002);
            restoreDto.setRole(Role.MANAGER);
            restoreDto.setActive(Boolean.TRUE);

            this.userService.update(SeederForDev.USER_MANAGER_ID, restoreDto);

            User restored = this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow();
            assertThat(restored.getMobile()).isEqualTo("+34611000202");
            assertThat(restored.getFirstName()).isEqualTo("Manager");
            assertThat(restored.getActive()).isTrue();
        }
    }

    @Test
    void testUpdateNotFound() {
        UUID nonExistentId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff9997");

        UserDto dto = new UserDto();
        dto.setMobile("+34611009999");
        dto.setFirstName("Ghost");
        dto.setFamilyName("User");
        dto.setEmail("ghost@upm.es");
        dto.setIdentity("G99999999");
        dto.setAddress("Nowhere");
        dto.setCity("Nowhere");
        dto.setProvince(Province.MADRID);
        dto.setPostalCode(28000);
        dto.setRole(Role.CUSTOMER);
        dto.setActive(Boolean.TRUE);

        assertThatThrownBy(() -> this.userService.update(nonExistentId, dto))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode")
                .isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(this.userRepository.existsById(SeederForDev.USER_MANAGER_ID)).isTrue();
        assertThat(this.userRepository.existsById(SeederForDev.USER_ADMIN_ID)).isTrue();
    }

    @Test
    void testUpdatePathIdIsAuthoritative() {
        User before = this.userService.read(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
        String originalFirstName = before.getFirstName();

        UserDto dto = new UserDto();
        dto.setId(SeederForDev.USER_CUSTOMER_ID);
        dto.setMobile("+34611009902");
        dto.setFirstName("PathWinsFirstName");
        dto.setFamilyName("PathWinsFamily");
        dto.setEmail("pathwins@upm.es");
        dto.setIdentity("P99990102");
        dto.setAddress("Calle del Path Wins 1");
        dto.setCity("Sevilla");
        dto.setProvince(Province.SEVILLA);
        dto.setPostalCode(41001);
        dto.setRole(Role.OPERATOR);
        dto.setActive(Boolean.FALSE);

        try {
            this.userService.update(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID, dto);

            User pathUpdated = this.userRepository.findById(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID).orElseThrow();
            assertThat(pathUpdated.getFirstName()).isEqualTo("PathWinsFirstName");
            assertThat(pathUpdated.getFamilyName()).isEqualTo("PathWinsFamily");
            assertThat(pathUpdated.getEmail()).isEqualTo("pathwins@upm.es");
            assertThat(pathUpdated.getRole()).isEqualTo(Role.OPERATOR);

            assertThat(this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow().getFirstName())
                    .isNotEqualTo("PathWinsFirstName");
        } finally {
            UserDto restoreDto = new UserDto();
            restoreDto.setId(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID);
            restoreDto.setMobile(null);
            restoreDto.setFirstName(originalFirstName);
            restoreDto.setFamilyName("Incomplete");
            restoreDto.setEmail(null);
            restoreDto.setIdentity(null);
            restoreDto.setAddress(null);
            restoreDto.setCity(null);
            restoreDto.setProvince(null);
            restoreDto.setPostalCode(null);
            restoreDto.setRole(Role.CUSTOMER);
            restoreDto.setActive(Boolean.TRUE);

            this.userService.update(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID, restoreDto);

            User restored = this.userRepository.findById(SeederForDev.USER_CUSTOMER_INCOMPLETE_ID).orElseThrow();
            assertThat(restored.getFirstName()).isEqualTo(originalFirstName);
        }
    }

    @Test
    void testUpdateActiveBatch() {
        Boolean originalManagerActive = this.userService.read(SeederForDev.USER_MANAGER_ID).getActive();
        Boolean originalCustomerActive = this.userService.read(SeederForDev.USER_CUSTOMER_ID).getActive();

        User managerBefore = this.userService.read(SeederForDev.USER_MANAGER_ID);
        User customerBefore = this.userService.read(SeederForDev.USER_CUSTOMER_ID);

        try {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.FALSE),
                    new UserActivePatchDto(SeederForDev.USER_CUSTOMER_ID, Boolean.FALSE)
            ));

            User managerAfter = this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow();
            User customerAfter = this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow();

            assertThat(managerAfter.getActive()).isFalse();
            assertThat(customerAfter.getActive()).isFalse();

            assertThat(managerAfter.getId()).isEqualTo(managerBefore.getId());
            assertThat(managerAfter.getFirstName()).isEqualTo(managerBefore.getFirstName());
            assertThat(managerAfter.getEmail()).isEqualTo(managerBefore.getEmail());
            assertThat(managerAfter.getRole()).isEqualTo(managerBefore.getRole());
            assertThat(managerAfter.getRegistrationDate()).isEqualTo(managerBefore.getRegistrationDate());

            assertThat(customerAfter.getId()).isEqualTo(customerBefore.getId());
            assertThat(customerAfter.getFirstName()).isEqualTo(customerBefore.getFirstName());
            assertThat(customerAfter.getEmail()).isEqualTo(customerBefore.getEmail());
            assertThat(customerAfter.getRole()).isEqualTo(customerBefore.getRole());
            assertThat(customerAfter.getRegistrationDate()).isEqualTo(customerBefore.getRegistrationDate());
        } finally {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive),
                    new UserActivePatchDto(SeederForDev.USER_CUSTOMER_ID, originalCustomerActive)
            ));

            assertThat(this.userService.read(SeederForDev.USER_MANAGER_ID).getActive()).isEqualTo(originalManagerActive);
            assertThat(this.userService.read(SeederForDev.USER_CUSTOMER_ID).getActive()).isEqualTo(originalCustomerActive);
        }
    }

    @Test
    void testUpdateActiveBatchRollbackOnMissingUser() {
        Boolean originalManagerActive = this.userService.read(SeederForDev.USER_MANAGER_ID).getActive();
        UUID nonExistentId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff9996");

        this.userService.updateActiveBatch(List.of(
                new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.FALSE)
        ));
        try {
            assertThat(this.userService.read(SeederForDev.USER_MANAGER_ID).getActive()).isFalse();

            assertThatThrownBy(() -> this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.TRUE),
                    new UserActivePatchDto(nonExistentId, Boolean.FALSE)
            )))
                    .isInstanceOf(ResponseStatusException.class)
                    .extracting("statusCode")
                    .isEqualTo(HttpStatus.NOT_FOUND);

            Boolean managerActiveAfterFailure =
                    this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive();
            assertThat(managerActiveAfterFailure).isEqualTo(Boolean.FALSE);
        } finally {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive)
            ));
            assertThat(this.userService.read(SeederForDev.USER_MANAGER_ID).getActive()).isEqualTo(originalManagerActive);
        }
    }

    @Test
    void testUpdateActiveBatchEmptyList() {
        Boolean originalManagerActive = this.userService.read(SeederForDev.USER_MANAGER_ID).getActive();

        try {
            this.userService.updateActiveBatch(List.of());

            assertThat(this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive())
                    .isEqualTo(originalManagerActive);
            assertThat(this.userRepository.findById(SeederForDev.USER_CUSTOMER_ID).orElseThrow().getActive())
                    .isEqualTo(Boolean.TRUE);
        } finally {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive)
            ));
        }
    }

    @Test
    void testUpdateActiveBatchDuplicateIdLastWriteWins() {
        Boolean originalManagerActive = this.userService.read(SeederForDev.USER_MANAGER_ID).getActive();

        try {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.FALSE),
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.TRUE)
            ));

            assertThat(this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive())
                    .isEqualTo(Boolean.TRUE);
        } finally {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive)
            ));
        }
    }

    // ================= Stage 7 Bug #12 — RED regression tests =================
    // These tests document the intended correct behaviour:
    // an ADMIN user must NOT be deactivated through PATCH /users.
    // They are expected to FAIL until the production fix is applied.

    @Test
    void testUpdateActiveBatchAdminDeactivationForbidden() {
        Boolean originalAdminActive =
                this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive();
        assertThat(originalAdminActive).isTrue();

        try {
            assertThatThrownBy(() -> this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_ADMIN_ID, Boolean.FALSE)
            )))
                    .isInstanceOf(RuntimeException.class);

            Boolean adminActiveAfter =
                    this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive();
            assertThat(adminActiveAfter).isTrue();
        } finally {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_ADMIN_ID, originalAdminActive)
            ));
            assertThat(this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive())
                    .isEqualTo(originalAdminActive);
        }
    }

    @Test
    void testUpdateActiveBatchAdminKeepActiveAllowed() {
        Boolean originalAdminActive =
                this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive();
        assertThat(originalAdminActive).isTrue();

        try {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_ADMIN_ID, Boolean.TRUE)
            ));

            Boolean adminActiveAfter =
                    this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive();
            assertThat(adminActiveAfter).isTrue();
        } finally {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_ADMIN_ID, originalAdminActive)
            ));
            assertThat(this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive())
                    .isEqualTo(originalAdminActive);
        }
    }

    @Test
    void testUpdateActiveBatchMixedNonAdminAndAdminForbiddenRollsBackNonAdmin() {
        Boolean originalManagerActive =
                this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive();
        Boolean originalAdminActive =
                this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive();
        assertThat(originalAdminActive).isTrue();

        try {
            assertThatThrownBy(() -> this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, Boolean.FALSE),
                    new UserActivePatchDto(SeederForDev.USER_ADMIN_ID, Boolean.FALSE)
            )))
                    .isInstanceOf(RuntimeException.class);

            Boolean managerActiveAfter =
                    this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive();
            Boolean adminActiveAfter =
                    this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive();
            assertThat(managerActiveAfter).isEqualTo(originalManagerActive);
            assertThat(adminActiveAfter).isEqualTo(originalAdminActive);
        } finally {
            this.userService.updateActiveBatch(List.of(
                    new UserActivePatchDto(SeederForDev.USER_MANAGER_ID, originalManagerActive),
                    new UserActivePatchDto(SeederForDev.USER_ADMIN_ID, originalAdminActive)
            ));
            assertThat(this.userRepository.findById(SeederForDev.USER_MANAGER_ID).orElseThrow().getActive())
                    .isEqualTo(originalManagerActive);
            assertThat(this.userRepository.findById(SeederForDev.USER_ADMIN_ID).orElseThrow().getActive())
                    .isEqualTo(originalAdminActive);
        }
    }
}
