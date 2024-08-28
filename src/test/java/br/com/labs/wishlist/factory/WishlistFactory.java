package br.com.labs.wishlist.factory;

import br.com.labs.wishlist.exceptions.FullWishlistException;
import br.com.labs.wishlist.model.Wishlist;

import java.util.HashSet;

public class WishlistFactory {

    private static final String PRODUCT_ID_PREFIX = "product_id_";
    private static final Integer PRODUCT_ID_SIZE_BEFORE_THE_NUMBERS = 11;

    public Wishlist build(final String userId, final Integer size) {
        if (size > 20) {
            throw new FullWishlistException(userId);
        } else {
            final HashSet<String> products = new HashSet<String>();
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
