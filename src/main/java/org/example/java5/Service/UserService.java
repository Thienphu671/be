package org.example.java5.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.java5.entity.User;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {
        // Kiểm tra nếu email hoặc tên đăng nhập đã tồn tại
        List<User> existingUsers = userRepository.findByEmailOrFullname(user.getEmail(), user.getFullname());

        // Kiểm tra nếu số điện thoại đã tồn tại
        boolean phoneExists = userRepository.existsByPhoneNumber(user.getPhoneNumber());

        if (!existingUsers.isEmpty() || phoneExists) {
            return "Email, tên đăng nhập hoặc số điện thoại đã tồn tại!";
        }

        // Nếu không trùng, tiếp tục đăng ký
        userRepository.save(user);
        return "Đăng ký thành công!";
    }
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> getById1(Integer id) {
        return userRepository.findById(id); // ✅ Trả về Optional<User>
    }


    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }



    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30)); // Hết hạn sau 30 phút
            userRepository.save(user);

            String resetLink = "http://localhost:3000/reset-password?token=" + token;
            sendEmail(email, resetLink);
        }
    }

    private void sendEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Yêu cầu đặt lại mật khẩu");
            helper.setText("Click vào link sau để đặt lại mật khẩu: <a href=\"" + resetLink + "\">Đặt lại mật khẩu</a>", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi email", e);
        }
    }

    public boolean validateResetToken(String token) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        return optionalUser.isPresent() && optionalUser.get().getResetTokenExpiry().isAfter(LocalDateTime.now());
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> optionalUser = userRepository.findByResetToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(newPassword);
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
        }
    }







// đổi mật khẩu


    public boolean changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            System.out.println("User không tồn tại với email: " + email);
            return false;
        }

        User user = optionalUser.get();

        // Kiểm tra mật khẩu cũ (tránh null)
        if (user.getPassword() != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword); // Lưu mật khẩu mới không mã hóa
            userRepository.save(user);
            System.out.println("Mật khẩu đã được thay đổi cho email: " + email);
            return true;
        }

        System.out.println("Mật khẩu cũ không đúng cho email: " + email);
        return false;
    }











    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    // đổi thông tin cá nhân
    public boolean updateUserInfo(String email, String fullname, String phoneNumber, String photo) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFullname(fullname);
            user.setPhoneNumber(phoneNumber);
            user.setPhoto(photo);

            userRepository.save(user);
            return true;
        }

        return false;
    }

// kich hoat trang thai
    public void toggleActive(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActivated(!user.getActivated()); // Đảo trạng thái kích hoạt
        userRepository.save(user); // Lưu thay đổi vào database
    }
//phan quyen admin

    public void toggleAdmin(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("không tìm thấy người dùng"));
        user.setAdmin(!user.getAdmin()); // Đảo trạng thái admin
        userRepository.save(user);
    }
}
