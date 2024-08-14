package registration.uz.hgpuserregistration.AdminPanel;

import lombok.Getter;
import lombok.Setter;
import registration.uz.hgpuserregistration.User.Entity.UserRole;

@Setter
@Getter
public class AdminToken {
    private String token;
    private String role;

    public AdminToken(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
