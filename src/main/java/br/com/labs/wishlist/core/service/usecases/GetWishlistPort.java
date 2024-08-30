package br.com.labs.wishlist.core.service.usecases;

import br.com.labs.wishlist.model.dto.WishlistDTO;

public interface GetWishlistPort {
    WishlistDTO get(final String userId);
}
