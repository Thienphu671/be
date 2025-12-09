package org.example.java5.repository;

import org.example.java5.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongKeRepository extends JpaRepository<Order, Long> {



    // ====== THỐNG KÊ DOANH THU ======
    @Query("SELECT COALESCE(SUM(od.price * od.quantity), 0) FROM OrderDetail od WHERE FUNCTION('YEAR', od.order.orderDate) = :year AND od.order.status = 1")
    Double getRevenueByYear(@Param("year") int year);

    @Query("SELECT COALESCE(SUM(od.price * od.quantity), 0) FROM OrderDetail od WHERE FUNCTION('YEAR', od.order.orderDate) = :year AND FUNCTION('MONTH', od.order.orderDate) = :month AND od.order.status = 1")
    Double getRevenueByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(od.price * od.quantity), 0) FROM OrderDetail od WHERE FUNCTION('YEAR', od.order.orderDate) = :year AND FUNCTION('MONTH', od.order.orderDate) = :month AND FUNCTION('DAY', od.order.orderDate) = :day AND od.order.status = 1")
    Double getRevenueByDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    // ====== DANH SÁCH NĂM, THÁNG, NGÀY CÓ DOANH THU ======
    @Query("SELECT DISTINCT FUNCTION('YEAR', o.orderDate) FROM Order o WHERE o.status = 1 ORDER BY FUNCTION('YEAR', o.orderDate) DESC")
    List<Integer> getAvailableYears();

    @Query("SELECT DISTINCT FUNCTION('MONTH', o.orderDate) FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year AND o.status = 1 ORDER BY FUNCTION('MONTH', o.orderDate)")
    List<Integer> getAvailableMonths(@Param("year") int year);

    @Query("SELECT DISTINCT FUNCTION('DAY', o.orderDate) FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) = :month AND o.status = 1 ORDER BY FUNCTION('DAY', o.orderDate)")
    List<Integer> getAvailableDays(@Param("year") int year, @Param("month") int month);

    // ====== THỐNG KÊ SẢN PHẨM YÊU THÍCH ======
    @Query("SELECT f.product.ten, COUNT(DISTINCT f.user.id) AS favoriteCount " +
            "FROM Favorite f " +
            "GROUP BY f.product.ten " +
            "ORDER BY favoriteCount DESC")
    List<Object[]> getMostFavoriteProducts();

    @Query("SELECT f.product.ten, COUNT(DISTINCT f.user.id) AS favoriteCount " +
            "FROM Favorite f " +
            "WHERE FUNCTION('YEAR', f.likedDate) = :year " +
            "GROUP BY f.product.ten " +
            "ORDER BY favoriteCount DESC")
    List<Object[]> getMostFavoriteProductsByYear(@Param("year") int year);

    @Query("SELECT f.product.ten, COUNT(DISTINCT f.user.id) AS favoriteCount " +
            "FROM Favorite f " +
            "WHERE FUNCTION('YEAR', f.likedDate) = :year AND FUNCTION('MONTH', f.likedDate) = :month " +
            "GROUP BY f.product.ten " +
            "ORDER BY favoriteCount DESC")
    List<Object[]> getMostFavoriteProductsByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT f.product.ten, COUNT(DISTINCT f.user.id) AS favoriteCount " +
            "FROM Favorite f " +
            "WHERE FUNCTION('YEAR', f.likedDate) = :year AND FUNCTION('MONTH', f.likedDate) = :month AND FUNCTION('DAY', f.likedDate) = :day " +
            "GROUP BY f.product.ten " +
            "ORDER BY favoriteCount DESC")
    List<Object[]> getMostFavoriteProductsByDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    // ====== DANH SÁCH NĂM, THÁNG, NGÀY CÓ SẢN PHẨM YÊU THÍCH ======
    @Query("SELECT DISTINCT FUNCTION('YEAR', f.likedDate) FROM Favorite f ORDER BY FUNCTION('YEAR', f.likedDate) DESC")
    List<Integer> getAvailableFavoriteYears();

    @Query("SELECT DISTINCT FUNCTION('MONTH', f.likedDate) FROM Favorite f WHERE FUNCTION('YEAR', f.likedDate) = :year ORDER BY FUNCTION('MONTH', f.likedDate)")
    List<Integer> getAvailableFavoriteMonths(@Param("year") int year);

    @Query("SELECT DISTINCT FUNCTION('DAY', f.likedDate) FROM Favorite f WHERE FUNCTION('YEAR', f.likedDate) = :year AND FUNCTION('MONTH', f.likedDate) = :month ORDER BY FUNCTION('DAY', f.likedDate)")
    List<Integer> getAvailableFavoriteDays(@Param("year") int year, @Param("month") int month);

    // ====== THỐNG KÊ SẢN PHẨM BÁN CHẠY ======
    @Query("SELECT od.product.ten, SUM(od.quantity) AS totalSold " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.status = 1 " +
            "GROUP BY od.product.ten " +
            "ORDER BY totalSold DESC")
    List<Object[]> getBestSellingProducts();

    @Query("SELECT od.product.ten, SUM(od.quantity) AS totalSold " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.status = 1 AND FUNCTION('YEAR', o.orderDate) = :year " +
            "GROUP BY od.product.ten " +
            "ORDER BY totalSold DESC")
    List<Object[]> getBestSellingProductsByYear(@Param("year") int year);

    @Query("SELECT od.product.ten, SUM(od.quantity) AS totalSold " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.status = 1 AND FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) = :month " +
            "GROUP BY od.product.ten " +
            "ORDER BY totalSold DESC")
    List<Object[]> getBestSellingProductsByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT od.product.ten, SUM(od.quantity) AS totalSold " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.status = 1 AND FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) = :month AND FUNCTION('DAY', o.orderDate) = :day " +
            "GROUP BY od.product.ten " +
            "ORDER BY totalSold DESC")
    List<Object[]> getBestSellingProductsByDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    // ====== DANH SÁCH NĂM, THÁNG, NGÀY CÓ SẢN PHẨM BÁN CHẠY ======
    @Query("SELECT DISTINCT FUNCTION('YEAR', o.orderDate) FROM Order o WHERE o.status = 1 ORDER BY FUNCTION('YEAR', o.orderDate) DESC")
    List<Integer> getAvailableSellingYears();

    @Query("SELECT DISTINCT FUNCTION('MONTH', o.orderDate) FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year AND o.status = 1 ORDER BY FUNCTION('MONTH', o.orderDate)")
    List<Integer> getAvailableSellingMonths(@Param("year") int year);

    @Query("SELECT DISTINCT FUNCTION('DAY', o.orderDate) FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = :year AND FUNCTION('MONTH', o.orderDate) = :month AND o.status = 1 ORDER BY FUNCTION('DAY', o.orderDate)")
    List<Integer> getAvailableSellingDays(@Param("year") int year, @Param("month") int month);



}
