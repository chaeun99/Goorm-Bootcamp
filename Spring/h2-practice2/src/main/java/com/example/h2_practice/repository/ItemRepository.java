package com.example.h2_practice.repository;

import com.example.h2_practice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // name필드로 검색. 내가 검색하고자하는 키워드가 포함만 되어 있어도 되고, 대소문자 무시
    List<Item> findByNameContainingIgnoreCase(String name);
    List<Item> findByPriceBetween(Integer minPrice, Integer maxPrice);
    List<Item> findByStockGreaterThan(Integer stock);
}
