package org.example.java5.controllers.nguoiDung;

import org.example.java5.Service.OrderService;
import org.example.java5.Service.UserService;
import org.example.java5.dto.DanhGiaDTO;
import org.example.java5.entity.DanhGia;
import org.example.java5.entity.Order;
import org.example.java5.entity.User;
import org.example.java5.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")  // URL của frontend

@RestController
@RequestMapping("/api/danhgia")
public class DanhGiaController {

    @Autowired
    private DanhGiaRepository danhGiaRepo;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

//    @PostMapping("/submit")
//    public ResponseEntity<String> submitReview(@RequestBody DanhGiaDTO dto) {
//        // Kiểm tra nếu đơn hàng đã có đánh giá
//        if (danhGiaRepo.existsByDonHangId(dto.getDonhangId())) {
//            return ResponseEntity.status(400).body("Bạn đã đánh giá đơn hàng này.");
//        }
//
//        // Kiểm tra tồn tại đơn hàng
//        Optional<Order> orderOpt = orderService.getOrderById(dto.getDonhangId());
//        if (orderOpt.isEmpty()) {
//            return ResponseEntity.status(404).body("Đơn hàng không tồn tại.");
//        }
//        Order order = orderOpt.get();
//
//        // Kiểm tra tồn tại người dùng
//        Optional<User> userOpt = userService.getById(dto.getTaikhoanId());
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(404).body("Người dùng không tồn tại.");
//        }
//        User user = userOpt.get();
//
//        // Kiểm tra số sao hợp lệ (từ 1 đến 5)
//        if (dto.getSao() < 1 || dto.getSao() > 5) {
//            return ResponseEntity.status(400).body("Số sao phải nằm trong khoảng từ 1 đến 5.");
//        }
//
//        // Tạo và lưu đánh giá
//        DanhGia dg = new DanhGia();
//        dg.setDonHang(order);
//        dg.setTaiKhoan(user);
//        dg.setDanhgia(dto.getDanhgia());
//        dg.setSao(dto.getSao());
//        dg.setNgayDanhgia(LocalDate.now());
//
//        danhGiaRepo.save(dg);
//
//        return ResponseEntity.ok("Đánh giá thành công!");
//    }


    @PostMapping("/submit")
    public ResponseEntity<String> submitReview(@RequestBody DanhGiaDTO dto) {
        System.out.println("Received review data: " + dto);
        System.out.println("Received user ID: " + dto.getTaikhoanId());

        // Kiểm tra nếu đơn hàng đã có đánh giá
        if (danhGiaRepo.existsByDonHangId(dto.getDonhangId())) {
            return ResponseEntity.status(400).body("Bạn đã đánh giá đơn hàng này.");
        }

        // Kiểm tra tồn tại đơn hàng
        Optional<Order> orderOpt = orderService.getOrderById(dto.getDonhangId());
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Đơn hàng không tồn tại.");
        }
        Order order = orderOpt.get();

        // Kiểm tra tồn tại người dùng
        Optional<User> userOpt = userService.getById(dto.getTaikhoanId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Người dùng không tồn tại.");
        }
        User user = userOpt.get();

        // Kiểm tra số sao hợp lệ (từ 1 đến 5)
        if (dto.getSao() < 1 || dto.getSao() > 5) {
            return ResponseEntity.status(400).body("Số sao phải nằm trong khoảng từ 1 đến 5.");
        }

        // Tạo và lưu đánh giá
        DanhGia dg = new DanhGia();
        dg.setDonHang(order);
        dg.setTaiKhoan(user);
        dg.setDanhgia(dto.getDanhgia());
        dg.setSao(dto.getSao());
        dg.setNgayDanhgia(LocalDate.now());

        danhGiaRepo.save(dg);
        System.out.println("Review saved successfully");

        return ResponseEntity.ok("Đánh giá thành công!");
    }

}
