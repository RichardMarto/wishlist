package br.com.labs.wishlist.core.service.usecases;

import br.com.labs.wishlist.model.dto.WishlistDTO;

public interface RemoveFromWishlistPort {
    WishlistDTO remove(final String userId, final String productId);
}
