package registration.uz.hgpuserregistration.ContactSupport.Model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExpertsDto {
    @NotEmpty
    private String fullName;
    @NotEmpty
    private String email;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String jobTitle;
    private String imageUrl;
}
