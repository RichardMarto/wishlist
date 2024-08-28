package br.com.labs.wishlist.exceptions;

public class FullWishlistException extends RuntimeException {
    public FullWishlistException(String userId) {
        super("Cannot add to a full list, please remove some items before adding any new ones. The wishlist has a limit of 20 items!");
    }
}
