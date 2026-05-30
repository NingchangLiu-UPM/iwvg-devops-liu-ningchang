package es.upm.miw.devops;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void testFindHighestFraction() {
        Fraction result = new Searches().findHighestFraction();
        assertThat(result).isNotNull();
        assertThat(result.getNumerator()).isEqualTo(7);
        assertThat(result.getDenominator()).isEqualTo(2);
        assertThat(result.decimal()).isEqualTo(7.0 / 2.0);
    }

    @Test
    void testFindHighestFractionIsHigherThanAllOthers() {
        Fraction highest = new Searches().findHighestFraction();
        double highestDecimal = highest.decimal();
        assertThat(new Fraction(9, 5).decimal()).isLessThan(highestDecimal);
        assertThat(new Fraction(5, 3).decimal()).isLessThan(highestDecimal);
        assertThat(new Fraction(8, 3).decimal()).isLessThan(highestDecimal);
        assertThat(new Fraction(4, 4).decimal()).isLessThan(highestDecimal);
        assertThat(new Fraction(1, 3).decimal()).isLessThan(highestDecimal);
        assertThat(new Fraction(-7, 3).decimal()).isLessThan(highestDecimal);
    }

    @Test
    void testFindFirstDecimalFractionByUserNameForCarlosIsFirstUserInOrder() {
        Double result = new Searches().findFirstDecimalFractionByUserName("Carlos");
        assertThat(result)
                .isNotNull()
                .isEqualTo(7.0 / 2.0);
    }

    @Test
    void testFindFirstDecimalFractionByUserNameForSusana() {
        Double result = new Searches().findFirstDecimalFractionByUserName("Susana");
        assertThat(result)
                .isNotNull()
                .isEqualTo(1.0 / 3.0);
    }

    @Test
    void testFindFirstDecimalFractionByUserNameForMariaWithNegativeFraction() {
        Double result = new Searches().findFirstDecimalFractionByUserName("Maria");
        assertThat(result)
                .isNotNull()
                .isEqualTo(-0.5);
    }

    @Test
    void testFindFirstDecimalFractionByUserNameForLucia() {
        Double result = new Searches().findFirstDecimalFractionByUserName("Lucia");
        assertThat(result)
                .isNotNull()
                .isEqualTo(1.0);
    }

    @Test
    void testFindFirstDecimalFractionByUserNameReturnsNullForUnknownName() {
        Double result = new Searches().findFirstDecimalFractionByUserName("Unknown");
        assertThat(result).isNull();
    }

    @Test
    void testFindUserFamilyNameBySomeImproperFractionReturnsCorrectFamilyNamesInOrder() {
        var result = new Searches().findUserFamilyNameBySomeImproperFraction().toList();
        assertThat(result)
                .hasSize(4)
                .containsExactly("Garcia", "Gomez", "Lopez", "Perez");
    }

    @Test
    void testFindUserFamilyNameBySomeImproperFractionExcludesUsersWithOnlyProperFractions() {
        var result = new Searches().findUserFamilyNameBySomeImproperFraction().toList();
        assertThat(result).hasSize(4);
    }

    @Test
    void testFindUserFamilyNameBySomeImproperFractionFamilyNameBelongsToImproperUser() {
        var result = new Searches().findUserFamilyNameBySomeImproperFraction().toList();
        assertThat(result)
                .contains("Perez")
                .isSubsetOf(List.of("Garcia", "Gomez", "Lopez", "Perez"));
    }

    @Test
    void testFindUserFamilyNameBySomeImproperFractionAllBelongToUsersWithImproperFraction() {
        var result = new Searches().findUserFamilyNameBySomeImproperFraction().toList();
        for (String familyName : result) {
            boolean hasImproper = new UsersDatabase().findAll()
                    .filter(u -> u.getFamilyName().equals(familyName))
                    .anyMatch(u -> u.getFractions().stream().anyMatch(Fraction::isImproper));
            assertThat(hasImproper).isTrue();
        }
    }
}
