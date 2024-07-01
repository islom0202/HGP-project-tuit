package registration.uz.hgpuserregistration.ContactSupport.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.ContactSupport.Entity.Experts;

@Repository
public interface ExpertsRepository extends JpaRepository<Experts, Long> {
    boolean existsByPhone(String phone);
}
