package br.com.labs.wishlist.ports.input;

import br.com.labs.wishlist.model.dto.WishlistDTO;

public interface WishlistServiceInputPort {
    WishlistDTO addIfNotFull(final String userId, final String productId);

    WishlistDTO remove(final String userId, final String productId);

    WishlistDTO get(final String userId);

    Boolean contains(final String userId, final String productId);
}
