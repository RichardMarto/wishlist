package br.com.labs.wishlist.wishlist.factory;

import br.com.labs.wishlist.model.exceptions.FullWishlistException;
import br.com.labs.wishlist.model.entity.Wishlist;

import java.util.HashSet;

public class WishlistFactory {

    private static final String PRODUCT_ID_PREFIX = "product_id_";
    private static final Integer PRODUCT_ID_SIZE_BEFORE_THE_NUMBERS = 11;
    private static final Integer MAX_SIZE = 20;

    public Wishlist build(final String userId, final long size) {
        if (size > 20) {
            throw new FullWishlistException(userId, PRODUCT_ID_PREFIX + MAX_SIZE);
        } else {
            final HashSet<String> products = new HashSet<>();
            final StringBuilder productIdBuilder = new StringBuilder();
            productIdBuilder.append(PRODUCT_ID_PREFIX);
            for (int i = 0; i < size; i++) {
                final Integer validId = i + 1;
                productIdBuilder.append(validId);
                products.add(productIdBuilder.toString());
                productIdBuilder.delete(PRODUCT_ID_SIZE_BEFORE_THE_NUMBERS, productIdBuilder.length());
            }
            return Wishlist.builder()
                    .userId(userId)
                    .products(products)
                    .build();
        }
    }
}
