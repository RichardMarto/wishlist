package br.com.labs.wishlist.core.service.usecases;

import br.com.labs.wishlist.model.dto.WishlistDTO;

public interface AddToWishlistPort {
    WishlistDTO addIfNotFull(final String userId, final String productId);
}
