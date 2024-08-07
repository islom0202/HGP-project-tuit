package registration.uz.hgpuserregistration.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import registration.uz.hgpuserregistration.JWT.JwtConfig;
import registration.uz.hgpuserregistration.JWT.TokenProvider.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtTokenProvider jwtTokenProvider;
    private final String[] SWAGGER_URLS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(SWAGGER_URLS).permitAll()
                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/detector/send").permitAll()
                        .requestMatchers("/websocket-connection/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/order").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/verify").permitAll()
                        .requestMatchers("/api/reset-pass/**").permitAll()
                        //delete has to be for admin only
                        .requestMatchers("/api/delete/**").permitAll()
                        .requestMatchers("/api/profile", "/api/uploadImage").hasRole("USER")
                        .requestMatchers("/api/contact/send/message").hasRole("USER")
                        .requestMatchers("/api/emergency/add", "/api/list").hasRole("ADMIN")
                        .requestMatchers("/api/experts/get", "/api/experts/image/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/experts/add/details").hasRole("ADMIN")
                        .requestMatchers("/api/contact/get/messages", "/api/list").hasRole("ADMIN")
                        .requestMatchers("/api/user/image/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/admin/user-list",
                                "/api/admin/user-statis",
                                "/api/admin/searching",
                                "/api/done").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        JwtConfig jwtConfig = new JwtConfig(jwtTokenProvider);
        jwtConfig.configure(http);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}