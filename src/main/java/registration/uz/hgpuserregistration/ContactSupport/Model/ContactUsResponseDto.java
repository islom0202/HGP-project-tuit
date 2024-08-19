package registration.uz.hgpuserregistration.ContactSupport.Model;

import lombok.Data;

import java.util.Date;

@Data
public class ContactUsResponseDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String message;
    private boolean isRead;
    private Date sentAt;
}
