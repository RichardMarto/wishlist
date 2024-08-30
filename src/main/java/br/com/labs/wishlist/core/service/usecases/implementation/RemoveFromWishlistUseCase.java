package br.com.labs.wishlist.core.service.usecases.implementation;

import br.com.labs.wishlist.core.service.usecases.RemoveFromWishlistPort;
import br.com.labs.wishlist.core.service.usecases.WishlistBaseUseCase;
import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.model.entity.Wishlist;
import br.com.labs.wishlist.ports.output.WishlistPersistencePort;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.function.Function;


@Service
@Log4j2
public class RemoveFromWishlistUseCase extends WishlistBaseUseCase implements RemoveFromWishlistPort {
    public RemoveFromWishlistUseCase(WishlistPersistencePort wishlistPersistencePort) {
        super(wishlistPersistencePort);
    }

    private static Function<Wishlist, Wishlist> remove(final String productId) {
        return wishlist -> wishlist.remove(productId);
    }

    @Override
    public WishlistDTO remove(final String userId, final String productId) {
        log.info("WishlistService.remove: {userId: {}, productId: {}}", userId, productId);
        final Wishlist wishlist = wishlistPersistencePort.findById(userId)
                .map(remove(productId))
                .orElse(createAnEmptyOne(userId));

        return saveAndMapToDTO(wishlist);
    }
}
