package com.megait.mymall.domain;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Member {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    private String emailCheckToken; // 이메일 검증에 사용할 토큰

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @ManyToMany
    private List<Item> likes = new ArrayList<>(); // 찜한 상품들 // 단방향관계 회원들은 어떤 상품을 찜했는지 알지만(알아야하고)
    // 상품입장에선 몰라도 된다!

    @OneToMany(mappedBy = "member") // Order의 member 필드와 mapping
    private List<Order> orders = new ArrayList<>(); // 주문들(주문내역)

    @Embedded
    private Address address;

    @Transactional // JPARepository가 아닌 다른 메서드에서 DB 변경을 해야한다면
                    // 그 메서드에 @Transactional 선언. (spting-data-jpa)
    public void generateEmailCheckToken(){
        emailCheckToken = UUID.randomUUID().toString();
    }


}
