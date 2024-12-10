package com.mycompany.electronicstore.repositories;

import com.mycompany.electronicstore.entities.Cart;
import com.mycompany.electronicstore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUser(User user);
}
