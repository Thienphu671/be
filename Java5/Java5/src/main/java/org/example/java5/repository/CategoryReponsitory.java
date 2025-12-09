package org.example.java5.repository;

import org.example.java5.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryReponsitory extends JpaRepository<Category, Integer> {


        @Query(value = "SELECT * FROM danhmuc WHERE ten LIKE CONCAT(?1, '%')", nativeQuery = true)
        public List<Category> findByName(String keyword);
        public List<Category> getById(int id);


}
