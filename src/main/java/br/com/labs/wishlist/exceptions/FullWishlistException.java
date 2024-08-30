package br.com.labs.wishlist.exceptions;

import lombok.Getter;

@Getter
public class FullWishlistException extends RuntimeException {

    private final String userid;
    private final String productId;

    public FullWishlistException(final String userId, final String productId) {
        super("Cannot add " + productId + " to user's " + userId + " wishlist! It's full, please remove some items before adding any new ones. The wishlist has a limit of 20 items.");
        this.userid = userId;
        this.productId = getProductId();
    }
}
