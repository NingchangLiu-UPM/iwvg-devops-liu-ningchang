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

    public static final UUID USER_ADMIN_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0201");
    public static final UUID USER_MANAGER_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0202");
    public static final UUID USER_OPERATOR_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0203");
    public static final UUID USER_CUSTOMER_ID = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeffff0204");

    @Bean
    public ApplicationRunner seedUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }
            userRepository.saveAll(List.of(
                    new User(
                            USER_ADMIN_ID,
                            "+34611000201",
                            "Admin",
                            "Root",
                            "admin@upm.es",
                            "A21020101",
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
                            "+34611000202",
                            "Manager",
                            "Lead",
                            "manager@upm.es",
                            "A21020102",
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
                            "+34611000203",
                            "Operator",
                            "Support",
                            "operator@upm.es",
                            "A21020103",
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
                            USER_CUSTOMER_ID,
                            "+34611000204",
                            "Customer",
                            "Standard",
                            "customer@upm.es",
                            "A21020104",
                            "Calle del Cliente 4",
                            "Valencia",
                            Province.VALENCIA,
                            46001,
                            "customer_pass",
                            Role.CUSTOMER,
                            LocalDate.of(2023, 3, 15),
                            Boolean.TRUE
                    )
            ));
        };
    }
}
