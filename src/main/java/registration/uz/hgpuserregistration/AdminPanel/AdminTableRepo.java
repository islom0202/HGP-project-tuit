package registration.uz.hgpuserregistration.AdminPanel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTableRepo extends JpaRepository<AdminTable, Long> {
    AdminTable findByUsername(String username);
}
