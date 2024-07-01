package registration.uz.hgpuserregistration.Order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class OrderRequest {
    @NotEmpty
    @Size(min = 12)
    private String userPhone;
    @NotEmpty
    private String orderAddress;
    @NotEmpty
    private String passportSerialNumber;
}
