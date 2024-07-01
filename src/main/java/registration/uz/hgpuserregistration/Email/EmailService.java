package registration.uz.hgpuserregistration.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String to,String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(text);
        message.setTo(to);
        message.setFrom(from);

        javaMailSender.send(message);
    }
    @Async
    public void sendToUser(String to) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Thank You for Reaching Out - Message Received");
        mailMessage.setTo(to);
        mailMessage.setText("""
                Dear User,

                Thank you for reaching out to us. We have successfully received your message and our team will review it shortly. Below are the details of your submission:

                We appreciate your feedback and will get back to you as soon as possible. If you have any additional information to provide or further questions, please feel free to reply to this email.

                Thank you for using our Gas Detector website.

                Best regards,
                Support Team""");
        mailMessage.setFrom(from);
        javaMailSender.send(mailMessage);
    }
    @Async
    public void sendToAdmin(String to, String title, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(title);
        mailMessage.setTo(from);
        mailMessage.setText(message);
        mailMessage.setFrom(to);
        javaMailSender.send(mailMessage);
    }

}
