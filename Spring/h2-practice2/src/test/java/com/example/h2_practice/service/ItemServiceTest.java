package com.example.h2_practice.service;

import com.example.h2_practice.entity.Item;
import com.example.h2_practice.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * ItemService 단위 테스트
 *
 * 테스트 전략:
 * - @ExtendWith(MockitoExtensio.class) : Mokito 사용 설정 - Mockito 기능 활성화
 * - Repository를 Mock(가짜)으로 처리해서 Service 로직만 테스트
 * - 비즈니스 로직의 분기 처리, 반환값 검증에 집중
 * **/
@ExtendWith(MockitoExtension.class) //Mockito 기능 활성화
public class ItemServiceTest {

    @Mock // Mock 객체 생성  <- 실제 Repository 대신 가짜 객체 사용
    private ItemRepository itemRepository;

    @InjectMocks // Mock 객체들을 주입받는 실제 테스트 대상
    private ItemService itemService;

    @Test
    @DisplayName("createItem: 상품 등록 성공")
    void createItem_Success(){
        // 테스트에 필요한 데이터 준비
        Item inputItem = Item.builder()
                .name("Test Item")
                .price(10000)
                .stock(5)
                .build();

        Item savedItem = Item.builder()
                .id(1L) // DB에서 자동 생성된 ID라고 가정
                .name("Test Item")
                .price(10000)
                .stock(5)
                .build();

        // Mock Repository의 동작 정의
        // "save 메서드가 호출되면 savedItem을 반환하라"
        given(itemRepository.save(any(Item.class))).willReturn(savedItem);

        // 실제 테스트 대상 메서드 호출
        Item result = itemService.createItem(inputItem);

        // 결과 검증
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L); // ID가 자동 생성되었는지 확인
        assertThat(result.getName()).isEqualTo("Test Item");

        // 상호작용 검증 : Repository.save()가 정확히 1번 호출되었는지 확인
        then(itemRepository).should(times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("deleteItem: 존재하는 상품 삭제 로직 테스트")
    void deleteItem_whenExists(){
        // 존재하는 상품 삭제 -> true 반환
        Long id = 1L;
        given(itemRepository.existsById(id)).willReturn(true);

        boolean result1 = itemService.deleteItem(id);

        assertThat(result1).isTrue();
        then(itemRepository).should(times(1)).existsById(id);
        then(itemRepository).should(times(1)).deleteById(id);;
    }

    @Test
    @DisplayName("deleteItem: 존재하지 않는 상품 삭제 로직 테스트")
    void deleteItem_whenNotExists(){
        // 존재하지 않는 상품 삭제 -> false 반환
        Long id = 999L;
        given(itemRepository.existsById(id)).willReturn(false);

        boolean result2 = itemService.deleteItem(id);

        assertThat(result2).isFalse();
        then(itemRepository).should(times(1)).existsById(id);
        // deleteById 호출되지 않아야 함
        then(itemRepository).should(times(0)).deleteById(id);
    }

    @Test
    @DisplayName("updateItem: Optional 처리 로직 테스트")
    void updateItem_Success() {
        // 기존 상품 데이터
        Item existingItem = Item.builder()
                .id(1L)
                .name("Old Name")
                .price(10000)
                .stock(5)
                .build();

        // 업데이트할 상품 데이터
        Item updateItem = Item.builder()
                .name("New Name")
                .price(2000)
                .stock(10)
                .build();

        // 업데이트 이후 repository에서 리턴해주는 상품 데이터 (ID값 포함된)
        Item updateedItem = Item.builder()
                .id(1L)
                .name("New Name")
                .price(2000)
                .stock(10)
                .build();

        given(itemRepository.findById(1L)).willReturn(Optional.of(existingItem));
        given(itemRepository.save(any(Item.class))).willReturn(updateedItem);

        Optional<Item> result = itemService.updateItem(1L, updateItem);

        assertThat(result).isPresent(); // Optional에 값이 있는지 확인
        assertThat(result.get().getName()).isEqualTo("New Name");
        assertThat(result.get().getPrice()).isEqualTo(2000);

        // Repository 메서드들이 1번씩 호출되었는지 검증
        then(itemRepository).should(times(1)).findById(1L);
        then(itemRepository).should(times(1)).save(any(Item.class));
    }

}
