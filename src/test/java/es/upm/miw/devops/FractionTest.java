package es.upm.miw.devops;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FractionTest {

    @Test
    void testGetters() {
        Fraction fraction = new Fraction(3, 4);
        assertThat(fraction.getNumerator()).isEqualTo(3);
        assertThat(fraction.getDenominator()).isEqualTo(4);
    }

    @Test
    void testOneArgumentConstructor() {
        Fraction fraction = new Fraction(5);
        assertThat(fraction.getNumerator()).isEqualTo(5);
        assertThat(fraction.getDenominator()).isEqualTo(1);
    }

    @Test
    void testDecimal() {
        assertThat(new Fraction(1, 2).decimal()).isEqualTo(0.5);
        assertThat(new Fraction(3, 4).decimal()).isEqualTo(0.75);
        assertThat(new Fraction(5, 2).decimal()).isEqualTo(2.5);
    }

    @Test
    void testZeroDenominatorThrowsException() {
        assertThatThrownBy(() -> new Fraction(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Denominator must not be zero");
    }
}
