package registration.uz.hgpuserregistration.ContactSupport.Model;

import lombok.Data;

@Data
public class MessageResponse {
    private Long userId;
    private Long messageId;

    public MessageResponse(Long userId, Long messageId) {
        this.userId = userId;
        this.messageId = messageId;
    }
}
