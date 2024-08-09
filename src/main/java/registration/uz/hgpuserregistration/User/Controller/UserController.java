package registration.uz.hgpuserregistration.User.Controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import registration.uz.hgpuserregistration.Email.EmailService;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.JWT.TokenProvider.JwtToken;
import registration.uz.hgpuserregistration.JWT.TokenProvider.JwtTokenProvider;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Entity.VerificationToken;
import registration.uz.hgpuserregistration.User.Model.*;
import registration.uz.hgpuserregistration.User.Respository.VerificationTokenRepo;
import registration.uz.hgpuserregistration.User.Service.UserProfileService;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationTokenRepo verificationTokenRepo;
    private final UserProfileService userProfileService;
    private final JwtTokenProvider jwtTokenProvider;

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
            String verificationUrl = " https://amused-bison-equipped.ngrok-free.app/api/verify?token=" + token;
            emailService.sendEmail(request.getEmail(), "Email Verification", "Email Verification\n" + "Please verify your email using the following link: " + verificationUrl);

            return ResponseEntity.ok(userProfile.getId() + " User Registered successful. Please check your email for verification instructions.");
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                .body("already registered!");
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> setImage(@RequestParam("userId") String userId,
                                           @RequestParam("file") MultipartFile file) {
        Long id = Long.parseLong(userId);

        try {
            userProfileService.uploadImage(id, file);
            return ResponseEntity.status(HttpStatus.OK).body("image uploaded!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving image: " + e.getMessage());
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
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

            UserProfile userProfile = userProfileService.findByLogin(request.getLogin());
            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body(new JwtToken(token, userProfile.getAccessStatus()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid login or password.");
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
    public ResponseEntity<UserProfileResponse> getProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userProfileService.getUserProfile(userDetails.getUsername()));
    }

    @GetMapping("/user/image/{id}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String id) {
        Long userId = Long.parseLong(id);
        Optional<UserProfile> userProfile = userProfileService.findById(userId);
        if (userProfile.isPresent() && userProfile.get().getImage() != null) {
            byte[] image = userProfile.get().getImage();
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(image);
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.jpg\"")
//                    .body(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        userProfileService.delete(id);
        return ResponseEntity.ok(id + " User deleted successfully.");
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<String> resetPass(@RequestBody ResetPass newPass) {
        try {
            userProfileService.resetPass(newPass);
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("Password reset successfully.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<UserProfileResponse> editUser(@RequestBody EditUserDetailsDTO request) throws UserProfileNotFoundException {
        return ResponseEntity.ok(userProfileService.editUserDetails(request));
    }
}
