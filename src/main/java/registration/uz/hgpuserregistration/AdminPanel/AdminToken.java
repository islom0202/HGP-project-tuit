package registration.uz.hgpuserregistration.AdminPanel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminToken {
    private String token;

    public AdminToken(String token) {
        this.token = token;
    }
}
