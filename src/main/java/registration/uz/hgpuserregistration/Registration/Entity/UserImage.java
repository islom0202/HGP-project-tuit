package registration.uz.hgpuserregistration.Registration.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "user_image")
public class UserImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @OneToOne
    @JoinColumn(name = "user_profile_id", unique = true, nullable = false)
    private UserProfile userProfile;
}
