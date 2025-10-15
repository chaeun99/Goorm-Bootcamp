package com.example.h2_practice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    private String name;        // 제품명

    private String description; // 제품 설명

    @NotNull(message = "가격은 필수 입력값이다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Integer price;      // 제품 가격

    @NotNull(message = "재고는 필수 입력값입니다.")
    @Min(value = 0, message = "재고는 0개 이상이어야합니다.")
    private Integer stock;      // 재고
}
