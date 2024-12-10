package com.mycompany.electronicstore.repositories;

import com.mycompany.electronicstore.entities.Order;
import com.mycompany.electronicstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
