package br.com.labs.wishlist.core.service.usecases;

import br.com.labs.wishlist.model.dto.WishlistDTO;

public interface GetWishlistPort {
    public WishlistDTO get(final String userId);
}
