package es.upm.miw.devops.rest.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByMobile(String mobile);

    Optional<User> findByMobileAndActive(String mobile, Boolean active);

    List<User> findByActive(Boolean active);
}
