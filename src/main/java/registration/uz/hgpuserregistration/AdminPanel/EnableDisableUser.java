package registration.uz.hgpuserregistration.AdminPanel;

import lombok.Data;

@Data
public class EnableDisableUser {
    private Long userId;
    private boolean enabled;
}
