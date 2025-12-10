package org.example.java5.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.java5.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();



        // Bỏ qua xác thực JWT cho các đường dẫn công khai mà không ảnh hưởng đến các chức năng bảo mật (admin)
        if (uri.contains("sanPhamND")
                || uri.startsWith("/api/public/")
                || uri.startsWith("/auth/api/")
                || uri.startsWith("/uploads/")
                || uri.startsWith("/api/admin/sanpham/add")
                || (request.getMethod().equals("GET") && uri.startsWith("/api/products/"))) {
            System.out.println("Bỏ qua filter: " + uri);
            filterChain.doFilter(request, response);
            return;
        }



        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;



        // Kiểm tra header Authorization và lấy token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);  // Lấy token sau "Bearer "
            try {
                // Xử lý và lấy email từ token
                email = jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                // Trả về lỗi khi token không hợp lệ
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ");
                return;
            }
        }

        // Nếu token và email hợp lệ, tiến hành xác thực người dùng
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Nếu userDetails tồn tại, tiến hành xác thực
                if (userDetails != null && jwtUtil.isValidToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Đặt Authentication vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Trường hợp lỗi khi tải userDetails hoặc xác thực
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Không thể xác thực người dùng");
                return;
            }
        }

        // Tiếp tục với chuỗi lọc
        filterChain.doFilter(request, response);
    }


}
