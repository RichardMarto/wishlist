package br.com.labs.wishlist.exceptions;

public class FullWishlistException extends RuntimeException {
    public FullWishlistException(String userId) {
        super("Cannot add to user's " + userId + " list! It's full, please remove some items before adding any new ones. The wishlist has a limit of 20 items.");
    }
}
