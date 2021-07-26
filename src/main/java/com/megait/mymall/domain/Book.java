package com.megait.mymall.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("bk")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends Item{
    private String isbn; // 도서 고유 번호
}
