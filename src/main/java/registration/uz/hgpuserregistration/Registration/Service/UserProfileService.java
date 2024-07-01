package registration.uz.hgpuserregistration.Registration.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import registration.uz.hgpuserregistration.Categories.Entity.Category;
import registration.uz.hgpuserregistration.Categories.Repository.CategoryRepository;
import registration.uz.hgpuserregistration.Registration.Entity.Gender;
import registration.uz.hgpuserregistration.Registration.Entity.UserImage;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;
import registration.uz.hgpuserregistration.Registration.Entity.UserRole;
import registration.uz.hgpuserregistration.Registration.Model.UserProfileRequest;
import registration.uz.hgpuserregistration.Registration.Respository.UserImageRepo;
import registration.uz.hgpuserregistration.Registration.Respository.UserProfileRepository;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserImageRepo userImageRepo;

    public boolean existsUser(UserProfileRequest request) {
        return userProfileRepository.existsByPhone(request.getPhone());
    }

    @Transactional
    public UserProfile save(UserProfileRequest request) throws IOException {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstname(request.getFirstname());
        userProfile.setLastname(request.getLastname());
        userProfile.setLocked(false);
        userProfile.setEnabled(false);
        userProfile.setAddress(request.getAddress());
        userProfile.setPassword(passwordEncoder.encode(request.getPassword()));
        userProfile.setPassportSerialNumber(request.getPassportSerialNumber());
        if (request.getGender() == null) {
            userProfile.setGender(Gender.MALE);
        }
        userProfile.setGender(request.getGender());
        userProfile.setRole(UserRole.ROLE_USER);

        if (isValidPhoneNumber(request.getPhone()) && isValidEmail(request.getEmail())) {
            userProfile.setPhone(request.getPhone());
            userProfile.setEmail(request.getEmail());
            userProfileRepository.save(userProfile);
        } else
            throw new IllegalArgumentException("Invalid phone or email");
        return userProfile;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\+?[0-9]{12,}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public UserProfileRequest getUserProfile(String username) {
        UserProfile user = userProfileRepository.findByLogin(username);
        return new UserProfileRequest(user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getAddress(),
                user.getPassword(),
                user.getPassportSerialNumber(),
                user.getPhone(),
                user.getGender());
    }

    public void updatedUser(UserProfile user) {
        userProfileRepository.save(user);
    }

    public void setImage(UserDetails userDetails, MultipartFile file) throws IOException {
        UserProfile userProfile = userProfileRepository.findByLogin(userDetails.getUsername());
        UserImage userImage = new UserImage();
            userImage.setImage(file.getBytes());
            userImage.setUserProfile(userProfile);
            userProfile.setUserImage(userImage);
            userImageRepo.save(userImage);
    }

    public UserProfile getByPassportSerialNumber(String passportSerialNumber) {
        return userProfileRepository.findByPassportSerialNumber(passportSerialNumber);
    }
}
