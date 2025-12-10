package org.example.java5.repository;

import org.example.java5.entity.Order;
import org.example.java5.entity.OrderDetail;
import org.example.java5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {


    List<Order> findByUserId(int userId);
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
    void updateOrderStatus(@Param("id") int id, @Param("status") int status);
    List<Order> findByUser(User user);
    List<Order> findAllByOrderByOrderDateDesc();

}
