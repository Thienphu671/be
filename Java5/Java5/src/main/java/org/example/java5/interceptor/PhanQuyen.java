package org.example.java5.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class PhanQuyen implements HandlerInterceptor, WebMvcConfigurer {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        Integer taikhoanid = (Integer) session.getAttribute("taikhoanid");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin"); // Lấy quyền admin từ session

        if (loggedInUserEmail == null || taikhoanid == null) {
            response.sendRedirect("/auth/login"); // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
            return false;
        }

        // Chặn người dùng không phải admin truy cập các trang admin
        if (request.getRequestURI().startsWith("/admin") && (isAdmin == null || !isAdmin)) {
            response.sendRedirect("/auth/login"); // Chuyển hướng về trang chính
            return false;
        }

        return true;
    }

    // Đăng ký Interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .addPathPatterns("/doiMatKhau/**", "/sanPham/**", "/gioHang/**", "/thongtin","/favorites","/admin/sanpham","/categories","/loai","/list/users","/donHang/form","/admin/**")
                .excludePathPatterns("/login", "/DangKy", "/css/**", "/js/**","/quenmatkhau");
    }



}
