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
    private String title;
    private String email;
    private String message;
}
