package com.megait.mymall.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("al")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Album extends Item{
    private String title;
    private String artist;
}
