package com.example.h2_practice.controller;

import com.example.h2_practice.dto.ItemRequestDto;
import com.example.h2_practice.entity.Item;
import com.example.h2_practice.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class) // Controller 계층만 로드
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // JSON <-> java 객체 간 매핑

    @MockitoBean
    private ItemService itemService;

    @Test
    @DisplayName("GET 전체 상품 조회 : JSON 배열 응답")
    void getAllItems_Success() throws Exception{
        // Mock 데이터 준비
        List<Item> mockItems = Arrays.asList(
                Item.builder().id(1L).name("Laptop").price(9990000).stock(10).build(),
                Item.builder().id(2L).name("Mouse").price(29900).stock(50).build());

        given(itemService.getAllItems()).willReturn(mockItems);

        // GET 요청 수행 및 응답 검증
        mockMvc.perform(get("/api/items"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    // GET /api/items/{id} 실패 케이스 1: 상품명이 누락된 경우
    @Test
    @DisplayName("createItem_NameBlank_Return400")
    void createItem_NameBlank_Returns400() throws Exception {
        ItemRequestDto invalidRequest = new ItemRequestDto("", "Desc", 1000, 1);
        String content = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").value("상품명은 필수 입력값입니다."));
        verify(itemService, never()). createItem(any());
    }

    // POST /api/items 실패 케이스 2: 가격이 음수인 경우
    @Test
    @DisplayName("createItem_PriceNegative_Returns400")
    void createItem_PriceNegative_Returns400() throws Exception {
        ItemRequestDto invalidRequest = new ItemRequestDto("Valid Name", "Desc", -1, 10);
        String content = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value("가격은 0원 이상이어야 합니다."));
        verify(itemService, never()).createItem(any());
    }

    // DELETE /api/items/{id} 실패 케이스 3: 없는 상품을 삭제하는 경우
    @Test
    @DisplayName("deleteItemById_NotFound_Returns404")
    void deleteItemById_NotFound_Returns404() throws Exception {
        Long missingId = 100L;
        given(itemService.deleteItem(missingId)).willReturn(false);

        mockMvc.perform(delete("/api/items/{id}", missingId))
                .andExpect(status().isNotFound());
        verify(itemService).deleteItem(missingId);
    }
}