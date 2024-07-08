package registration.uz.hgpuserregistration.Registration.Controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import registration.uz.hgpuserregistration.Email.EmailService;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.JWT.TokenProvider.JwtToken;
import registration.uz.hgpuserregistration.JWT.TokenProvider.JwtTokenProvider;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;
import registration.uz.hgpuserregistration.Registration.Entity.VerificationToken;
import registration.uz.hgpuserregistration.Registration.Model.EditUserDetailsDTO;
import registration.uz.hgpuserregistration.Registration.Model.LoginRequest;
import registration.uz.hgpuserregistration.Registration.Model.UserProfileRequest;
import registration.uz.hgpuserregistration.Registration.Respository.VerificationTokenRepo;
import registration.uz.hgpuserregistration.Registration.Service.UserProfileService;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    private final VerificationTokenRepo verificationTokenRepo;
    private final UserProfileService userProfileService;
    private final JwtTokenProvider jwtTokenProvider;

    public RegistrationController(AuthenticationManager authenticationManager, EmailService emailService, VerificationTokenRepo verificationTokenRepo, UserProfileService userProfileService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.verificationTokenRepo = verificationTokenRepo;
        this.userProfileService = userProfileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserProfileRequest request) {
        if (!userProfileService.existsUser(request)) {

            UserProfile userProfile = userProfileService.save(request);
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setGeneratedDate(new Date());
            verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
            verificationToken.setUser(userProfile);
            verificationTokenRepo.save(verificationToken);
            String verificationUrl = "https://315b-195-158-2-216.ngrok-free.app/api/verify?token=" + token;
            emailService.sendEmail(request.getEmail(), "Email Verification", "Email Verification\n" + "Please verify your email using the following link: " + verificationUrl);

            return ResponseEntity.ok(userProfile.getId() + " User Registered successful. Please check your email for verification instructions.");
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                .body("already registered!");
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userProfileService.setImage(userDetails, file);
        return ResponseEntity.ok("Image uploaded");
    }


    @PostMapping("/login")
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

            Boolean accessStatus = userProfileService.getAccessStatus(request.getLogin());
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body(new JwtToken(token, accessStatus));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login or password.");
        }
    }

    @Transactional
    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        VerificationToken verificationToken = verificationTokenRepo.findByToken(token);

        if (verificationToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
        }

        UserProfile user = verificationToken.getUser();
        user.setEnabled(true);
        userProfileService.updatedUser(user);

        verificationTokenRepo.delete(verificationToken);

        return ResponseEntity.ok("Email verified successfully.");
    }

    @GetMapping("/profile")
    public UserProfileRequest getProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userProfileService.getUserProfile(userDetails.getUsername());
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        userProfileService.delete(id);
        return ResponseEntity.ok(id + " User deleted successfully.");
    }

    @PutMapping("/edit/{id}")
    public UserProfile editUser(@PathVariable("id") Long id,
                                           @RequestBody EditUserDetailsDTO request) throws UserProfileNotFoundException {
        return userProfileService.editUserDetails(id, request);
    }
}
