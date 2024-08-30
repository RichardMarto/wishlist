package br.com.labs.wishlist.core.service.usecases;

import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.model.entity.Wishlist;
import br.com.labs.wishlist.ports.output.WishlistPersistencePort;

import java.util.HashSet;

public class WishlistBaseUseCase {

    protected final WishlistPersistencePort wishlistPersistencePort;

    public WishlistBaseUseCase(final WishlistPersistencePort wishlistPersistencePort) {
        this.wishlistPersistencePort = wishlistPersistencePort;
    }

    protected static WishlistDTO toDTO(Wishlist wishlist) {
        return WishlistDTO.builder()
                .wishlist(wishlist)
                .build();
    }

    protected static Wishlist createAnEmptyOne(final String userId) {
        return Wishlist.builder().userId(userId).products(HashSet.newHashSet(0)).build();
    }

    protected WishlistDTO saveAndMapToDTO(final Wishlist wishlist) {
        return toDTO(wishlistPersistencePort.save(wishlist));
    }
}
