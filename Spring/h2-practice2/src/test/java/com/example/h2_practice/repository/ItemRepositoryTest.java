package com.example.h2_practice.repository;

import com.example.h2_practice.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryTest {

    // 테스트 대상 Reposotiry
    @Autowired
    private ItemRepository itemRepository;

    // 테스트용 EntityManager
    @Autowired
    private TestEntityManager entityManager;

    // 각 테스트 실행 전 공통 테스트 데이터 준비
    // @BeforeEach : 각 @Test 메서드 실행 전마다 호출
    // persistAndFlush() : 엔티티를 DB에 저장하고 즉시 flush
    @BeforeEach
    void setup(){
        // 다양한 검색 테스트를 위한 테스트 데이터 준비
        Item laptop = Item.builder().name("Gaming Laptop").price(1500000).stock(5).build();
        Item mouse = Item.builder().name("Wireless Mouse").price(45000).stock(30).build();
        Item keyboard = Item.builder().name("mechanical keyboard").price(120000).stock(15).build();

        // 테스트 데이터를 DB에 저장
        entityManager.persistAndFlush(laptop);
        entityManager.persistAndFlush(mouse);
        entityManager.persistAndFlush(keyboard);
    }

    @Test
    @DisplayName("기본 save / findAll : CRUD 기본 동작 확인")
    void basicCrud_Success(){
        Item newItem = Item.builder()
                .name("USB Hub")
                .price(25000)
                .stock(20)
                .build();
        Item saved = itemRepository.save(newItem);

        // 저장 결과 검증
        assertThat(saved.getId()).isNotNull(); // ID 자동 생성 확인
        assertThat(saved.getName()).isEqualTo("USB Hub");

        // setup()에서 기본 3개 데이터 + 개로 1개 추가 = 4
        assertThat(itemRepository.findAll()).hasSize(4);
    }

    @Test
    @DisplayName("대소문자 무시 검색: 가장 복잡한 쿼리 메서드")
    void  findByNameContainingIgnoreCase_Success(){
        // 대문자로 "MOUSE" 검색
        List<Item> results = itemRepository.findByNameContainingIgnoreCase("MOUSE");

        // 대소문자 구분 없이 "Wireless Mouse" 찾아야 함. 검증
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Wireless Mouse");

        // 추가 테스트: 대문자로 "KEYBOARD" 검색
        List<Item> results2 = itemRepository.findByNameContainingIgnoreCase("KEYBOARD");
        assertThat(results2).hasSize(1);
        assertThat(results2.get(0).getName()).contains("keyboard");
    }

    @Test
    @DisplayName("가격 범위 검색 : 비즈니스 로직 핵심")
    void findByPriceBetween_Success(){
        // 40_000원 ~ 130_000원 범위로 검색
        List<Item> results = itemRepository.findByPriceBetween(40_000, 130_000);

        // mouse(45_000원), keyboard(120_000원) 2개 상품 반환
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name").containsExactlyInAnyOrder("Wireless Mouse", "mechanical keyboard");

        // 결과가 가격 범위 내에 있는지 검증
        assertThat(results).allMatch(item -> item.getPrice() >= 40_000 && item.getPrice() <= 130_000);
    }
}
