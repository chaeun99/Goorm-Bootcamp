package com.example.h2_practice.controller;

import com.example.h2_practice.dto.ItemRequestDto;
import com.example.h2_practice.entity.Item;
import com.example.h2_practice.service.ItemService;
import jakarta.validation.Valid;
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

    // 새로운 상품 등록 : DTO 적용
    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequestDto itemDto){
        log.info("POST /api/items - Creating new item: {}", itemDto.getName());
//        Item createdItem = itemService.createItem(item);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);

        Item newItem = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .price(itemDto.getPrice())
                .stock(itemDto.getStock())
                .build();
        Item createdItem = itemService.createItem(newItem);
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

    // 검색 API 추가
    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItemByName(@RequestParam String name){
        List<Item> items = itemService.searchItemByName(name);
        return ResponseEntity.ok(items);
    }

    // 상품 정보 수정
    @PutMapping("{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequestDto itemDto){
        log.info("PUT /api/items/{} - Updating item: {}", id, itemDto.getName());

        // DTO -> Entity 변환
        Item updateItem = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .price(itemDto.getPrice())
                .stock(itemDto.getStock())
                .build();

        // Service 메서드에 Entity 넣어서 호출
        // Service의 Oprional 반환값ㅇ르 ResponseEntity로 변환해서 반환
        return itemService.updateItem(id, updateItem)
                .map(ResponseEntity::ok) // 존재하면 200 OK
                .orElse(ResponseEntity.notFound().build()); // 없으면 404 Not Found
    }

    // 상품 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        log.info("DELETE /api/items/{} - Deleing item", id);
        boolean deleted = itemService.deleteItem(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
