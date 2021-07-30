package com.megait.mymall.service;

import com.megait.mymall.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberUser extends User {
    private final Member member;

    public MemberUser(Member member){
        super(
                member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getType().name()))

        );

        this.member = member;
    }
}
