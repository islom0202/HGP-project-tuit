package registration.uz.hgpuserregistration.User.Model;

import lombok.Data;
import registration.uz.hgpuserregistration.User.Entity.Gender;

@Data
public class UserProfileResponseDto {
    private String firstname;
    private String lastname;
    private String email;
    private String address;
    private String phone;
    private Gender gender;
    private boolean enabled;
    private String deviceId;
    private String imageUrl;
}
