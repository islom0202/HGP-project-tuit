package registration.uz.hgpuserregistration.Order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
public class UserOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Date orderDate;
    @OneToOne(optional = false)
    @JoinColumn(
            name = "user_profile_id"
    )
    private UserProfile userProfile;
    @Column(unique = true)
    private String orderAddress;
}
