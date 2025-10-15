package com.example.h2_practice.integration;

import com.example.h2_practice.dto.ItemRequestDto;
import com.example.h2_practice.entity.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest // 전체 어플리케이션 컨텍스트 로드
@AutoConfigureMockMvc // MockMvc 자동 구성
@Transactional // 각 테스트 후 롤백 (테스트 간 독립성 보장)
public class ItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // HTTP 요청 시뮬레이션

    @Autowired
    private ObjectMapper objectMapper; // JSON <-> JAva 객체 변환

    @Test
    @DisplayName("등록 -> 조회 -> 삭제 End-to-End 플로우 검증")
    void createReadDelete_Success() throws Exception{
        // 1. 상품 등록 (POST /api/items)
        ItemRequestDto createRequest = new ItemRequestDto("Test Item", "Description", 50000, 10);

        String createResponse = mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.name").value("Test Item")) // 응답 데이터 확인
                .andReturn().getResponse().getContentAsString();

        // 응답에서 생성된 상품의 ID 추출
        Item createItem = objectMapper.readValue(createResponse, Item.class);
        Long itemId = createItem.getId();

        // 2. 생성된 상품 조회 (GET /api/items/{id})
        mockMvc.perform(get("/api/items/" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId)) // ID 일치 확인
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.price").value(50000));

        // 3. 상품 삭제 (DELETE /api/items/{id}
        mockMvc.perform(delete("/api/items/" + itemId))
                .andExpect(status().isNoContent()); // 204 No Content 확인

        // 4. 삭제 후 조회 시도 -> 404 Not Found 확인
        mockMvc.perform(get("/api/items/" + itemId))
                .andExpect(status().isNotFound()); // 404 Not Found 확인

    }


}
