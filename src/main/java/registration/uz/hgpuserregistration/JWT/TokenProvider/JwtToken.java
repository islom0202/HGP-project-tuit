package registration.uz.hgpuserregistration.JWT.TokenProvider;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtToken {
    private String token;
    private Boolean accessStatus;
    public JwtToken(String token, Boolean accessStatus) {
        this.token = token;
        this.accessStatus = accessStatus;
    }
}
