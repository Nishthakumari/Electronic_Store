package com.lcwd.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;
}
