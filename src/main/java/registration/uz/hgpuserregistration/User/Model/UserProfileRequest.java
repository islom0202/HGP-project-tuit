package registration.uz.hgpuserregistration.User.Model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import registration.uz.hgpuserregistration.User.Entity.Gender;

@Data
@Getter
@Setter
public class UserProfileRequest {
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    @Size(min = 7)
    private String email;
    @NotEmpty
    private String address;
    @NotEmpty
    @Size(min = 6)
    private String password;
    @NotEmpty
    @Size(min = 9)
    private String passportSerialNumber;
    @NotEmpty
    @Size(min = 12, max = 15)
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String imageUrl;

    public UserProfileRequest(String firstname,
                              String lastname,
                              String email,
                              String address,
                              String passportSerialNumber,
                              String phone,
                              Gender gender,
                              String imageUrl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.passportSerialNumber = passportSerialNumber;
        this.phone = phone;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }
}
