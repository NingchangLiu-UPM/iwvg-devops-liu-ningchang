package es.upm.miw.devops.rest.user;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void delete(UUID id) {
        this.userRepository.deleteById(id);
    }
}
