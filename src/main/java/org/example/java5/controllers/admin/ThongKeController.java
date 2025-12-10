package org.example.java5.controllers.admin;

import org.example.java5.Service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/revenue")
public class ThongKeController {




    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("/menuThongKe")
    public String menuThongKe() {
        return "admin/menu_thongke";
    }

    // Trả về trang thống kê
    @GetMapping("/thongke")
    public String showRevenueStatistics(Model model) {
        List<Integer> years = thongKeService.getAvailableYears();
        model.addAttribute("years", years);
        return "admin/thongke";
    }

    // API lấy danh sách tháng của một năm
    @ResponseBody
    @GetMapping("/months/{year}")
    public List<Integer> getAvailableMonths(@PathVariable int year) {
        return thongKeService.getAvailableMonths(year);
    }

    // API lấy danh sách ngày của một tháng
    @ResponseBody
    @GetMapping("/days/{year}/{month}")
    public List<Integer> getAvailableDays(@PathVariable int year, @PathVariable int month) {
        return thongKeService.getAvailableDays(year, month);
    }

    // API tính tổng doanh thu theo năm
    @ResponseBody
    @GetMapping("/total/{year}")
    public Double getTotalRevenueByYear(@PathVariable int year) {
        return thongKeService.getTotalRevenueByYear(year);
    }

    // API tính tổng doanh thu theo tháng
    @ResponseBody
    @GetMapping("/total/{year}/{month}")
    public Double getTotalRevenueByMonth(@PathVariable int year, @PathVariable int month) {
        return thongKeService.getTotalRevenueByMonth(year, month);
    }

    // API tính tổng doanh thu theo ngày
    @ResponseBody
    @GetMapping("/total/{year}/{month}/{day}")
    public Double getTotalRevenueByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return thongKeService.getTotalRevenueByDay(year, month, day);
    }

    //  lấy dữ liệu doanh thu theo tháng để vẽ biểu đồ
    @ResponseBody
    @GetMapping("/chart/{year}")
    public Map<String, Double> getRevenueChartByYear(@PathVariable int year) {
        return thongKeService.getRevenueChartByYear(year);
    }

    //  lấy dữ liệu doanh thu theo ngày trong tháng để vẽ biểu đồ
    @ResponseBody
    @GetMapping("/chart/{year}/{month}")
    public Map<String, Double> getRevenueChartByMonth(@PathVariable int year, @PathVariable int month) {
        return thongKeService.getRevenueChartByMonth(year, month);
    }

    //  lấy dữ liệu doanh thu theo giờ trong ngày để vẽ biểu đồ
    @ResponseBody
    @GetMapping("/chart/{year}/{month}/{day}")
    public Map<String, Double> getRevenueChartByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return thongKeService.getRevenueChartByDay(year, month, day);
    }



    // ✅ Trả về trang HTML thống kê sản phẩm yêu thích
    @GetMapping("/sanphamyeuthich")
    public String getMostFavoriteProductsPage(Model model) {
        List<Object[]> favoriteProducts = thongKeService.getMostFavoriteProducts();
        model.addAttribute("favoriteProducts", favoriteProducts);
        return "admin/thongke_SPYT"; // Trả về trang thongke_SPYT.html
    }

    // ✅ API trả về danh sách sản phẩm yêu thích để vẽ biểu đồ
    @ResponseBody
    @GetMapping("/sanphamyeuthich/data")
    public List<Object[]> getMostFavoriteProductsData() {
        return thongKeService.getMostFavoriteProducts();

    }

    @GetMapping("/most-favorite")
    public ResponseEntity<List<Object[]>> getMostFavoriteProducts() {
        List<Object[]> result = thongKeService.getMostFavoriteProducts();
        return ResponseEntity.ok(result);
    }

    // 2. Lấy danh sách sản phẩm yêu thích theo năm
    @GetMapping("/most-favorite/{year}")
    public ResponseEntity<Map<String, Integer>> getMostFavoriteProductsByYear(@PathVariable int year) {
        Map<String, Integer> result = thongKeService.getFavoriteChartByYear(year);
        return ResponseEntity.ok(result);
    }

    // 3. Lấy danh sách sản phẩm yêu thích theo tháng
    @GetMapping("/most-favorite/{year}/{month}")
    public ResponseEntity<Map<String, Integer>> getMostFavoriteProductsByMonth(@PathVariable int year, @PathVariable int month) {
        Map<String, Integer> result = thongKeService.getFavoriteChartByMonth(year, month);
        return ResponseEntity.ok(result);
    }

    // 4. Lấy danh sách sản phẩm yêu thích theo ngày
    @GetMapping("/most-favorite/{year}/{month}/{day}")
    public ResponseEntity<Map<String, Integer>> getMostFavoriteProductsByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        Map<String, Integer> result = thongKeService.getFavoriteChartByDay(year, month, day);
        return ResponseEntity.ok(result);
    }


    // 5. Lấy danh sách tháng có sản phẩm yêu thích trong năm
    @GetMapping("/most-favorite/available-months/{year}")
    public ResponseEntity<List<Integer>> getFavoriteMonthsByYear(@PathVariable int year) {
        List<Integer> result = thongKeService.getFavoriteMonthsByYear(year);
        return ResponseEntity.ok(result);
    }

    // 6. Lấy danh sách ngày có sản phẩm yêu thích trong tháng
    @GetMapping("/most-favorite/available-days/{year}/{month}")
    public ResponseEntity<List<Integer>> getFavoriteDaysByMonth(@PathVariable int year, @PathVariable int month) {
        List<Integer> result = thongKeService.getFavoriteDaysByMonth(year, month);
        return ResponseEntity.ok(result);
    }

    // 7. Lấy danh sách năm có sản phẩm yêu thích
    @GetMapping("/most-favorite/available-years")
    public ResponseEntity<List<Integer>> getAvailableYears() {
        List<Integer> result = thongKeService.getAvailableYears();
        return ResponseEntity.ok(result);
    }


    // ✅ Trả về trang HTML thống kê sản phẩm bán chạy
    @GetMapping("/sanphambanchay")
    public String getBestSellingProductsPage(Model model) {
        List<Object[]> bestSellingProducts = thongKeService.getBestSellingProducts();
        model.addAttribute("bestSellingProducts", bestSellingProducts);
        return "admin/thongke_SPBC"; // Trả về trang thongke_SPBC.html
    }

    // ✅ API trả về danh sách sản phẩm bán chạy để vẽ biểu đồ
    @ResponseBody
    @GetMapping("/sanphambanchay/data")
    public List<Object[]> getBestSellingProductsData() {
        return thongKeService.getBestSellingProducts();
    }

    @GetMapping("/best-selling")
    public ResponseEntity<List<Object[]>> getBestSellingProducts() {
        List<Object[]> result = thongKeService.getBestSellingProducts();
        return ResponseEntity.ok(result);
    }


    // ✅ Trả về danh sách sản phẩm bán chạy theo năm
    @GetMapping("/best-selling/{year}")
    public ResponseEntity<Map<String, Integer>> getBestSellingProductsByYear(@PathVariable int year) {
        return ResponseEntity.ok(thongKeService.getBestSellingProductsByYear(year));
    }

    // ✅ Trả về danh sách sản phẩm bán chạy theo tháng
    @GetMapping("/best-selling/{year}/{month}")
    public ResponseEntity<Map<String, Integer>> getBestSellingProductsByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(thongKeService.getBestSellingProductsByMonth(year, month));
    }

    // ✅ Trả về danh sách sản phẩm bán chạy theo ngày
    @GetMapping("/best-selling/{year}/{month}/{day}")
    public ResponseEntity<Map<String, Integer>> getBestSellingProductsByDay(@PathVariable int year, @PathVariable int month, @PathVariable int day) {
        return ResponseEntity.ok(thongKeService.getBestSellingProductsByDay(year, month, day));
    }

    // ✅ Lấy danh sách tháng có sản phẩm bán chạy trong năm
    @GetMapping("/best-selling/available-months/{year}")
    public ResponseEntity<List<Integer>> getBestSellingMonthsByYear(@PathVariable int year) {
        return ResponseEntity.ok(thongKeService.getBestSellingMonthsByYear(year));
    }

    // ✅ Lấy danh sách ngày có sản phẩm bán chạy trong tháng
    @GetMapping("/best-selling/available-days/{year}/{month}")
    public ResponseEntity<List<Integer>> getBestSellingDaysByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(thongKeService.getBestSellingDaysByMonth(year, month));
    }

    @GetMapping("/best-selling/available-years")
    public ResponseEntity<List<Integer>> getAvailableYears1() {
        List<Integer> result = thongKeService.getAvailableYears();
        return ResponseEntity.ok(result);
    }

}
