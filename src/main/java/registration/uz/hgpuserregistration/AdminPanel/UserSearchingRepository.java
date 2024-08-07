package registration.uz.hgpuserregistration.AdminPanel;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.User.Entity.Gender;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserSearchingRepository {

    private final EntityManager em;

    public List<UserProfile> findBySearchTerm(
            String firstname,
            String lastname,
            String email,
            Gender gender,
            String enabled
    ) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);

        Root<UserProfile> root = cq.from(UserProfile.class);
        List<Predicate> predicates = new ArrayList<>();
        if (firstname != null && !firstname.isEmpty()) {
            Predicate namePredicate = cb.like(root.get("firstname"), "%" + firstname + "%");
            predicates.add(namePredicate);
        }
        if (lastname != null && !lastname.isEmpty()) {
            Predicate namePredicate = cb.like(root.get("lastname"), "%" + lastname + "%");
            predicates.add(namePredicate);
        }
        if (email != null && !email.isEmpty()) {
            Predicate emailPredicate = cb.like(root.get("email"), "%" + email + "%");
            predicates.add(emailPredicate);
        }
        if (gender != null) {
            Predicate genderPredicate = cb.equal(root.get("gender"), gender);
            predicates.add(genderPredicate);
        }
        if (enabled != null && !enabled.isEmpty()){
            boolean enabledBool = Boolean.parseBoolean(enabled);
            Predicate enabledPredicate = cb.equal(root.get("enabled"), enabledBool);
            predicates.add(enabledPredicate);
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<UserProfile> typedQuery = em.createQuery(cq);
        return typedQuery.getResultList();
    }
}
