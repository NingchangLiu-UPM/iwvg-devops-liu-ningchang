package es.upm.miw.devops;

import java.util.Collections;
import java.util.List;

public class User {

    private final String id;
    private final String name;
    private final String familyName;
    private final List<Fraction> fractions;

    public User(String id, String name, String familyName, List<Fraction> fractions) {
        this.id = id;
        this.name = name;
        this.familyName = familyName;
        this.fractions = List.copyOf(fractions);
    }

    public User(String id, String name, String familyName) {
        this(id, name, familyName, List.of());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public List<Fraction> getFractions() {
        return fractions;
    }
}
