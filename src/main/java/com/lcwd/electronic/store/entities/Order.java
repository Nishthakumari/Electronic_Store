package com.lcwd.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    //PENDING, DISPATCHED, DELIVERED
    //enum
    private String orderStatus;

    //NOT-PAID, PAID
    //enum
    //boolean false-> NOT PAID true-PAID
    private String paymentStatus;

    private int orderAmount;

    @Column(length = 100)
    private String billingAddress;

    private String billingPhone;

    private String billingName;

    private Date orderedDate;

    private Date deliveredDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


}
