package com.megait.mymall.repository;

import com.megait.mymall.domain.Book;
import com.megait.mymall.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface
BookRepository extends JpaRepository<Book, Long> {
}
