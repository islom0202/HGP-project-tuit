package registration.uz.hgpuserregistration.AdminPanel;

import lombok.Data;

@Data
public class AdminRequest {
    private String username;
    private String password;
    private String role;
}
