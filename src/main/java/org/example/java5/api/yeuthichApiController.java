package org.example.java5.api;

import jakarta.servlet.http.HttpServletRequest;
import org.example.java5.dto.YeuThichReponse;
import org.example.java5.entity.User;
import org.example.java5.Service.FavoriteService;
import org.example.java5.repository.UserRepository;
import org.example.java5.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/favorites")
public class yeuthichApiController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 📌 Lấy danh sách yêu thích
    @GetMapping
    public ResponseEntity<?> getFavorites(HttpServletRequest request) {
        Optional<User> userOpt = getUserFromToken(request);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized or invalid token");
        }

        List<YeuThichReponse> favorites = favoriteService.getFavoritesByUser(userOpt.get())
                .stream()
                .map(fav -> new YeuThichReponse(
                        fav.getId(),
                        fav.getUser().getId(),
                        fav.getProduct().getId(),
                        fav.getProduct().getTen(),
                        fav.getProduct().getHinh(),
                        fav.getProduct().getKichthuoc(),
                        fav.getProduct().getGia(),
                        fav.getLikedDate(),

                        true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }

    // 📌 Thêm vào yêu thích (POST)
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addFavorite(@PathVariable int productId, HttpServletRequest request) {
        Optional<User> userOpt = getUserFromToken(request);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized or invalid token");
        }

        String message = favoriteService.addFavorite(userOpt.get().getId(), productId);
        return ResponseEntity.ok(message);
    }

    // 📌 Xóa khỏi yêu thích (DELETE)
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable int productId, HttpServletRequest request) {
        Optional<User> userOpt = getUserFromToken(request);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized or invalid token");
        }

        String message = favoriteService.removeFavorite(userOpt.get().getId(), productId);
        return ResponseEntity.ok(message);
    }

    // 📌 Hàm dùng chung để lấy user từ JWT
    private Optional<User> getUserFromToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                return Optional.empty();
            }
            token = jwtUtil.cleanToken(token); // Bỏ "Bearer " nếu có
            String email = jwtUtil.extractEmail(token);
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
