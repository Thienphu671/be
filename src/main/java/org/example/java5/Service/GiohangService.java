package org.example.java5.Service;

import jakarta.transaction.Transactional;
import org.example.java5.entity.Giohang;
import org.example.java5.entity.Product;
import org.example.java5.entity.User;
import org.example.java5.repository.GiohangRepository;
import org.example.java5.repository.SanPhamRepository;
import org.example.java5.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GiohangService {
    private final GiohangRepository giohangRepository;
    private final UserRepository userRepository;
    private final SanPhamRepository productRepository;

    // Constructor duy nhất - Đảm bảo tất cả các repository được khởi tạo
    public GiohangService(GiohangRepository giohangRepository, UserRepository userRepository, SanPhamRepository productRepository) {
        this.giohangRepository = giohangRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // ✅ Lấy giỏ hàng của tài khoản

    // ✅ Lấy danh sách giỏ hàng theo tài khoản ID
    public List<Giohang> getAllByTaikhoanId(Integer taikhoanId) {
        return giohangRepository.findAllByTaikhoanId(taikhoanId);
    }

    // ✅ Thêm sản phẩm vào giỏ hàng
    @Transactional
    public Giohang themVaoGiohang(int taikhoanId, Long sanphamId, int soLuong) {
        User user = userRepository.findById(taikhoanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        Product product = productRepository.findById(sanphamId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // ✅ Lấy số lượng tồn kho từ sản phẩm thực tế
        int soLuongTonKho = product.getSoluong();

        // ✅ Kiểm tra giỏ hàng có sản phẩm này chưa
        Optional<Giohang> giohangOpt = giohangRepository.findByTaikhoanAndSanpham(user, product);

        int soLuongHienTaiTrongGio = giohangOpt.map(Giohang::getSoLuong).orElse(0);
        int tongSoLuongMuonThem = soLuongHienTaiTrongGio + soLuong;

        // ✅ Kiểm tra nếu tổng số lượng vượt quá số lượng tồn kho
        if (tongSoLuongMuonThem > soLuongTonKho) {
            throw new RuntimeException("Số lượng sản phẩm trong kho không đủ!");
        }

        Giohang giohang;
        if (giohangOpt.isPresent()) {
            // ✅ Nếu sản phẩm đã có trong giỏ -> Cập nhật số lượng
            giohang = giohangOpt.get();
            giohang.setSoLuong(tongSoLuongMuonThem);
        } else {
            // ✅ Nếu sản phẩm chưa có trong giỏ -> Thêm mới
            giohang = new Giohang();
            giohang.setTaikhoan(user);
            giohang.setSanpham(product);
            giohang.setTenSanpham(product.getTen());
            giohang.setSoLuong(soLuong);
        }

        // ✅ Cập nhật tổng tiền
        giohang.setTongTien(product.getGia().multiply(BigDecimal.valueOf(giohang.getSoLuong())));

        return giohangRepository.save(giohang);
    }


    // ✅ Cập nhật số lượng sản phẩm trong giỏ hàng
    @Transactional
    public void capNhatSoLuong(Integer giohangId, int soLuong) {
        // Kiểm tra giỏ hàng có tồn tại không
        Giohang giohang = giohangRepository.findById(giohangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));

        // Tính lại tổng tiền dựa trên số lượng mới
        BigDecimal tongTien = giohang.getSanpham().getGia().multiply(BigDecimal.valueOf(soLuong));

        // Gọi trực tiếp phương thức cập nhật SQL thuần
        giohangRepository.capNhatSoLuong(giohangId, soLuong);

        // Cập nhật tổng tiền cũng bằng SQL thuần (nếu cần)
        giohangRepository.capNhatTongTien(giohangId, tongTien);
    }

    // ✅ Xóa sản phẩm khỏi giỏ hàng
//    @Transactional
//    public void xoaSanPham(Integer giohangId) {
//        giohangRepository.deleteById(giohangId);
//    }

    // ✅ Xóa toàn bộ giỏ hàng của tài khoản
    @Transactional
    public void xoaGiohangTheoTaikhoan(Integer taikhoanId, Integer sanphamId) {
        giohangRepository.xoaSanPhamTrongGiohang(taikhoanId, sanphamId);
    }

    public List<Giohang> getCartItemsByIds(List<Integer> selectedIds) {
        return giohangRepository.findAllById(selectedIds);
    }

    public void removeSelectedItems(List<Integer> selectedIds) {
        giohangRepository.deleteAllById(selectedIds);
    }

    public BigDecimal tinhTongTien(List<Giohang> cartItems) {
        return cartItems.stream()
                .map(Giohang::getTongTien) // Lấy tổng tiền của từng sản phẩm
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Cộng dồn tổng tiền
    }

    public int laySoLuongSanphamTrongGio(int taikhoanId, Long sanphamId) {
        return giohangRepository.findByTaikhoanIdAndSanphamId(taikhoanId, sanphamId)
                .map(Giohang::getSoLuong)
                .orElse(0); // Nếu không có thì trả về 0
    }

}