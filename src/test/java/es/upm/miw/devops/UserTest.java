package es.upm.miw.devops;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testIdentityGetters() {
        User user = new User("u1", "John", "Doe");
        assertThat(user.getId()).isEqualTo("u1");
        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getFamilyName()).isEqualTo("Doe");
    }

    @Test
    void testConstructorWithFractions() {
        List<Fraction> fractions = new ArrayList<>();
        fractions.add(new Fraction(1, 2));
        fractions.add(new Fraction(3, 4));
        User user = new User("u2", "Jane", "Smith", fractions);
        assertThat(user.getFractions()).hasSize(2);
        assertThat(user.getFractions().get(0).decimal()).isEqualTo(0.5);
        assertThat(user.getFractions().get(1).decimal()).isEqualTo(0.75);
    }

    @Test
    void testConstructorDefendsAgainstExternalCollectionMutation() {
        List<Fraction> fractions = new ArrayList<>();
        fractions.add(new Fraction(1, 2));
        User user = new User("u3", "Alice", "Brown", fractions);
        fractions.add(new Fraction(3, 4));
        assertThat(user.getFractions()).hasSize(1);
    }

    @Test
    void testGetFractionsReturnsUnmodifiableCollection() {
        List<Fraction> fractions = new ArrayList<>();
        fractions.add(new Fraction(1, 2));
        User user = new User("u4", "Bob", "Green", fractions);
        assertThat(user.getFractions()).isUnmodifiable();
    }

    @Test
    void testNoFractionsConstructorReturnsEmptyCollection() {
        User user = new User("u5", "Charlie", "White");
        assertThat(user.getFractions()).isEmpty();
    }
}
