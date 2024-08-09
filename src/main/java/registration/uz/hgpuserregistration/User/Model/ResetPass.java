package registration.uz.hgpuserregistration.User.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResetPass {
    @NotEmpty
    @Size(min = 6)
    private String newPass;
    private String email;
}
