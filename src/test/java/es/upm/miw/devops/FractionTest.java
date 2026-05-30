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

    @Test
    void testIsProperPositiveFraction() {
        assertThat(new Fraction(1, 2).isProper()).isTrue();
        assertThat(new Fraction(3, 4).isProper()).isTrue();
    }

    @Test
    void testIsProperNegativeFraction() {
        assertThat(new Fraction(-1, 3).isProper()).isTrue();
        assertThat(new Fraction(1, -3).isProper()).isTrue();
    }

    @Test
    void testIsImproperPositiveFraction() {
        assertThat(new Fraction(3, 2).isImproper()).isTrue();
        assertThat(new Fraction(5, 4).isImproper()).isTrue();
    }

    @Test
    void testIsImproperWholeNumber() {
        assertThat(new Fraction(4, 4).isImproper()).isTrue();
        assertThat(new Fraction(5, 1).isImproper()).isTrue();
    }

    @Test
    void testIsEquivalent() {
        assertThat(new Fraction(1, 2).isEquivalent(new Fraction(2, 4))).isTrue();
        assertThat(new Fraction(3, 6).isEquivalent(new Fraction(1, 2))).isTrue();
    }

    @Test
    void testIsNotEquivalent() {
        assertThat(new Fraction(1, 2).isEquivalent(new Fraction(2, 3))).isFalse();
        assertThat(new Fraction(3, 4).isEquivalent(new Fraction(2, 5))).isFalse();
    }

    @Test
    void testIsEquivalentWithNegativeDenominators() {
        assertThat(new Fraction(1, 2).isEquivalent(new Fraction(-1, -2))).isTrue();
        assertThat(new Fraction(-1, 2).isEquivalent(new Fraction(1, -2))).isTrue();
    }

    @Test
    void testAdd() {
        assertThat(new Fraction(1, 2).add(new Fraction(1, 3)).decimal())
                .isEqualTo(5.0 / 6);
        assertThat(new Fraction(1, 2).add(new Fraction(1, 2)).getNumerator())
                .isEqualTo(4);
        assertThat(new Fraction(1, 2).add(new Fraction(1, 2)).getDenominator())
                .isEqualTo(4);
    }

    @Test
    void testMultiply() {
        assertThat(new Fraction(2, 3).multiply(new Fraction(3, 4)).getNumerator())
                .isEqualTo(6);
        assertThat(new Fraction(2, 3).multiply(new Fraction(3, 4)).getDenominator())
                .isEqualTo(12);
        assertThat(new Fraction(2, 3).multiply(new Fraction(3, 4)).decimal())
                .isEqualTo(0.5);
    }

    @Test
    void testDivide() {
        assertThat(new Fraction(1, 2).divide(new Fraction(1, 4)).getNumerator())
                .isEqualTo(4);
        assertThat(new Fraction(1, 2).divide(new Fraction(1, 4)).getDenominator())
                .isEqualTo(2);
        assertThat(new Fraction(1, 2).divide(new Fraction(1, 4)).decimal())
                .isEqualTo(2.0);
    }

    @Test
    void testDivideByZeroNumeratorThrowsException() {
        Fraction dividend = new Fraction(1, 2);
        Fraction divisor = new Fraction(0, 3);
        assertThatThrownBy(() -> dividend.divide(divisor))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Divisor numerator must not be zero");
    }
}
