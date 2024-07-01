package registration.uz.hgpuserregistration.Registration.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.UserImage;

@Repository
public interface UserImageRepo extends JpaRepository<UserImage, Long> {
}
