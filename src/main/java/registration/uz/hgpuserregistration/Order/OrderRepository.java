package registration.uz.hgpuserregistration.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {

    boolean existsByUserProfile(UserProfile userProfile);

    void deleteByUserProfile_Id(Long id);

    UserOrder findByUserProfile(Optional<UserProfile> userProfile);

//    @Query(value = """
//            WITH monthly_counts AS (
//                                SELECT
//                                    TO_CHAR(order_date, 'YYYY') AS year,
//                                    TO_CHAR(order_date, 'MM') AS month,
//                                    COUNT(*) AS number
//                                FROM
//                                    user_order
//                                GROUP BY
//                                    TO_CHAR(order_date, 'YYYY'),
//                                    TO_CHAR(order_date, 'MM')
//                            )
//                            SELECT json_agg(json_build_object(
//                                    'year', year,
//                                    'month', month,
//                                    'number', number,
//                                    'income', number * 150
//                                            )) AS result
//                            FROM monthly_counts;
//        """, nativeQuery = true)
//    String getMonthlyCounts();

    @Query(value = """
            WITH monthly_counts AS (
                SELECT
                    TO_CHAR(order_date, 'YYYY') AS year,
                    TO_CHAR(order_date, 'MM') AS month,
                    COUNT(*) AS number
                FROM
                    user_order
                WHERE
                    TO_CHAR(order_date, 'YYYY') = :year
                GROUP BY
                    TO_CHAR(order_date, 'YYYY'),
                    TO_CHAR(order_date, 'MM')
            )
            SELECT json_agg(json_build_object(
                    'year', year,
                    'month', month,
                    'number', number,
                    'income', number * 150
                    )) AS result
            FROM monthly_counts;
        """, nativeQuery = true)
    String getMonthlyCountsByYear(@Param("year") String year);

}
