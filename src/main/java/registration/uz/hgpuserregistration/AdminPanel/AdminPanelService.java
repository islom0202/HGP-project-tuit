package registration.uz.hgpuserregistration.AdminPanel;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import registration.uz.hgpuserregistration.DetectorData.DetectorData;
import registration.uz.hgpuserregistration.DetectorData.DetectorRepository;
import registration.uz.hgpuserregistration.User.Entity.Gender;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Model.UserProfileResponseDto;
import registration.uz.hgpuserregistration.User.Model.UserStatistics;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminPanelService {

    private final UserProfileRepository profileRepository;
    private final DetectorRepository detectorRepository;
    private final UserSearchingRepository searchingRepository;
    private final AdminTableRepo adminTableRepo;
    private final PasswordEncoder passwordEncoder;

    public List<UserProfileResponseDto> getUserList() {
        List<UserProfile> userProfiles = profileRepository.findAll();
        return getUserProfileResponseDtos(userProfiles);
    }

    public UserStatistics getUserStatistics() {
        UserStatistics statistic = new UserStatistics();
        int allUsers = profileRepository.countAllUsers();
        int enabled = profileRepository.countEnabledUsers();
        int disabled = allUsers - enabled;
        statistic.setAllUsers(allUsers);
        statistic.setEnabled(enabled);
        statistic.setDisabled(disabled);
        return statistic;
    }

    public List<UserProfileResponseDto> findBySearching(
            String firstname,
            String lastname,
            String email,
            Gender gender,
            String enabled
    ){
        List<UserProfile> userProfiles = searchingRepository.findBySearchTerm(
                firstname,
                lastname,
                email,
                gender,
                enabled
        );
        return getUserProfileResponseDtos(userProfiles);
    }

    private List<UserProfileResponseDto> getUserProfileResponseDtos(List<UserProfile> userProfiles) {
        List<UserProfileResponseDto> userProfileResponseDtos = new ArrayList<>();
        for (UserProfile userProfile : userProfiles) {
            UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
            String detectorId = detectorRepository.getDetectorId(userProfile.getId());
            userProfileResponseDto.setId(userProfile.getId());
            userProfileResponseDto.setFirstname(userProfile.getFirstname());
            userProfileResponseDto.setLastname(userProfile.getLastname());
            userProfileResponseDto.setEmail(userProfile.getEmail());
            userProfileResponseDto.setAddress(userProfile.getAddress());
            userProfileResponseDto.setPhone(userProfile.getPhone());
            userProfileResponseDto.setGender(userProfile.getGender());
            userProfileResponseDto.setEnabled(userProfile.getEnabled());
            userProfileResponseDto.setDeviceId(detectorId);
            userProfileResponseDtos.add(userProfileResponseDto);
        }
        userProfileResponseDtos.sort(Comparator.comparing(UserProfileResponseDto::getId));
        return userProfileResponseDtos;
    }

    public AdminTable save(AdminRequest request) {
        AdminTable adminTable = new AdminTable();
        adminTable.setUsername(request.getLogin());
        adminTable.setPassword(passwordEncoder.encode(request.getPassword()));
        adminTable.setRole("ROLE_ADMIN");
        return adminTableRepo.save(adminTable);
    }
}
