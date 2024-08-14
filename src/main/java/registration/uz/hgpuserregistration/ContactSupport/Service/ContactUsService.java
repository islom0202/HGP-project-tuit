package registration.uz.hgpuserregistration.ContactSupport.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.ContactSupport.Entity.ContactUs;
import registration.uz.hgpuserregistration.ContactSupport.Model.MessageDto;
import registration.uz.hgpuserregistration.ContactSupport.Repository.ContactUsRepository;
import registration.uz.hgpuserregistration.Email.EmailService;

import java.util.*;

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
        contactUs.setTitle(message.getTitle());
        contactUs.setSentAt(new Date());
        contactUsRepository.save(contactUs);
        try {
            emailService.sendToAdmin(message.getEmail(), message.getTitle(), message.getMessage());
            emailService.sendToUser(message.getEmail());
        } catch (Exception e) {
             throw new RuntimeException("Error sending email", e);
        }
    }

    public List<ContactUs> findAll() {
        List<ContactUs> list = new ArrayList<>(contactUsRepository.findAll(Sort.by(Sort.Direction.DESC, "sentAt")));
        //Collections.reverse(list);
        return list;
    }
}
