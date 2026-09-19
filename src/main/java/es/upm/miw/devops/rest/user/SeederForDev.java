package es.upm.miw.devops.rest.user;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Configuration
@Profile({"dev", "test"})
public class SeederForDev {

    public static final UUID USER_ADMIN_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0001");
    public static final UUID USER_MANAGER_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0002");
    public static final UUID USER_OPERATOR_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0003");
    public static final UUID USER_CUSTOMER_COMPLETE_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0004");
    public static final UUID USER_CUSTOMER_INCOMPLETE_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0005");
    public static final UUID USER_CUSTOMER_INACTIVE_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0006");

    @Bean
    public ApplicationRunner seedUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }
            userRepository.saveAll(List.of(
                    new User(
                            USER_ADMIN_ID,
                            "+34600000001",
                            "Admin",
                            "Root",
                            "admin@upm.es",
                            "00000001A",
                            "Calle del Rectorado 1",
                            "Madrid",
                            Province.MADRID,
                            28001,
                            "admin_pass",
                            Role.ADMIN,
                            LocalDate.of(2020, 1, 1),
                            Boolean.TRUE
                    ),
                    new User(
                            USER_MANAGER_ID,
                            "+34600000002",
                            "Manager",
                            "Lead",
                            "manager@upm.es",
                            "00000002B",
                            "Avenida del Manager 10",
                            "Madrid",
                            Province.MADRID,
                            28002,
                            "manager_pass",
                            Role.MANAGER,
                            LocalDate.of(2021, 5, 12),
                            Boolean.TRUE
                    ),
                    new User(
                            USER_OPERATOR_ID,
                            "+34600000003",
                            "Operator",
                            "Support",
                            "operator@upm.es",
                            "00000003C",
                            "Plaza del Operador 5",
                            "Barcelona",
                            Province.BARCELONA,
                            8001,
                            "operator_pass",
                            Role.OPERATOR,
                            LocalDate.of(2022, 9, 20),
                            Boolean.TRUE
                    ),
                    new User(
                            USER_CUSTOMER_COMPLETE_ID,
                            "+34600000004",
                            "Customer",
                            "Complete",
                            "customer.complete@upm.es",
                            "00000004D",
                            "Calle del Cliente 4",
                            "Valencia",
                            Province.VALENCIA,
                            46001,
                            "customer_pass",
                            Role.CUSTOMER,
                            LocalDate.of(2023, 3, 15),
                            Boolean.TRUE
                    ),
                    new User(
                            USER_CUSTOMER_INCOMPLETE_ID,
                            null,
                            "Customer",
                            "Incomplete",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            Role.CUSTOMER,
                            LocalDate.of(2024, 7, 1),
                            Boolean.TRUE
                    ),
                    new User(
                            USER_CUSTOMER_INACTIVE_ID,
                            "+34600000006",
                            "Customer",
                            "Inactive",
                            "customer.inactive@upm.es",
                            "00000006F",
                            "Calle del Antiguo Cliente 6",
                            "Sevilla",
                            Province.SEVILLA,
                            41001,
                            "inactive_pass",
                            Role.CUSTOMER,
                            LocalDate.of(2019, 11, 11),
                            Boolean.FALSE
                    )
            ));
        };
    }
}
