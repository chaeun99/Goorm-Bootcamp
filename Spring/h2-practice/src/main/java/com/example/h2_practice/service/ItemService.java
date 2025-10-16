package com.example.h2_practice.service;

import com.example.h2_practice.entity.Item;
import com.example.h2_practice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
