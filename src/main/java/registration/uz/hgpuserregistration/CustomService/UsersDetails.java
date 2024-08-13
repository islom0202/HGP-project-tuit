package registration.uz.hgpuserregistration.CustomService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

import java.util.Collection;
import java.util.Collections;

public class UsersDetails implements UserDetails {

    private final UserProfile userProfile;

    public UsersDetails(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
               userProfile.getRole().name()
        );
        return Collections.singleton(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return userProfile.getPassword();
    }

    @Override
    public String getUsername() {
        return userProfile.getLogin();
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
        return userProfile.getEnabled();
    }
}
