package com.example.JustEat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "favourites",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "restaurant_id"}),
        @UniqueConstraint(columnNames = {"user_id", "menu_item_id"})
    }
)
@Getter
@Setter
public class Favourite extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}

