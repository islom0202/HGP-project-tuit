package registration.uz.hgpuserregistration.Registration.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.UserProfile;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByLogin(String login);
    boolean existsByPhone(String phone);

    @Query(value = "select u.enabled from UserProfile u where u.login=:login", nativeQuery = true)
    Boolean checkEnabledByLogin(@Param("login") String login);

    UserProfile findByPassportSerialNumber(String passportSerialNumber);

    Optional<UserProfile> findByDetectorData_DetectorId(String detectorId);
}
