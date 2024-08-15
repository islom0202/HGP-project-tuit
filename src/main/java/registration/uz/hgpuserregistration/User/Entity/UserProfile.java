package registration.uz.hgpuserregistration.User.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import registration.uz.hgpuserregistration.DetectorData.DetectorData;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true)
    private String login;
    @Column(unique = true, nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String phone;
    @Column(unique = true, nullable = false)
    private String email;
    private String address;
    @Column(unique = true, nullable = false)
    private String passportSerialNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "enabled")
    private Boolean enabled = false;
    @Column(name = "locked")
    private Boolean locked = false;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
    @JoinColumn(name = "accessStatus")
    private Boolean accessStatus;

}
