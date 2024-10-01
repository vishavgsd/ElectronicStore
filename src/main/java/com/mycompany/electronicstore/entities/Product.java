package com.mycompany.electronicstore.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    private String productId;
    private String title;
    @Column(length = 1000)
    private String description;
    private int price;
    private int discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean Stock;
    private String productImageName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Category category;


}
