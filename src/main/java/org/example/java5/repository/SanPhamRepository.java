package org.example.java5.repository;

import jakarta.transaction.Transactional;
import org.example.java5.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository extends JpaRepository<Product, Integer> {

    List<Product> findByTenContaining(String keyword);
    Optional<Product> findById(Long id);
    void deleteById(Integer id);
    @Query("SELECT p FROM Product p WHERE "
            + "(:keyword IS NULL OR p.ten LIKE %:keyword%) "
            + "AND (:category IS NULL OR p.danhMuc.ten = :Category)")
    List<Product> findByNameAndCategory(@Param("keyword") String keyword, @Param("category") String category);
    List<Product> findByTenContainingIgnoreCase(String keyword);

    //  Tìm kiếm theo danh mục (tìm trong tên danh mục, không phân biệt hoa/thường)
    List<Product> findByDanhMuc_TenContainingIgnoreCase(String category);

    Optional<Product> findById(Integer id);
    Optional<Product> findByTen(String ten);
    //  Tìm kiếm sản phẩm theo nhiều tiêu chí (từ khóa, danh mục, loại)
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:category IS NULL OR p.danhMuc.id = :category) ")
    List<Product> findByFilters(@Param("keyword") String keyword,
                                @Param("category") Integer category );


    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.id = :id")
    void updateProductStatus(@Param("id") Integer id, @Param("status") Integer status);

    Page<Product> findByTenContaining(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE "
            + "(:keyword IS NULL OR LOWER(p.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
            + "AND (:category IS NULL OR p.danhMuc.id = :category)")
    Page<Product> findByFilters(@Param("keyword") String keyword,
                                @Param("category") Integer category,
                                Pageable pageable);
}
