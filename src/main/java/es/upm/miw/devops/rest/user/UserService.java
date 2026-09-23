package es.upm.miw.devops.rest.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Stream<User> find(UserFindCriteria criteria) {
        return this.findByActiveAndMobile(criteria)
                .filter(user -> this.matchBillable(criteria, user));
    }

    private Stream<User> findByActiveAndMobile(UserFindCriteria criteria) {
        if (!criteria.hasActive() && !criteria.hasMobile()) {
            return this.userRepository.findAll().stream();
        }
        if (criteria.hasMobile() && criteria.hasActive()) {
            return this.userRepository
                    .findByMobileAndActive(criteria.getMobile(), criteria.getActive())
                    .stream();
        }
        if (criteria.hasMobile()) {
            return this.userRepository.findByMobile(criteria.getMobile()).stream();
        }
        return this.userRepository.findByActive(criteria.getActive()).stream();
    }

    private boolean matchBillable(UserFindCriteria criteria, User user) {
        return !criteria.hasBillable()
                || user.isBillable() == criteria.getBillable();
    }

    @Transactional(readOnly = true)
    public User read(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found: " + id));
    }

    public void delete(UUID id) {
        this.userRepository.deleteById(id);
    }

    @Transactional
    public void updateActive(UUID id, Boolean active) {
        User user = this.read(id);
        user.setActive(active);
    }

    @Transactional
    public void update(UUID id, UserDto userDto) {
        User user = this.read(id);
        user.setMobile(userDto.getMobile());
        user.setFirstName(userDto.getFirstName());
        user.setFamilyName(userDto.getFamilyName());
        user.setEmail(userDto.getEmail());
        user.setIdentity(userDto.getIdentity());
        user.setAddress(userDto.getAddress());
        user.setCity(userDto.getCity());
        user.setProvince(userDto.getProvince());
        user.setPostalCode(userDto.getPostalCode());
        user.setRole(userDto.getRole());
        user.setActive(userDto.getActive());
    }

    @Transactional
    public void updateActiveBatch(List<UserActivePatchDto> updates) {
        for (UserActivePatchDto update : updates) {
            User user = this.read(update.getId());
            user.setActive(update.getActive());
        }
    }
}
