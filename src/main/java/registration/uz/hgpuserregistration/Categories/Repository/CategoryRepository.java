package registration.uz.hgpuserregistration.Categories.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.Categories.Entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByName(String name);
    Category getCategoryByName(String profile);
}
