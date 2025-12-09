package org.example.java5.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "dGhpcyBpcyBhIHNlY3JldCBrZXkgaXMgdmVyeSBzZWN1cmUgYW5kIGxvbmc=";
    private final Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Tạo token từ email
    public String generateToken(String email, Boolean isAdmin) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", isAdmin ? "ROLE_ADMIN" : "ROLE_USER")  // Thêm quyền vào token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 ngày
                .signWith(secretKey)
                .compact();
    }

    // Trích xuất email từ token
    public String extractEmail(String token) {
        try {
            token = cleanToken(token);  // Làm sạch token nếu có "Bearer "
            Claims claims = extractAllClaims(token);  // Trích xuất claims từ token
            return claims.getSubject();  // Lấy email từ subject của claims
        } catch (Exception e) {
            throw new RuntimeException("Token không hợp lệ: " + e.getMessage());
        }
    }

    // Làm sạch token nếu có "Bearer "
    public String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);  // Loại bỏ "Bearer " khỏi token
        }
        return token;  // Nếu không có "Bearer ", trả lại token nguyên vẹn
    }

    // Trích xuất tất cả claims từ token
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)  // Đảm bảo key dùng để xác thực đúng
                    .parseClaimsJws(token)
                    .getBody();  // Trả về các claims trong token
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token không hợp lệ: " + e.getMessage());
        }
    }

    // Trích xuất ngày hết hạn từ token
    public Date extractExpiration(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration();  // Trả về ngày hết hạn từ claims
        } catch (Exception e) {
            throw new RuntimeException("Không thể lấy ngày hết hạn từ token: " + e.getMessage());
        }
    }

    // Kiểm tra xem token có hết hạn hay không
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // Kiểm tra nếu ngày hết hạn trước thời gian hiện tại
    }

    // Kiểm tra tính hợp lệ của token


}
