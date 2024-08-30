package br.com.labs.wishlist.core.service;

import br.com.labs.wishlist.core.service.usecases.AddToWishlistPort;
import br.com.labs.wishlist.core.service.usecases.GetWishlistPort;
import br.com.labs.wishlist.core.service.usecases.RemoveFromWishlistPort;
import br.com.labs.wishlist.core.service.usecases.WishlistContainsPort;
import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.ports.input.WishlistServiceInputPort;
import org.springframework.stereotype.Service;

@Service
public class WishlistService implements WishlistServiceInputPort {
    private final AddToWishlistPort addToWishlistPort;
    private final GetWishlistPort getWishlistPort;
    private final RemoveFromWishlistPort removeFromWishlistPort;
    private final WishlistContainsPort wishlistContainsPort;

    public WishlistService(
            final AddToWishlistPort addToWishlistPort,
            final GetWishlistPort getWishlistPort,
            final RemoveFromWishlistPort removeFromWishlistPort,
            final WishlistContainsPort wishlistContainsPort) {
        this.addToWishlistPort = addToWishlistPort;
        this.getWishlistPort = getWishlistPort;
        this.removeFromWishlistPort = removeFromWishlistPort;
        this.wishlistContainsPort = wishlistContainsPort;
    }

    @Override
    public WishlistDTO addIfNotFull(String userId, String productId) {
        return this.addToWishlistPort.addIfNotFull(userId, productId);
    }

    @Override
    public WishlistDTO remove(String userId, String productId) {
        return this.removeFromWishlistPort.remove(userId, productId);
    }

    @Override
    public WishlistDTO get(String userId) {
        return this.getWishlistPort.get(userId);
    }

    @Override
    public Boolean contains(String userId, String productId) {
        return this.wishlistContainsPort.contains(userId, productId);
    }
}
