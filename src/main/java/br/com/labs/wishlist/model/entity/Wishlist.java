package br.com.labs.wishlist.model.entity;

import br.com.labs.wishlist.model.dto.WishlistDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Wishlist {
    @Id
    private String userId;

    private HashSet<String> products;

    @Builder
    Wishlist(final WishlistDTO wishlistDTO) {
        this.userId = wishlistDTO.getUserId();
        this.products = wishlistDTO.getProducts();
    }

    public Wishlist add(String productId) {
        products.add(productId);
        return this;
    }

    public Wishlist remove(String productId) {
        products.remove(productId);
        return this;
    }
}
