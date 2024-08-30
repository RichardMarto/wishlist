package br.com.labs.wishlist.core.service.usecases.implementation;

import br.com.labs.wishlist.core.service.usecases.WishlistBaseUseCase;
import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.model.entity.Wishlist;
import br.com.labs.wishlist.model.exceptions.FullWishlistException;
import br.com.labs.wishlist.core.service.usecases.AddToWishlistPort;
import br.com.labs.wishlist.ports.output.WishlistPersistencePort;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Log4j2
public class AddToWishlistUseCase extends WishlistBaseUseCase implements AddToWishlistPort {
    public AddToWishlistUseCase(WishlistPersistencePort wishlistPersistencePort) {
        super(wishlistPersistencePort);
    }

    private static Supplier<Wishlist> createThenAdd(final String userId, final String productId) {
        return () -> {
            final HashSet<String> products = new HashSet<>();
            products.add(productId);
            return Wishlist.builder().userId(userId).products(products).build();
        };
    }

    private static Function<Wishlist, Wishlist> addIfNotFull(final String productId) {
        return wishlist -> wishlist.add(productId);
    }

    @Override
    public WishlistDTO addIfNotFull(final String userId, final String productId) {
        log.info("WishlistService.addIfNotFull: {userId: {}, productId: {}}", userId, productId);
        if (isFull(userId)) {
            throw new FullWishlistException(userId, productId);
        } else {
            return add(userId, productId);
        }
    }

    private Boolean isFull(final String userId) {
        return wishlistPersistencePort.findById(userId).map(wishlist -> wishlist.getProducts().size() >= 20).orElse(Boolean.FALSE);
    }

    private WishlistDTO add(String userId, String productId) {
        final Wishlist wishlist = wishlistPersistencePort.findById(userId)
                .map(addIfNotFull(productId))
                .orElseGet(createThenAdd(userId, productId));

        return saveAndMapToDTO(wishlist);
    }
}
