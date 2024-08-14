package registration.uz.hgpuserregistration.ContactSupport.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.ContactSupport.Entity.ContactUs;

import java.util.List;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {
}
