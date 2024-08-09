package registration.uz.hgpuserregistration.User.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.webjars.NotFoundException;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;
import registration.uz.hgpuserregistration.Order.OrderRepository;
import registration.uz.hgpuserregistration.User.Entity.Gender;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Entity.UserRole;
import registration.uz.hgpuserregistration.User.Model.*;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;
import registration.uz.hgpuserregistration.User.Respository.VerificationTokenRepo;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;
    private final VerificationTokenRepo verificationTokenRepo;

    public boolean existsUser(UserProfileRequest request) {
        return userProfileRepository.existsByPhone(request.getPhone());
    }

    @Transactional
    public UserProfile save(UserProfileRequest request) {
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstname(request.getFirstname());
        userProfile.setLastname(request.getLastname());
        userProfile.setLocked(false);
        userProfile.setEnabled(false);
        userProfile.setAccessStatus(false);
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

    public UserProfileResponse getUserProfile(String username) {
        UserProfile user = userProfileRepository.findByLogin(username);
        String userId = String.valueOf(user.getId());
        if(user.getImage() != null) {
            String imageUrl = ServletUriComponentsBuilder.fromHttpUrl("https://amused-bison-equipped.ngrok-free.app/api/user/image/")
                    .path(user.getId().toString())
                    .toUriString();

            return new UserProfileResponse(
                    userId,
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getPassportSerialNumber(),
                    user.getPhone(),
                    user.getGender(),
                    imageUrl);
        }
        else
            return new UserProfileResponse(
                    userId,
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getPassportSerialNumber(),
                    user.getPhone(),
                    user.getGender(),
                    null);
    }

    public void updatedUser(UserProfile user) {
        userProfileRepository.save(user);
    }

    public UserProfile getByPassportSerialNumber(String passportSerialNumber) {
        return userProfileRepository.findByPassportSerialNumber(passportSerialNumber);
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.deleteByUserProfile_Id(id);
        verificationTokenRepo.deleteByUser_Id(id);
        deleteUser(id);
    }


    private void deleteUser(Long id) {
        userProfileRepository.deleteById(id);
    }

    public UserProfileResponse editUserDetails(EditUserDetailsDTO request) throws UserProfileNotFoundException {
        Long id = Long.parseLong(request.getId());
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new UserProfileNotFoundException("User not found!"));

        if (!request.getEmail().isEmpty()) {
            userProfile.setEmail(request.getEmail());
        }
        if (!request.getPhone().isEmpty()) {
            userProfile.setPhone(request.getPhone());
        }
        if (!request.getAddress().isEmpty()) {
            userProfile.setAddress(request.getAddress());
        }
        userProfileRepository.save(userProfile);
        return getUserProfile(userProfile.getLogin());
    }

    public UserProfile findByLogin(String login) {
        return userProfileRepository.findByLogin(login);
    }

    public Optional<UserProfile> findById(Long id) {
        return userProfileRepository.findById(id);
    }

    public void uploadImage(Long userId, MultipartFile file) throws AlreadyBoundException, IOException {
        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);

        if (userProfile.isPresent()) {
            UserProfile profile = userProfile.get();
            profile.setImage(file.getBytes());
            userProfileRepository.save(profile);
        } else
            throw new AlreadyBoundException("image is already in use");
    }

    public void resetPass(ResetPass newPass) {
        Optional<UserProfile> userProfile = userProfileRepository.findByEmail(newPass.getEmail());
        if (userProfile.isPresent()){
            UserProfile profile = userProfile.get();
            profile.setPassword(passwordEncoder.encode(newPass.getNewPass()));
            userProfileRepository.save(profile);
        }
    }
}
