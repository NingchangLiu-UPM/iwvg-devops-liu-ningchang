package es.upm.miw.devops;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SearchesTest {

    @Test
    void testFindFractionSubtractionByUserNameForSusana() {
        Fraction result = new Searches().findFractionSubtractionByUserName("Susana");
        assertThat(result).isNotNull();
        assertThat(result.getNumerator()).isEqualTo(-53);
        assertThat(result.getDenominator()).isEqualTo(120);
    }

    @Test
    void testFindFractionSubtractionByUserNameForFirstCarlos() {
        Fraction result = new Searches().findFractionSubtractionByUserName("Carlos");
        assertThat(result).isNotNull();
        assertThat(result.getNumerator()).isEqualTo(38);
        assertThat(result.getDenominator()).isEqualTo(24);
    }

    @Test
    void testFindFractionSubtractionByUserNameForMaria() {
        Fraction result = new Searches().findFractionSubtractionByUserName("Maria");
        assertThat(result).isNotNull();
        assertThat(result.getNumerator()).isEqualTo(73);
        assertThat(result.getDenominator()).isEqualTo(30);
    }

    @Test
    void testFindFractionSubtractionByUserNameForLucia() {
        Fraction result = new Searches().findFractionSubtractionByUserName("Lucia");
        assertThat(result).isNotNull();
        assertThat(result.getNumerator()).isEqualTo(-132);
        assertThat(result.getDenominator()).isEqualTo(72);
    }

    @Test
    void testFindFractionSubtractionByUserNameReturnsNullForUnknownName() {
        Fraction result = new Searches().findFractionSubtractionByUserName("Unknown");
        assertThat(result).isNull();
    }
}
