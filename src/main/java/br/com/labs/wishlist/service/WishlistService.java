package br.com.labs.wishlist.service;

import br.com.labs.wishlist.exceptions.FullWishlistException;
import br.com.labs.wishlist.model.Wishlist;
import br.com.labs.wishlist.model.WishlistDTO;
import br.com.labs.wishlist.repository.WishlistRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

@Log4j2
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(final WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public static WishlistDTO toDTO(Wishlist wishlist) {
        return WishlistDTO.builder()
                .wishlist(wishlist)
                .build();
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

    private static Wishlist createAnEmptyOne(final String userId) {
        return Wishlist.builder().userId(userId).products(HashSet.newHashSet(0)).build();
    }

    private static Function<Wishlist, Wishlist> remove(final String productId) {
        return wishlist -> wishlist.remove(productId);
    }

    public WishlistDTO addIfNotFull(final String userId, final String productId) {
        log.info("WishlistService.addIfNotFull: {userId: {}, productId: {}}", userId, productId);
        if (isFull(userId)) {
            throw new FullWishlistException(userId, productId);
        } else {
            return add(userId, productId);
        }
    }

    public WishlistDTO remove(final String userId, final String productId) {
        log.info("WishlistService.remove: {userId: {}, productId: {}}", userId, productId);
        final Wishlist wishlist = wishlistRepository.findById(userId)
                .map(remove(productId))
                .orElse(createAnEmptyOne(userId));

        return saveAndMapToDTO(wishlist);
    }

    public WishlistDTO get(final String userId) {
        log.info("WishlistService.get: {userId: {}}", userId);
        return wishlistRepository.findById(userId)
                .map(WishlistService::toDTO)
                .orElseGet(() -> toDTO(createAnEmptyOne(userId)));
    }

    public Boolean contains(final String userId, final String productId) {
        log.info("WishlistService.contains: {userId: {}, productId: {}}", userId, productId);
        return wishlistRepository.findById(userId).map(wishlist -> wishlist.getProducts().contains(productId)).orElse(Boolean.FALSE);
    }

    private Boolean isFull(final String userId) {
        return wishlistRepository.findById(userId).map(wishlist -> wishlist.getProducts().size() >= 20).orElse(Boolean.FALSE);
    }

    private WishlistDTO add(String userId, String productId) {
        final Wishlist wishlist = wishlistRepository.findById(userId)
                .map(addIfNotFull(productId))
                .orElseGet(createThenAdd(userId, productId));

        return saveAndMapToDTO(wishlist);
    }

    private WishlistDTO saveAndMapToDTO(final Wishlist wishlist) {
        return toDTO(wishlistRepository.save(wishlist));
    }

}
