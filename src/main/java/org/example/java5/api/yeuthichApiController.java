package org.example.java5.api;

import jakarta.servlet.http.HttpSession;
import org.example.java5.dto.YeuThichReponse;
import org.example.java5.entity.User;
import org.example.java5.Service.FavoriteService;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class yeuthichApiController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách yêu thích
    @GetMapping
    public ResponseEntity<?> getFavorites(HttpSession session) {
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanid == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Optional<User> userOpt = userRepository.findById(taikhoanid);
        if (userOpt.isPresent()) {
            List<YeuThichReponse> responseList = favoriteService.getFavoritesByUser(userOpt.get())
                    .stream()
                    .map(fav -> new YeuThichReponse(
                            fav.getId(),
                            fav.getUser().getId(),
                            fav.getProduct().getId(),
                            fav.getLikedDate(),
                            true))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        }
        return ResponseEntity.status(404).body("User not found");
    }

    //  Thêm vào yêu thích
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addFavorite(@PathVariable int productId, HttpSession session) {
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanid == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String message = favoriteService.addFavorite(taikhoanid, productId);
        return ResponseEntity.ok(message);
    }

    //  Xóa khỏi yêu thích
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFavorite(@PathVariable int productId, HttpSession session) {
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        if (taikhoanid == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String message = favoriteService.removeFavorite(taikhoanid, productId);
        return ResponseEntity.ok(message);
    }
}