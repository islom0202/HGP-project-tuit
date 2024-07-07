package registration.uz.hgpuserregistration.Registration.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Registration.Entity.VerificationToken;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    @Query(value = "delete from VerificationToken where user.id=:id", nativeQuery = true)
    void deleteTok(@Param("id") Long id);

    void deleteByUser_Id(Long id);
}
