package registration.uz.hgpuserregistration.User.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import registration.uz.hgpuserregistration.AdminPanel.AdminToken;
import registration.uz.hgpuserregistration.JWT.TokenProvider.JwtToken;
import registration.uz.hgpuserregistration.JWT.TokenProvider.JwtTokenProvider;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Model.LoginRequest;
import registration.uz.hgpuserregistration.User.Service.UserProfileService;

@RestController
@RequestMapping("/api/login")
@AllArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getLogin() == null || request.getPassword() == null || request.getLogin().isEmpty() || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Login and password are required.");
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    request.getLogin(),
                    request.getPassword()
            );
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            String token = jwtTokenProvider.createToken(authentication);

            UserProfile userProfile = userProfileService.findByLogin(request.getLogin());
            if (userProfile != null) {
                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + token)
                        .body(new JwtToken(token, userProfile.getAccessStatus()));
            } else
                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + token)
                        .body(new AdminToken(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid login or password.");
        }
    }
}
