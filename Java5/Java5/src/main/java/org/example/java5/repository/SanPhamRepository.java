//package org.example.java5.repository;
//
//import jakarta.transaction.Transactional;
//import org.example.java5.entity.Product;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface SanPhamRepository extends JpaRepository<Product, Integer> {
//
//    List<Product> findByTenContaining(String keyword);
//    Optional<Product> findById(Long id);
//    void deleteById(Integer id);
//    @Query("SELECT p FROM Product p WHERE "
//            + "(:keyword IS NULL OR p.ten LIKE %:keyword%) "
//            + "AND (:category IS NULL OR p.danhMuc.ten = :Category)")
//    List<Product> findByNameAndCategory(@Param("keyword") String keyword, @Param("category") String category);
//    List<Product> findByTenContainingIgnoreCase(String keyword);
//
//    //  Tìm kiếm theo danh mục (tìm trong tên danh mục, không phân biệt hoa/thường)
//    List<Product> findByDanhMuc_TenContainingIgnoreCase(String category);
//
//    Optional<Product> findById(Integer id);
//    Optional<Product> findByTen(String ten);
//    //  Tìm kiếm sản phẩm theo nhiều tiêu chí (từ khóa, danh mục, loại)
//    @Query("SELECT p FROM Product p WHERE " +
//            "(:keyword IS NULL OR LOWER(p.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//            "AND (:category IS NULL OR p.danhMuc.id = :category) ")
//    List<Product> findByFilters(@Param("keyword") String keyword,
//                                @Param("category") Integer category );
//
//
//    @Modifying
//    @Transactional
//    @Query("UPDATE Product p SET p.status = :status WHERE p.id = :id")
//    void updateProductStatus(@Param("id") Integer id, @Param("status") Integer status);
//
//    Page<Product> findByTenContaining(String keyword, Pageable pageable);
//
//    @Query("SELECT p FROM Product p WHERE "
//            + "(:keyword IS NULL OR LOWER(p.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
//            + "AND (:category IS NULL OR p.danhMuc.id = :category)")
//    Page<Product> findByFilters(@Param("keyword") String keyword,
//                                @Param("category") Integer category,
//                                Pageable pageable);
//}

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
    List<Product> findAllByStatusOrderByIdDesc(int status);
    
    boolean existsByTen(String ten);
    Product findFirstByStatusOrderByIdDesc(int status);
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

    // Lấy 8 sản phẩm có status = 0 và id lớn nhất
    List<Product> findTop8ByStatusOrderByIdDesc(int status);

    @Query("SELECT p FROM Product p " +
            "WHERE (:name IS NULL OR LOWER(p.ten) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:category IS NULL OR LOWER(p.danhMuc.ten) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:size IS NULL OR LOWER(p.kichthuoc) LIKE LOWER(CONCAT('%', :size, '%'))) " +
            "ORDER BY " +
            "(CASE WHEN :ascendingPrice = TRUE THEN p.gia END) ASC, " +
            "(CASE WHEN :ascendingPrice = FALSE THEN p.gia END) DESC")
    Page<Product> findByFiltersAdvanced(String name, String category, String size, Boolean ascendingPrice, Pageable pageable);



}

