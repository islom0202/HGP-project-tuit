package registration.uz.hgpuserregistration.ContactSupport.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.ContactSupport.Entity.ContactUs;
import registration.uz.hgpuserregistration.ContactSupport.Model.ContactUsResponseDto;
import registration.uz.hgpuserregistration.ContactSupport.Model.MessageDto;
import registration.uz.hgpuserregistration.ContactSupport.Model.MessageResponse;
import registration.uz.hgpuserregistration.ContactSupport.Repository.ContactUsRepository;
import registration.uz.hgpuserregistration.Email.EmailService;
import registration.uz.hgpuserregistration.User.Entity.UserProfile;
import registration.uz.hgpuserregistration.User.Respository.UserProfileRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class ContactUsService {
    private final ContactUsRepository contactUsRepository;
    private final EmailService emailService;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public void save(MessageDto message) {
        Optional<UserProfile> user = userProfileRepository.findByEmail(message.getEmail());
        ContactUs contactUs = new ContactUs();
        contactUs.setMessage(message.getMessage());
        contactUs.setEmail(message.getEmail());
        contactUs.setTitle(message.getTitle());
        contactUs.setSentAt(new Date());
        contactUs.setRead(false);
        if (user.isPresent()) {
            UserProfile userProfile = user.get();
            contactUs.setFirstName(userProfile.getFirstname());
            contactUs.setLastName(userProfile.getLastname());
        }
        contactUsRepository.save(contactUs);
        try {
            emailService.sendToAdmin(message.getEmail(), message.getTitle(), message.getMessage());
            emailService.sendToUser(message.getEmail());
        } catch (Exception e) {
             throw new RuntimeException("Error sending email", e);
        }
    }

    public List<ContactUsResponseDto> findAll() {
        List<ContactUs> list = new ArrayList<>(contactUsRepository
                .findAll(Sort.by(Sort.Direction.DESC, "sentAt","isRead")));

        List<ContactUsResponseDto> responseDtos = new ArrayList<>();

        for (ContactUs contactUs : list) {
            ContactUsResponseDto responseDto = getContactUsResponseDto(contactUs);
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }

    private ContactUsResponseDto getContactUsResponseDto(ContactUs contactUs) {
        ContactUsResponseDto responseDto = new ContactUsResponseDto();
        responseDto.setId(contactUs.getId());
        responseDto.setFirstName(contactUs.getFirstName());
        responseDto.setLastName(contactUs.getLastName());
        responseDto.setEmail(contactUs.getEmail());
        responseDto.setTitle(contactUs.getTitle());
        responseDto.setSentAt(contactUs.getSentAt());
        responseDto.setMessage(contactUs.getMessage());
        responseDto.setRead(contactUs.isRead());
        responseDto.setUserId(getUserId(contactUs.getEmail()));
        return responseDto;
    }

    private Long getUserId(String email) {
        return userProfileRepository.findByEmail(email).get().getId();
    }

    @Transactional
    public void isRead(MessageResponse response) {
        Optional<UserProfile> user = userProfileRepository.findById(response.getUserId());
        if (user.isPresent()) {
            UserProfile userProfile = user.get();
            ContactUs contactUs = contactUsRepository.findByIdAndEmail(response.getMessageId(), userProfile.getEmail());

            if (contactUs != null) {
                contactUs.setRead(true);  // Ensure the read flag is set to true
                contactUsRepository.save(contactUs);  // Save the updated entity
            } else {
                // Handle case where ContactUs is not found
                throw new RuntimeException("ContactUs message not found for the given email and messageId");
            }
        } else {
            // Handle case where UserProfile is not found
            throw new RuntimeException("UserProfile not found for the given userId");
        }
    }

    public Integer countMessage() {
        int count = contactUsRepository.countMessage();
        return Integer.valueOf(count);
    }
}
