package com.example.h2_practice.controller;

import com.example.h2_practice.entity.Item;
import com.example.h2_practice.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @MockBean deprecation 경고 무시
@SuppressWarnings("deprecation")
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = Item.builder()
                .id(1L).name("MacBook Pro").description("노트북").price(2990000).stock(5)
                .build();
        item2 = Item.builder()
                .id(2L).name("iPad Air").description("태블릿").price(779000).stock(15)
                .build();
    }

    // 1. 전체 상품 조회 테스트
    @Test
    @DisplayName("전체 상품 조회 테스트 (GET /api/items) - 200 OK")
    void getAllItemsTest() throws Exception {
        List<Item> expectedItems = Arrays.asList(item1, item2);
        given(itemService.getAllItems()).willReturn(expectedItems);

        mockMvc.perform(get("/api/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(item1.getId()))
                .andExpect(jsonPath("$.length()").value(expectedItems.size()));
    }

    // 2. 단일 상품 조회 테스트 (존재하는 id)
    @Test
    @DisplayName("단일 상품 조회 테스트 (존재하는 id) - 200 OK")
    void getItemById_Found_Test() throws Exception {
        Long existingId = 1L;
        given(itemService.getItemById(existingId)).willReturn(Optional.of(item1));

        mockMvc.perform(get("/api/items/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingId));
    }

    // 3. 단일 상품 조회 테스트 (존재하지 않는 id)
    @Test
    @DisplayName("단일 상품 조회 테스트 (존재하지 않는 id) - 404 Not Found")
    void getItemById_NotFound_Test() throws Exception {
        Long nonExistingId = 999L;
        given(itemService.getItemById(nonExistingId)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/items/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    // 4. 상품 등록 테스트
    @Test
    @DisplayName("상품 등록 테스트 (POST /api/items) - 201 Created")
    void createItemTest() throws Exception {
        Item newItemRequest = Item.builder()
                .name("Gaming Mouse").description("게이밍 마우스").price(80000).stock(50)
                .build();
        Item createdItemResponse = Item.builder()
                .id(3L).name("Gaming Mouse").description("게이밍 마우스").price(80000).stock(50)
                .build();

        given(itemService.createItem(any(Item.class))).willReturn(createdItemResponse);
        String content = objectMapper.writeValueAsString(newItemRequest);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L));
    }

    // 5. 상품 삭제 테스트 (존재하는 id)
    @Test
    @DisplayName("상품 삭제 테스트 (존재하는 id) - 204 No Content")
    void deleteItem_Success_Test() throws Exception {
        Long existingId = 1L;
        given(itemService.deleteItem(existingId)).willReturn(true);

        mockMvc.perform(delete("/api/items/{id}", existingId))
                .andExpect(status().isNoContent());
    }

    // 6. 상품 삭제 테스트 (존재하지 않는 id)
    @Test
    @DisplayName("상품 삭제 테스트 (존재하지 않는 id) - 404 Not Found")
    void deleteItem_Failure_Test() throws Exception {
        Long nonExistingId = 999L;
        given(itemService.deleteItem(nonExistingId)).willReturn(false);

        mockMvc.perform(delete("/api/items/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }
}