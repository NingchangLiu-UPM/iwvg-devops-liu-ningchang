package es.upm.miw.devops.rest.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
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
