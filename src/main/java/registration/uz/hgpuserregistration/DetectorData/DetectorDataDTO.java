package registration.uz.hgpuserregistration.DetectorData;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class DetectorDataDTO {
    private double gasPressure;
    private int battery;
    private double temperature;
    private double speed;
    private double price;
}
