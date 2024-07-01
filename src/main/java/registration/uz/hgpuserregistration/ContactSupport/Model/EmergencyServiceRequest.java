package registration.uz.hgpuserregistration.ContactSupport.Model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class EmergencyServiceRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String location;
    @NotEmpty
    private String phone;
}
