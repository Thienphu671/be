package org.example.java5.repository;

import org.example.java5.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrderId(Integer orderId);
    List<OrderDetail> findByOrderId(int orderId);
    public  Optional<OrderDetail> findById(Integer id); // ID là chitietdonhang

}
