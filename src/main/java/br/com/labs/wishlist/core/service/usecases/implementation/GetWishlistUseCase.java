package br.com.labs.wishlist.core.service.usecases.implementation;

import br.com.labs.wishlist.core.service.usecases.GetWishlistPort;
import br.com.labs.wishlist.core.service.usecases.WishlistBaseUseCase;
import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.ports.output.WishlistPersistencePort;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class GetWishlistUseCase extends WishlistBaseUseCase implements GetWishlistPort {
    public GetWishlistUseCase(WishlistPersistencePort wishlistPersistencePort) {
        super(wishlistPersistencePort);
    }

    @Override
    public WishlistDTO get(final String userId) {
        log.info("WishlistService.get: {userId: {}}", userId);
        return wishlistPersistencePort.findById(userId)
                .map(GetWishlistUseCase::toDTO)
                .orElseGet(() -> toDTO(createAnEmptyOne(userId)));
    }
}
