package org.example.java5.api;

import org.example.java5.Service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin/revenue")
public class ThongKeApiController {

    @Autowired
    private ThongKeService thongKeService;

    // ✅ Trả về danh sách năm
    @GetMapping("/menuThongKe")
    public ResponseEntity<String> menuThongKe() {
        return ResponseEntity.ok("menu_thongke");
    }

    // ✅ Trả về danh sách năm cho trang thống kê
    @GetMapping("/thongke")
    public ResponseEntity<List<Integer>> showRevenueStatistics() {
        List<Integer> years = thongKeService.getAvailableYears();
        return ResponseEntity.ok(years);
    }

    // --- Các phương thức trả về API JSON (giữ nguyên) ---
    @GetMapping("/months/{year}")
    public List<Integer> getAvailableMonths(@PathVariable int year) {
        return thongKeService.getAvailableMonths(year);
    }

    @GetMapping("/days/{year}/{month}")
    public List<Integer> getAvailableDays(@PathVariable int year, @PathVariable int month) {
        return thongKeService.getAvailableDays(year, month);
    }

    @GetMapping("/total/{year}")
    public Double getTotalRevenueByYear(@PathVariable int year) {
        return thongKeService.getTotalRevenueByYear(year);
    }

    @GetMapping("/total/{year}/{month}")
    public Double getTotalRevenueByMonth(@PathVariable int year, @PathVariable int month) {
        return thongKeService.getTotalRevenueByMonth(year, month);
    }

    @GetMapping("/total/{year}/{month}/{day}")
    public Double getTotalRevenueByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return thongKeService.getTotalRevenueByDay(year, month, day);
    }

    @GetMapping("/chart/{year}")
    public Map<String, Double> getRevenueChartByYear(@PathVariable int year) {
        return thongKeService.getRevenueChartByYear(year);
    }

    @GetMapping("/chart/{year}/{month}")
    public Map<String, Double> getRevenueChartByMonth(@PathVariable int year, @PathVariable int month) {
        return thongKeService.getRevenueChartByMonth(year, month);
    }

    @GetMapping("/chart/{year}/{month}/{day}")
    public Map<String, Double> getRevenueChartByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return thongKeService.getRevenueChartByDay(year, month, day);
    }

    // ✅ Sản phẩm yêu thích
    @GetMapping("/sanphamyeuthich")
    public ResponseEntity<List<Object[]>> getMostFavoriteProductsPage() {
        List<Object[]> favoriteProducts = thongKeService.getMostFavoriteProducts();
        return ResponseEntity.ok(favoriteProducts);
    }

    @GetMapping("/sanphamyeuthich/data")
    public List<Object[]> getMostFavoriteProductsData() {
        return thongKeService.getMostFavoriteProducts();
    }

    @GetMapping("/most-favorite")
    public ResponseEntity<List<Object[]>> getMostFavoriteProducts() {
        return ResponseEntity.ok(thongKeService.getMostFavoriteProducts());
    }

    @GetMapping("/most-favorite/{year}")
    public ResponseEntity<Map<String, Integer>> getMostFavoriteProductsByYear(@PathVariable int year) {
        return ResponseEntity.ok(thongKeService.getFavoriteChartByYear(year));
    }

    @GetMapping("/most-favorite/{year}/{month}")
    public ResponseEntity<Map<String, Integer>> getMostFavoriteProductsByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(thongKeService.getFavoriteChartByMonth(year, month));
    }

    @GetMapping("/most-favorite/{year}/{month}/{day}")
    public ResponseEntity<Map<String, Integer>> getMostFavoriteProductsByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(thongKeService.getFavoriteChartByDay(year, month, day));
    }

    @GetMapping("/most-favorite/available-months/{year}")
    public ResponseEntity<List<Integer>> getFavoriteMonthsByYear(@PathVariable int year) {
        return ResponseEntity.ok(thongKeService.getFavoriteMonthsByYear(year));
    }

    @GetMapping("/most-favorite/available-days/{year}/{month}")
    public ResponseEntity<List<Integer>> getFavoriteDaysByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(thongKeService.getFavoriteDaysByMonth(year, month));
    }

    @GetMapping("/most-favorite/available-years")
    public ResponseEntity<List<Integer>> getAvailableYears() {
        return ResponseEntity.ok(thongKeService.getAvailableYears());
    }

    // ✅ Sản phẩm bán chạy
    @GetMapping("/sanphambanchay")
    public ResponseEntity<List<Object[]>> getBestSellingProductsPage() {
        return ResponseEntity.ok(thongKeService.getBestSellingProducts());
    }

    @GetMapping("/sanphambanchay/data")
    public List<Object[]> getBestSellingProductsData() {
        return thongKeService.getBestSellingProducts();
    }

    @GetMapping("/best-selling")
    public ResponseEntity<List<Object[]>> getBestSellingProducts() {
        return ResponseEntity.ok(thongKeService.getBestSellingProducts());
    }

    @GetMapping("/best-selling/{year}")
    public ResponseEntity<Map<String, Integer>> getBestSellingProductsByYear(@PathVariable int year) {
        return ResponseEntity.ok(thongKeService.getBestSellingProductsByYear(year));
    }

    @GetMapping("/best-selling/{year}/{month}")
    public ResponseEntity<Map<String, Integer>> getBestSellingProductsByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(thongKeService.getBestSellingProductsByMonth(year, month));
    }

    @GetMapping("/best-selling/{year}/{month}/{day}")
    public ResponseEntity<Map<String, Integer>> getBestSellingProductsByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(thongKeService.getBestSellingProductsByDay(year, month, day));
    }

    @GetMapping("/best-selling/available-months/{year}")
    public ResponseEntity<List<Integer>> getBestSellingMonthsByYear(@PathVariable int year) {
        return ResponseEntity.ok(thongKeService.getBestSellingMonthsByYear(year));
    }

    @GetMapping("/best-selling/available-days/{year}/{month}")
    public ResponseEntity<List<Integer>> getBestSellingDaysByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(thongKeService.getBestSellingDaysByMonth(year, month));
    }

    @GetMapping("/best-selling/available-years")
    public ResponseEntity<List<Integer>> getAvailableYears1() {
        return ResponseEntity.ok(thongKeService.getAvailableYears());
    }
}
