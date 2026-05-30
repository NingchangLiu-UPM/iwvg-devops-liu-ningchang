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

    public boolean isProper() {
        return Math.abs(numerator) < Math.abs(denominator);
    }

    public boolean isImproper() {
        return Math.abs(numerator) >= Math.abs(denominator);
    }

    public boolean isEquivalent(Fraction fraction) {
        return (long) numerator * fraction.denominator
                == (long) fraction.numerator * denominator;
    }

    public Fraction add(Fraction fraction) {
        return new Fraction(
                numerator * fraction.denominator + fraction.numerator * denominator,
                denominator * fraction.denominator);
    }

    public Fraction multiply(Fraction fraction) {
        return new Fraction(numerator * fraction.numerator, denominator * fraction.denominator);
    }

    public Fraction divide(Fraction fraction) {
        if (fraction.numerator == 0) {
            throw new IllegalArgumentException("Divisor numerator must not be zero");
        }
        return new Fraction(numerator * fraction.denominator, denominator * fraction.numerator);
    }
}
