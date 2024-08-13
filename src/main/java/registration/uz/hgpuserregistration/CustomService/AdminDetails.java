package registration.uz.hgpuserregistration.CustomService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import registration.uz.hgpuserregistration.AdminPanel.AdminTable;

import java.util.Collection;
import java.util.Collections;

public class AdminDetails implements UserDetails {

    private final AdminTable adminTable;

    public AdminDetails(AdminTable adminTable) {
        this.adminTable = adminTable;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
                adminTable.getRole()
        );
        return Collections.singleton(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return adminTable.getPassword();
    }

    @Override
    public String getUsername() {
        return adminTable.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
