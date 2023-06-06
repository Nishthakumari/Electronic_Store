package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private String cart_id;
    private Date createdAt;
    private User user;
    private List<CartItemDto> items = new ArrayList<>();
}
