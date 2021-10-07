package com.megait.mymall.repository;

import com.megait.mymall.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // insert select delete 우리가 직접 만들수 있따!
    // select   WHERE name=? AND price=?
//    Optional<Item> findByNameAndPrice(String name, int price);
//
//    Item getByNameAndPrice(String name, int price);
}
