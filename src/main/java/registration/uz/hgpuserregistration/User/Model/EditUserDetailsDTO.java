package registration.uz.hgpuserregistration.User.Model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class EditUserDetailsDTO {
    private String id;
    private String email;
    private String address;
    @Size(min = 12)
    private String phone;
}
