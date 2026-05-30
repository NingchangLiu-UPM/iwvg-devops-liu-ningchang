package es.upm.miw.devops;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UsersDatabaseTest {

    @Test
    void testFindAllReturnsAllSampleUsersInOrder() {
        assertThat(new UsersDatabase().findAll().map(User::getId))
                .containsExactly("1", "2", "3", "4", "5");
    }

    @Test
    void testFirstUserHasExpectedIdentityAndFractions() {
        List<User> users = new UsersDatabase().findAll().toList();
        User first = users.get(0);
        assertThat(first.getId()).isEqualTo("1");
        assertThat(first.getName()).isEqualTo("Susana");
        assertThat(first.getFamilyName()).isEqualTo("Perez");
        assertThat(first.getFractions()).hasSize(3);
        assertThat(first.getFractions().get(0).decimal()).isEqualTo(1.0 / 3);
    }

    @Test
    void testFindAllReturnsAFreshConsumableStreamOnEachCall() {
        UsersDatabase usersDatabase = new UsersDatabase();
        assertThat(usersDatabase.findAll()).hasSize(5);
        assertThat(usersDatabase.findAll()).hasSize(5);
    }

    @Test
    void testSampleDataCoversProperImproperAndNegativeFractions() {
        List<Fraction> allFractions = new UsersDatabase().findAll()
                .flatMap(user -> user.getFractions().stream())
                .toList();
        assertThat(allFractions)
                .anyMatch(f -> Math.abs(f.getNumerator()) < Math.abs(f.getDenominator()))
                .anyMatch(f -> Math.abs(f.getNumerator()) >= Math.abs(f.getDenominator()))
                .anyMatch(f -> f.decimal() < 0);
    }
}
