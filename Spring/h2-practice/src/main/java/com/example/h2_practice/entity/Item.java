package com.example.h2_practice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 이 클래스가 JPA 엔티티임을 선언(DB table과 매핑)
@Table(name="items") // 매핑될 DB 테이블 정보 지정(items 테이블과 매핑)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // DB의 AUTO_INCREMENT기능 사용 기본 키값 자동 생성
    private Long id;            // 제품 id

    @Column(nullable = false, length = 100)
    private String name;        // 제품명

    @Column(length = 500)
    private String description; // 제품 설명

    @Column(nullable = false)
    private Integer price;      // 제품 가격

    @Column(nullable = false)
    private Integer stock;      // 재고
}
