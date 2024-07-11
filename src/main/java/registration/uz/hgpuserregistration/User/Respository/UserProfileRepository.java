package registration.uz.hgpuserregistration.User.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByLogin(String login);
    boolean existsByPhone(String phone);

    UserProfile findByPassportSerialNumber(String passportSerialNumber);

    Optional<UserProfile> findByDetectorData_DetectorId(String detectorId);
}
