package com.example.h2_practice.service;

import com.example.h2_practice.entity.Item;
import com.example.h2_practice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;

    // 새로운 상품 등록
    public Item createItem(Item item){
        log.info("Creating new item: {}", item.getName());
        return itemRepository.save(item);
    }

    // 모든 상품 조회
    public List<Item> getAllItems(){
        log.info("Fetching all items");
        return itemRepository.findAll();
    }

    // ID로 상품 조회
    public Optional<Item> getItemById(Long id){
        log.info("Searching items by id: {}", id);
        return itemRepository.findById(id);
    }

    // 상품명으로 상품 조회
    @Transactional(readOnly = true)
    public List<Item> searchItemByName(String name){
        return itemRepository.findByNameContainingIgnoreCase(name);
    }

    // 상품 정보 수정
    public Optional<Item> updateItem(Long id, Item updatedItem){
        return itemRepository.findById(id)  // 1. 기존 상품 조회
                .map(getItem -> {     // 2. 존재하면 map 실행
                    // 기존 엔티티 필드들을 새 데이터로 update
                   getItem.setName(updatedItem.getName());
                   getItem.setDescription(updatedItem.getDescription());
                   getItem.setPrice(updatedItem.getPrice());
                   getItem.setStock(updatedItem.getStock());

                   // 3. 업데이트된 엔티티 저장 후 반환
                   return itemRepository.save(getItem);
                });

        // 4. findById가 empty이면 map이 실행되지 않고 empty 반환
    }

    // 상품 삭제
    public boolean deleteItem(Long id){
        log.info("Delete item with id: {}", id);
        if (itemRepository.existsById(id)){
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
