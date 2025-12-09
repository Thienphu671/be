package org.example.java5.repository;

import jakarta.transaction.Transactional;
import org.example.java5.entity.Favorite;
import org.example.java5.entity.User;
import org.example.java5.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser(User user);
    boolean existsByUserAndProduct(User user, Product product);
    @Transactional
        // Đảm bảo có transaction khi xóa
    void deleteByUserAndProduct(User user, Product product);
    public Optional<Favorite> findById(Integer id);

    Optional<Favorite> findByUserAndProduct(User user, Product product);  // ✅ Thêm dòng này


}

