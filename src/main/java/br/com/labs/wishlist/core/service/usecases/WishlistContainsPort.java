package br.com.labs.wishlist.core.service.usecases;

public interface WishlistContainsPort {
    Boolean contains(final String userId, final String productId);
}
