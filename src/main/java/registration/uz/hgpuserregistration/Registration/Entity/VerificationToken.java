package registration.uz.hgpuserregistration.Registration.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "verification_token")
@Getter
@Setter
public class VerificationToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date generatedDate;
    private Date expiryDate;
    @OneToOne(targetEntity = UserProfile.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserProfile user;
}
