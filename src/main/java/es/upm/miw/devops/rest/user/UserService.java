package es.upm.miw.devops.rest.user;

import org.springframework.stereotype.Service;

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
}
