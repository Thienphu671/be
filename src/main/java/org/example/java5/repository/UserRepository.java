//package org.example.java5.repository;
//
//import org.example.java5.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface UserRepository extends JpaRepository<User, Integer> {
//
//    @Query(value = "SELECT * FROM taikhoan WHERE email = ?1 OR hoten = ?2", nativeQuery = true)
//    List<User> findByEmailOrFullname(String email, String fullname);
//
//    Optional<User> findByEmail(String email);
//    Optional<User> findById(int userId);
//    Optional<User> findByResetToken(String resetToken);
//    Optional<User> getById(int id);
//    boolean existsByPhoneNumber(String phoneNumber);
//    boolean existsByEmail(String email);     // Thêm để kiểm tra trùng email
//    boolean existsByFullname(String fullname); // Thêm để kiểm tra trùng fullname
//
//    List<User> findByFullnameContaining(String fullname);
//    List<User> findByEmailContaining(String email);
//
//
//}

package org.example.java5.repository;

import org.example.java5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM taikhoan WHERE email = ?1 OR hoten = ?2", nativeQuery = true)
    List<User> findByEmailOrFullname(String email, String fullname);

    Optional<User> findByEmail(String email);
    Optional<User> findById(int userId);
    Optional<User> findByResetToken(String resetToken);
    Optional<User> getById(int id);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);     // Thêm để kiểm tra trùng email
    boolean existsByFullname(String fullname); // Thêm để kiểm tra trùng fullname

    List<User> findByFullnameContaining(String fullname);
    List<User> findByEmailContaining(String email);

    // Thêm các phương thức tìm kiếm người dùng không phải admin
    List<User> findByAdminFalse();
    List<User> findByFullnameContainingAndAdminFalse(String fullname);
    List<User> findByEmailContainingAndAdminFalse(String email);
}
