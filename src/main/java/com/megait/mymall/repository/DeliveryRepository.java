package com.megait.mymall.repository;

import com.megait.mymall.domain.Delivery;
import com.megait.mymall.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
