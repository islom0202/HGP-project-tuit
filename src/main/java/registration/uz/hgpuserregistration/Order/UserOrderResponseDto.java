package registration.uz.hgpuserregistration.Order;

import lombok.Data;

import java.util.Date;

@Data
public class UserOrderResponseDto {
    private Long id;
    private Date orderDate;
    private Long userId;
    private String orderAddress;
    private boolean delivered;
}
