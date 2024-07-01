package registration.uz.hgpuserregistration.ContactSupport.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "experts")
@Setter
@Getter
public class Experts implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    private String jobTitle;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
}
