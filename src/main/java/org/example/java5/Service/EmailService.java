package org.example.java5.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Phương thức gửi email
    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = hỗ trợ gửi email với định dạng HTML

            // Thiết lập người nhận, chủ đề và nội dung email
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);  // Nội dung email

            // Gửi email
            mailSender.send(message);

        } catch (Exception e) {  // Thay vì catch MessagingException, bạn có thể catch Exception chung
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email", e);
        }
    }
}
