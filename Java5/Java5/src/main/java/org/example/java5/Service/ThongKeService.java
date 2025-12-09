package org.example.java5.Service;

import org.example.java5.repository.ThongKeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThongKeService {

    @Autowired
    private ThongKeRepository thongKeRepository;

    public List<Integer> getAvailableYears() {
        return thongKeRepository.getAvailableYears();
    }

    public List<Integer> getAvailableMonths(int year) {
        return thongKeRepository.getAvailableMonths(year);
    }

    public List<Integer> getAvailableDays(int year, int month) {
        return thongKeRepository.getAvailableDays(year, month);
    }

    public Double getTotalRevenueByYear(int year) {
        return thongKeRepository.getRevenueByYear(year);
    }

    public Double getTotalRevenueByMonth(int year, int month) {
        return thongKeRepository.getRevenueByMonth(year, month);
    }

    public Double getTotalRevenueByDay(int year, int month, int day) {
        return thongKeRepository.getRevenueByDay(year, month, day);
    }

    // 🔹 Lấy doanh thu từng tháng trong năm để vẽ biểu đồ
    public Map<String, Double> getRevenueChartByYear(int year) {
        Map<String, Double> revenueMap = new LinkedHashMap<>();
        List<Integer> months = getAvailableMonths(year);
        for (int month : months) {
            Double revenue = thongKeRepository.getRevenueByMonth(year, month);
            revenueMap.put("Tháng " + month, revenue != null ? revenue : 0);
        }
        return revenueMap;
    }

    // 🔹 Lấy doanh thu từng ngày trong tháng để vẽ biểu đồ
    public Map<String, Double> getRevenueChartByMonth(int year, int month) {
        Map<String, Double> revenueMap = new LinkedHashMap<>();
        List<Integer> days = getAvailableDays(year, month);
        for (int day : days) {
            Double revenue = thongKeRepository.getRevenueByDay(year, month, day);
            revenueMap.put("Ngày " + day, revenue != null ? revenue : 0);
        }
        return revenueMap;
    }

    // 🔹 Lấy doanh thu từng giờ trong ngày để vẽ biểu đồ
    // 🔹 Lấy tổng doanh thu của một ngày (không phân theo giờ)
    public Map<String, Double> getRevenueChartByDay(int year, int month, int day) {
        Map<String, Double> revenueMap = new LinkedHashMap<>();
        Double revenue = thongKeRepository.getRevenueByDay(year, month, day);
        revenueMap.put("Ngày " + day, revenue != null ? revenue : 0);
        return revenueMap;
    }



    public List<Object[]> getMostFavoriteProducts() {
        return thongKeRepository.getMostFavoriteProducts(); // Gọi repo thay vì gọi chính nó
    }

    // Lấy danh sách tháng có sản phẩm yêu thích trong năm
    public List<Integer> getFavoriteMonthsByYear(int year) {
        return thongKeRepository.getAvailableFavoriteMonths(year);
    }

    // Lấy danh sách ngày có sản phẩm yêu thích trong tháng
    public List<Integer> getFavoriteDaysByMonth(int year, int month) {
        return thongKeRepository.getAvailableFavoriteDays(year, month);
    }

    // Lấy danh sách sản phẩm yêu thích theo năm
    public Map<String, Integer> getFavoriteChartByYear(int year) {
        Map<String, Integer> favoriteMap = new LinkedHashMap<>();
        List<Object[]> favorites = thongKeRepository.getMostFavoriteProductsByYear(year);
        for (Object[] row : favorites) {
            String productName = (String) row[0];
            Integer count = ((Number) row[1]).intValue();
            favoriteMap.put(productName, count);
        }
        return favoriteMap;
    }

    // Lấy danh sách sản phẩm yêu thích theo tháng
    public Map<String, Integer> getFavoriteChartByMonth(int year, int month) {
        Map<String, Integer> favoriteMap = new LinkedHashMap<>();
        List<Object[]> favorites = thongKeRepository.getMostFavoriteProductsByMonth(year, month);
        for (Object[] row : favorites) {
            String productName = (String) row[0];
            Integer count = ((Number) row[1]).intValue();
            favoriteMap.put(productName, count);
        }
        return favoriteMap;
    }

    public Map<String, Integer> getFavoriteChartByDay(int year, int month, int day) {
        Map<String, Integer> favoriteMap = new LinkedHashMap<>();
        List<Object[]> favorites = thongKeRepository.getMostFavoriteProductsByDay(year, month, day);

        for (Object[] row : favorites) {
            String productName = (String) row[0];
            Integer count = ((Number) row[1]).intValue();
            favoriteMap.put(productName, count);
        }
        return favoriteMap;
    }



    public List<Object[]> getBestSellingProducts() {
        return thongKeRepository.getBestSellingProducts();
    }

    // ✅ Lấy danh sách sản phẩm bán chạy theo năm
    public Map<String, Integer> getBestSellingProductsByYear(int year) {
        return thongKeRepository.getBestSellingProductsByYear(year).stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).intValue(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    // ✅ Lấy danh sách sản phẩm bán chạy theo tháng
    public Map<String, Integer> getBestSellingProductsByMonth(int year, int month) {
        return thongKeRepository.getBestSellingProductsByMonth(year, month).stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).intValue(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    // ✅ Lấy danh sách sản phẩm bán chạy theo ngày
    public Map<String, Integer> getBestSellingProductsByDay(int year, int month, int day) {
        return thongKeRepository.getBestSellingProductsByDay(year, month, day).stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).intValue(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    // ✅ Lấy danh sách những tháng có sản phẩm bán chạy trong năm
    public List<Integer> getBestSellingMonthsByYear(int year) {
        return thongKeRepository.getAvailableSellingMonths(year);
    }

    // ✅ Lấy danh sách những ngày có sản phẩm bán chạy trong tháng
    public List<Integer> getBestSellingDaysByMonth(int year, int month) {
        return thongKeRepository.getAvailableSellingDays(year, month);
    }


}
