package registration.uz.hgpuserregistration.ContactSupport.Model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class MessageDto {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String message;
    private String email;
}
