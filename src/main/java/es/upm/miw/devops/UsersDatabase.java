package es.upm.miw.devops;

import java.util.List;
import java.util.stream.Stream;

public class UsersDatabase {

    public Stream<User> findAll() {
        return Stream.of(
                new User("1", "Susana", "Perez", List.of(
                        new Fraction(1, 3),
                        new Fraction(2, 5),
                        new Fraction(3, 8))),
                new User("2", "Carlos", "Garcia", List.of(
                        new Fraction(7, 2),
                        new Fraction(1, 4),
                        new Fraction(5, 3))),
                new User("3", "Carlos", "Gomez", List.of(
                        new Fraction(9, 5),
                        new Fraction(2, 7))),
                new User("4", "Maria", "Lopez", List.of(
                        new Fraction(-1, 2),
                        new Fraction(-7, 3),
                        new Fraction(3, -5))),
                new User("5", "Lucia", "Perez", List.of(
                        new Fraction(4, 4),
                        new Fraction(1, 6),
                        new Fraction(8, 3))));
    }
}
