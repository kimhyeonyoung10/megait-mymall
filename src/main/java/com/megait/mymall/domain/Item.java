package com.megait.mymall.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter
@SuperBuilder
@AllArgsConstructor @NoArgsConstructor
public abstract class Item { // 얘 객체자체를 쓰진 않겠다!
    @Id @GeneratedValue
    private Long id; // 상품 pk

    private String name; // 상품명

    private int price; // 상품가격

    private int stockQuantity; // 재고량

    @ManyToMany
    private List<Category> categories = new ArrayList<>(); // 소속 카테고리리

    private String imageUrl; // 상품 이미지 경로

    private int liked; // 찜하기 수

}
