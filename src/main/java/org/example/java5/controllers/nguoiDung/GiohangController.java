package org.example.java5.controllers.nguoiDung;

import jakarta.servlet.http.HttpSession;
import org.example.java5.Service.GiohangService;
import org.example.java5.Service.SanPhamService;
import org.example.java5.entity.Giohang;
import org.example.java5.entity.Product;
import org.example.java5.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.java5.repository.UserRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/giohang")
public class GiohangController {
    private final GiohangService giohangService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    public GiohangController(GiohangService giohangService) {
        this.giohangService = giohangService;
    }

    // Kiểm tra session trước khi hiển thị giỏ hàng
    @GetMapping
    public String hienThiGiohang(HttpSession session, Model model) {
        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");

        if (taikhoanId == null) {
            System.out.println("⚠ Người dùng chưa đăng nhập, chuyển hướng sang trang login!");
            return "redirect:/login";
        }

        // Lấy thông tin người dùng từ DB
        Optional<User> userOptional = userRepository.findById(taikhoanId);
        if (userOptional.isEmpty()) {
            System.out.println("⚠ Không tìm thấy người dùng trong DB!");
            return "redirect:/login";
        }

        User user = userOptional.get();
        session.setAttribute("taikhoanid", user.getId());

        System.out.println("Session hợp lệ! Người dùng ID: " + taikhoanId);

        // Lấy giỏ hàng từ service thay vì session
        List<Giohang> gioHang = giohangService.getAllByTaikhoanId(taikhoanId);
        if (gioHang.isEmpty()) {
            model.addAttribute("message", "Giỏ hàng trống.");
        } else {
            model.addAttribute("gioHang", gioHang);
        }
        System.out.println(" Danh sách sản phẩm trong giỏ hàng:");
        for (Giohang item : gioHang) {
            System.out.println("- Sản phẩm: " + item.getSanpham().getTen() + " | Số lượng: " + item.getSoLuong());
        }
        model.addAttribute("giohang", gioHang);



        return "nguoiDung/giohang";
    }

    //  Thêm sản phẩm vào giỏ hàng
    @PostMapping("/them")
    public String themVaoGiohang(@RequestParam Long sanphamId,
                                 @RequestParam int soLuong,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");

        if (taikhoanId == null) {
            System.out.println("⚠ Người dùng chưa đăng nhập, chuyển hướng sang login.");
            return "redirect:/login";
        }

        // Lấy thông tin sản phẩm
        Product sanpham = sanPhamService.laySanphamTheoId(sanphamId);
        if (sanpham == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/sanPham";
        }

        // Kiểm tra trạng thái sản phẩm
        if (sanpham.getStatus() == 2) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm này hiện không thể mua!");
            return "redirect:/sanPham";
        }

        //  Lấy số lượng tồn kho
        int soLuongTonKho = sanpham.getSoluong();

        //  Kiểm tra số lượng trong giỏ hàng hiện tại
        int soLuongHienTaiTrongGio = giohangService.laySoLuongSanphamTrongGio(taikhoanId, sanphamId);
        int tongSoLuongMuonThem = soLuongHienTaiTrongGio + soLuong;

        //  Kiểm tra nếu tổng số lượng vượt quá số lượng tồn kho
        if (tongSoLuongMuonThem > soLuongTonKho) {
            redirectAttributes.addFlashAttribute("error", "Số lượng sản phẩm trong kho không đủ!");
            return "redirect:/sanPham";
        }

        //  Thêm sản phẩm vào giỏ hàng
        giohangService.themVaoGiohang(taikhoanId, sanphamId, soLuong);

        System.out.println(" Người dùng ID: " + taikhoanId + " thêm sản phẩm ID: " + sanphamId);

        return "redirect:/giohang";
    }



    //  Cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/capnhat")
    public String capNhatSoLuong(@RequestParam Integer giohangId,
                                 @RequestParam int soLuong,
                                 HttpSession session) {
        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");

        if (taikhoanId == null) {
            return "redirect:/login";
        }

        giohangService.capNhatSoLuong(giohangId, soLuong);
        return "redirect:/giohang";
    }


    @PostMapping("/xoa/{taikhoanId}/{sanphamId}")
    public String xoaSanPhamTrongGiohang(@PathVariable Integer taikhoanId, @PathVariable Integer sanphamId) {
        giohangService.xoaGiohangTheoTaikhoan(taikhoanId, sanphamId);
        return "redirect:/giohang";
    }




    //  Lấy toàn bộ giỏ hàng của người dùng
    @GetMapping("/all")
    public String getAll(HttpSession session, Model model) {
        Integer taikhoanId = (Integer) session.getAttribute("taikhoanid");

        if (taikhoanId == null) {
            return "redirect:/login";
        }

        List<Giohang> cartItems = giohangService.getAllByTaikhoanId(taikhoanId);
        model.addAttribute("cartItems", cartItems);

        return "nguoiDung/giohang";
    }
}
