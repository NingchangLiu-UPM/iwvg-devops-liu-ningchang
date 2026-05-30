package es.upm.miw.devops;

import java.util.Optional;

public class Searches {

    private final UsersDatabase usersDatabase;

    public Searches() {
        this.usersDatabase = new UsersDatabase();
    }

    Searches(UsersDatabase usersDatabase) {
        this.usersDatabase = usersDatabase;
    }

    public Fraction findFractionSubtractionByUserName(String name) {
        Optional<User> user = usersDatabase.findAll()
                .filter(u -> u.getName().equals(name))
                .findFirst();
        if (user.isEmpty() || user.get().getFractions().isEmpty()) {
            return null;
        }
        return user.get().getFractions().stream()
                .reduce(subtract())
                .orElse(null);
    }

    private java.util.function.BinaryOperator<Fraction> subtract() {
        return this::subtract;
    }

    private Fraction subtract(Fraction minuend, Fraction subtrahend) {
        int resultNumerator = minuend.getNumerator() * subtrahend.getDenominator()
                - subtrahend.getNumerator() * minuend.getDenominator();
        int resultDenominator = minuend.getDenominator() * subtrahend.getDenominator();
        if (resultDenominator < 0) {
            resultNumerator = -resultNumerator;
            resultDenominator = -resultDenominator;
        }
        return new Fraction(resultNumerator, resultDenominator);
    }
}
