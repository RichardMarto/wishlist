package br.com.labs.wishlist.service;

import br.com.labs.wishlist.exceptions.FullWishlistException;
import br.com.labs.wishlist.model.Wishlist;
import br.com.labs.wishlist.model.WishlistDTO;
import br.com.labs.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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
            final HashSet<String> products = new HashSet<String>();
            products.add(productId);
            return Wishlist.builder().userId(userId).products(products).build();
        };
    }

    private static Function<Wishlist, Wishlist> addIfNotFull(final String productId) {
        return wishlist -> wishlist.add(productId);
    }

    private static Wishlist createAnEmptyOne(final String userId) {
        return Wishlist.builder().userId(userId).build();
    }

    private static Function<Wishlist, Wishlist> remove(final String productId) {
        return wishlist -> wishlist.remove(productId);
    }

    public WishlistDTO addIfNotFull(final String userId, final String productId) {
        if (isFull(userId)) {
            throw new FullWishlistException(userId);
        } else {
            return add(userId, productId);
        }
    }

    public WishlistDTO remove(final String userId, final String productId) {
        final Wishlist wishlist = wishlistRepository.findById(userId)
                .map(remove(productId))
                .orElse(createAnEmptyOne(userId));

        return saveAndMapToDTO(wishlist);
    }

    public List<String> getProducts(final String userId) {
        return wishlistRepository.getProducts(userId);
    }

    public Boolean contains(final String userId, final String productId) {
        return wishlistRepository.contains(userId, productId).isPresent();
    }

    public Boolean isFull(final String userId) {
        return wishlistRepository.count(userId) >= 20;
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
