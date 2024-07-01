package registration.uz.hgpuserregistration.ContactSupport.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.ContactSupport.Entity.EmergencyService;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmergencyServiceRepository {
    private final EntityManager em;

    public List<EmergencyService> findByFilter(
            String name,
            String location
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EmergencyService> query = cb.createQuery(EmergencyService.class);
        Root<EmergencyService> root = query.from(EmergencyService.class);

        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            Predicate predicate = cb.like(root.get("name"), "%" + name + "%");
            predicates.add(predicate);
        }
        if (location != null && !location.isEmpty()) {
            Predicate predicate = cb.like(root.get("location"), "%" + location + "%");
            predicates.add(predicate);
        }
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        TypedQuery<EmergencyService> q = em.createQuery(query);
        return q.getResultList();
    }

    @Query(value = "select 1 from EmergencyService where name =: name")
    public boolean exists(@Param("name") String name) {
        return false;
    }
}
