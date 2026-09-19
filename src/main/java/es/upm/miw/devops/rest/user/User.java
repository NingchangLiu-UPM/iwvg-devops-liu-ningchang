package es.upm.miw.devops.rest.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Column(unique = true)
    private String mobile;

    private String firstName;

    private String familyName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String identity;

    private String address;

    private String city;

    @Enumerated(EnumType.STRING)
    private Province province;

    private Integer postalCode;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate registrationDate;

    private Boolean active;

    public User() {
        // JPA
    }

    public User(UUID id, String mobile, String firstName, String familyName, String email,
                String identity, String address, String city, Province province, Integer postalCode,
                String password, Role role, LocalDate registrationDate, Boolean active) {
        this.id = id;
        this.mobile = mobile;
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.identity = identity;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.password = password;
        this.role = role;
        this.registrationDate = registrationDate;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", firstName='" + firstName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", email='" + email + '\'' +
                ", identity='" + identity + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", province=" + province +
                ", postalCode=" + postalCode +
                ", role=" + role +
                ", registrationDate=" + registrationDate +
                ", active=" + active +
                '}';
    }
}
