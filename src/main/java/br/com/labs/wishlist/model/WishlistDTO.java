package br.com.labs.wishlist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDTO {
    private String userId;
    private HashSet<String> products;

    @Builder
    WishlistDTO(final Wishlist wishlist) {
        this.userId = wishlist.getUserId();
        this.products = wishlist.getProducts();
    }
}
