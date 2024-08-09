package registration.uz.hgpuserregistration.CustomService;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import registration.uz.hgpuserregistration.AdminPanel.AdminTable;
import registration.uz.hgpuserregistration.AdminPanel.AdminTableRepo;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;

import java.util.Collection;
import java.util.Collections;

@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final UserProfile userProfile = userProfileRepository.findByLogin(login);

        return new UserDetails() {
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
        };
    }
}
