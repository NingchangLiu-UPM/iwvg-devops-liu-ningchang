package es.upm.miw.devops.rest.user;

import java.time.LocalDate;
import java.util.UUID;

public class UserDto {

    private UUID id;
    private String mobile;
    private String firstName;
    private String familyName;
    private String email;
    private String identity;
    private String address;
    private String city;
    private Province province;
    private Integer postalCode;
    private Role role;
    private LocalDate registrationDate;
    private Boolean active;

    public UserDto() {
        // empty
    }

    public static UserDto of(User user) {
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.mobile = user.getMobile();
        dto.firstName = user.getFirstName();
        dto.familyName = user.getFamilyName();
        dto.email = user.getEmail();
        dto.identity = user.getIdentity();
        dto.address = user.getAddress();
        dto.city = user.getCity();
        dto.province = user.getProvince();
        dto.postalCode = user.getPostalCode();
        dto.role = user.getRole();
        dto.registrationDate = user.getRegistrationDate();
        dto.active = user.getActive();
        return dto;
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
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
