package com.mycompany.electronicstore.repositories;

import com.mycompany.electronicstore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
