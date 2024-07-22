package registration.uz.hgpuserregistration.DetectorData;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;

import java.io.Serializable;

@Entity
@Table(name = "detector_data")
@Setter
@Getter
public class DetectorData implements Serializable {
    @Id
    private String detectorId;
    private double gasPressure;
    private int battery;
    private double temperature;
    //havo namligi
    private double speed;
    private double price;
    @OneToOne(optional = false)
    private UserProfile userId;
}
