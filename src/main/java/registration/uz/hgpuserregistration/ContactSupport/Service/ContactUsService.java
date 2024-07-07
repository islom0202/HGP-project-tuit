package registration.uz.hgpuserregistration.ContactSupport.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.ContactSupport.Entity.ContactUs;
import registration.uz.hgpuserregistration.ContactSupport.Model.MessageDto;
import registration.uz.hgpuserregistration.ContactSupport.Repository.ContactUsRepository;
import registration.uz.hgpuserregistration.Email.EmailService;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ContactUsService {
    private final ContactUsRepository contactUsRepository;
    private final EmailService emailService;

    @Transactional
    public void save(MessageDto message) {
        ContactUs contactUs = new ContactUs();
        contactUs.setMessage(message.getMessage());
        contactUs.setEmail(message.getEmail());
        contactUs.setFirstname(message.getFirstname());
        contactUs.setLastname(message.getLastname());
        contactUs.setPhoneNumber(message.getPhoneNumber());
        contactUs.setSentAt(new Date());
        contactUsRepository.save(contactUs);
        try {
            emailService.sendToAdmin(message.getEmail(), message.getFirstname() + " " +message.getLastname(), message.getMessage());
            emailService.sendToUser(message.getEmail());
        } catch (Exception e) {
             throw new RuntimeException("Error sending email", e);
        }
    }

    public List<ContactUs> findAll() {
        return contactUsRepository.findAll();
    }
}
