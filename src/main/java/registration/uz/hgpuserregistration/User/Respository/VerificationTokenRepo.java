package registration.uz.hgpuserregistration.User.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.User.Entity.VerificationToken;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    void deleteByUser_Id(Long id);
}
