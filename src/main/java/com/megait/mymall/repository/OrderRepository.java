package com.megait.mymall.repository;

import com.megait.mymall.domain.Order;
import com.megait.mymall.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
