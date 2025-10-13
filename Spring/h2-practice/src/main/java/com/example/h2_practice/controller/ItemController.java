package com.example.h2_practice.controller;

import com.example.h2_practice.entity.Item;
import com.example.h2_practice.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    // 새로운 상품 등록
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item){
        log.info("POST /api/items - Creating new item: {}", item.getName());
        Item createdItem = itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    // 모든 상품 조회
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems(){
        log.info("GET /api/items - Fetching all items");
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    // 특정 상품 조회 (id로 상품 조회)
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id){
        log.info("GET /api/items/{} - Fetching item by id", id);
        return itemService.getItemById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 상품 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        log.info("DELETE /api/items/{} - Deleing item", id);
        boolean deleted = itemService.deleteItem(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
