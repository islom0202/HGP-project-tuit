package registration.uz.hgpuserregistration.Registration.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.VerificationToken;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    void deleteByUser_Id(Long id);
}
