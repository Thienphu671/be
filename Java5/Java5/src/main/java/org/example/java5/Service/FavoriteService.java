package org.example.java5.Service;

import jakarta.transaction.Transactional;
import org.example.java5.entity.Favorite;
import org.example.java5.entity.User;
import org.example.java5.entity.Product;
import org.example.java5.repository.FavoriteRepository;

import org.example.java5.repository.SanPhamRepository;
import org.example.java5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SanPhamRepository productRepository;

    // Lấy danh sách sản phẩm yêu thích của một người dùng
    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteRepository.findByUser(user);
    }

    // Thêm sản phẩm vào danh sách yêu thích
    public String addFavorite(int userId, int productId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (userOpt.isPresent() && productOpt.isPresent()) {
            User user = userOpt.get();
            Product product = productOpt.get();

            Optional<Favorite> favoriteOpt = favoriteRepository.findByUserAndProduct(user, product);
            if (favoriteOpt.isPresent()) {
                Favorite favorite = favoriteOpt.get();
                if (!favorite.isStatus()) {
                    favorite.setStatus(true);
                    favorite.setLikedDate(new Date());
                    favoriteRepository.save(favorite);
                    return "Sản phẩm đã được thêm lại vào danh sách yêu thích!";
                }
                return "Sản phẩm đã có trong danh sách yêu thích!";
            }

            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProduct(product);
            favorite.setLikedDate(new Date());
            favorite.setStatus(true);
            favoriteRepository.save(favorite);
            return "Đã thêm vào danh sách yêu thích!";
        }
        return "Người dùng hoặc sản phẩm không tồn tại!";
    }





    @Transactional
    public String removeFavorite(int userId, int productId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (userOpt.isPresent() && productOpt.isPresent()) {
            User user = userOpt.get();
            Product product = productOpt.get();

            Optional<Favorite> favoriteOpt = favoriteRepository.findByUserAndProduct(user, product);
            if (favoriteOpt.isPresent()) {
                favoriteRepository.delete(favoriteOpt.get()); // Xóa bản ghi khỏi DB
                return "Đã xóa sản phẩm khỏi danh sách yêu thích!";
            }
            return "Sản phẩm không có trong danh sách yêu thích!";
        }
        return "Người dùng hoặc sản phẩm không tồn tại!";
    }


}
