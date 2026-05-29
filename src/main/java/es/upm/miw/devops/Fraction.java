package es.upm.miw.devops;

public class Fraction {

    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator must not be zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction(int numerator) {
        this(numerator, 1);
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public double decimal() {
        return (double) numerator / denominator;
    }
}
