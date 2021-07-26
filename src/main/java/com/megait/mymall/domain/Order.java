package com.megait.mymall.domain;

import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="orders")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Order {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // 주문자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> OrderItems = new ArrayList<>(); // 주문한 상품들

    @OneToOne(fetch = FetchType.LAZY)
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문한 시각

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태

    // TODO 0715 - Order 완성하기

}
