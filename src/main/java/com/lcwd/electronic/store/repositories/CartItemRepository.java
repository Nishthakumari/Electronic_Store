package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {


    Optional<CartItem> findById(int cartItemId);

}
