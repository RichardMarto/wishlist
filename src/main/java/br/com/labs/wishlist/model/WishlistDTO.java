package br.com.labs.wishlist.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;

@Data
public class WishlistDTO {
    private final String userId;
    private final HashSet<String> products;

    @Builder
    WishlistDTO(final Wishlist wishlist) {
        this.userId = wishlist.getUserId();
        this.products = wishlist.getProducts();
    }
}
