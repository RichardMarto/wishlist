package br.com.labs.wishlist.core.service.usecases.implementation;

import br.com.labs.wishlist.core.service.usecases.WishlistBaseUseCase;
import br.com.labs.wishlist.core.service.usecases.WishlistContainsPort;
import br.com.labs.wishlist.ports.output.WishlistPersistencePort;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class WishlistContainsUseCase extends WishlistBaseUseCase implements WishlistContainsPort {
    public WishlistContainsUseCase(WishlistPersistencePort wishlistPersistencePort) {
        super(wishlistPersistencePort);
    }

    @Override
    public Boolean contains(final String userId, final String productId) {
        log.info("WishlistService.contains: {userId: {}, productId: {}}", userId, productId);
        return wishlistPersistencePort.findById(userId).map(wishlist -> wishlist.getProducts().contains(productId)).orElse(Boolean.FALSE);
    }
}
