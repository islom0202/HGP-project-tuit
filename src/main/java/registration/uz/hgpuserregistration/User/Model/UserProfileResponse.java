package registration.uz.hgpuserregistration.User.Model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import registration.uz.hgpuserregistration.User.Entity.Gender;

@Setter
@Getter
public class UserProfileResponse {
    private String id;
    private String firstname;
    private String lastname;

    private String email;

    private String address;

    private String passportSerialNumber;

    private String phone;

    private Gender gender;
    private String imageUrl;

    public UserProfileResponse(
            String id,
            String firstname,
                              String lastname,
                              String email,
                              String address,
                              String passportSerialNumber,
                              String phone,
                              Gender gender,
                              String imageUrl) {
        this.id = id;
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
