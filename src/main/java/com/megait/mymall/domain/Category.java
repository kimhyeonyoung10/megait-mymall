package com.megait.mymall.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Category {

    @Id @GeneratedValue
    private Long id; // 카테고리 pk

    private String name; // 카테고리 이름

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent; // 이 카테고리의 상위 카테고리(대분류) 이 카테고리는 소분류

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>(); // 이 카테고리의 하위 카테고리

    @ManyToMany
    private List<Item> items = new ArrayList<>(); // 이 카테고리에 속한 상품들

    public Category addChildCategory(Category child){
        if(child == null){
            return this;
        }
        if(children == null){
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);

        return this;
    }

}
