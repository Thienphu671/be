package org.example.java5.controllers.nguoiDung;

import jakarta.servlet.http.HttpSession;
import org.example.java5.entity.Favorite;
import org.example.java5.entity.User;
import org.example.java5.Service.FavoriteService;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/favorites")
public class  yeuthich{  // ✅ Đổi tên class theo chuẩn CamelCase
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    // 📌 Lấy danh sách yêu thích
    @GetMapping
    public String getFavorites(HttpSession session, Model model, @RequestParam(required = false) String message) {
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanid == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(taikhoanid);
        if (userOpt.isPresent()) {
            List<Favorite> favorites = favoriteService.getFavoritesByUser(userOpt.get());
            model.addAttribute("favorites", favorites);
            model.addAttribute("message", message);
            return "nguoiDung/yeuthich";
        }
        return "error";
    }

    // 📌 Thêm vào yêu thích (GET)
    @GetMapping("/add/{productId}")
    public String addFavorite(@PathVariable int productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanid == null) {
            return "redirect:/login";
        }

        // ✅ Kiểm tra thông báo từ service
        String message = favoriteService.addFavorite(taikhoanid, productId);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/favorites";
    }

    // 📌 Xóa khỏi yêu thích
    @GetMapping("/remove/{productId}")
    public String removeFavorite(@PathVariable int productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanid == null) {
            return "redirect:/login";
        }

        // ✅ Kiểm tra thông báo từ service
        String message = favoriteService.removeFavorite(taikhoanid, productId);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/favorites";
    }

    // 📌 Thêm vào yêu thích (POST) - Tránh trùng URL với GET
    @PostMapping("/add-to-favorites/{productId}")  // ✅ Đổi URL để tránh trùng lặp
    public String addToFavorites(@PathVariable int productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanid == null) {
            return "redirect:/login";
        }

        // ✅ Kiểm tra thông báo từ service
        String message = favoriteService.addFavorite(taikhoanid, productId);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/sanPham";
    }
}
