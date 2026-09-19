package es.upm.miw.devops.rest.user;

import java.time.LocalDate;

public class UserDto {

    private String id;
    private String mobile;
    private String firstName;
    private String familyName;
    private String email;
    private String identity;
    private String address;
    private String city;
    private String province;
    private Integer postalCode;
    private String role;
    private LocalDate registrationDate;
    private Boolean active;

    public UserDto() {
        // for serialization
    }

    public static UserDto of(User user) {
        UserDto dto = new UserDto();
        dto.id = user.getId() != null ? user.getId().toString() : null;
        dto.mobile = user.getMobile();
        dto.firstName = user.getFirstName();
        dto.familyName = user.getFamilyName();
        dto.email = user.getEmail();
        dto.identity = user.getIdentity();
        dto.address = user.getAddress();
        dto.city = user.getCity();
        dto.province = user.getProvince() != null ? user.getProvince().name() : null;
        dto.postalCode = user.getPostalCode();
        dto.role = user.getRole() != null ? user.getRole().name() : null;
        dto.registrationDate = user.getRegistrationDate();
        dto.active = user.getActive();
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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
}
