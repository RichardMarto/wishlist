package br.com.labs.wishlist.service;

import br.com.labs.wishlist.exceptions.FullWishlistException;
import br.com.labs.wishlist.factory.WishlistFactory;
import br.com.labs.wishlist.model.Wishlist;
import br.com.labs.wishlist.model.WishlistDTO;
import br.com.labs.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class WishlistServiceTest {
    private static final String USER_ID = "user_id_1";

    private final WishlistService wishlistService;
    private final WishlistRepository wishlistRepository;
    private final WishlistFactory wishlistFactory;

    WishlistServiceTest() {
        this.wishlistRepository = Mockito.mock(WishlistRepository.class);
        this.wishlistService = new WishlistService(wishlistRepository);
        this.wishlistFactory = new WishlistFactory();
    }

    private static Optional<Wishlist> ifContainsReturnsWishlist(Wishlist wishlist, Boolean contains) {
        return contains ? Optional.of(wishlist) : Optional.empty();
    }

    private static String getNextProductId(int size) {
        return "product_id_" + (size + 1);
    }

    private static String getLastProductId(int size) {
        return "product_id_" + (size - 1);
    }

    @Test
    void addIfNotFull_withEmptyWishlist() {
        final int size = 0;
        final String nextProductId = getNextProductId(size);
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);
        final WishlistDTO wishlistDTO = WishlistDTO.builder().wishlist(wishlist).build();

        setupForAddIfNotFull(wishlist, size);

        final WishlistDTO foundWishlist = wishlistService.addIfNotFull(USER_ID, nextProductId);
        Assertions.assertEquals(wishlistDTO, foundWishlist);
        final int expectedSize = size + 1;
        Assertions.assertEquals(expectedSize, foundWishlist.getProducts().size());
    }

    @Test
    void addIfNotFull_withAlmostFullWishlist() {
        final int size = 19;
        final String nextProductId = getNextProductId(size);
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);
        final WishlistDTO wishlistDTO = WishlistDTO.builder().wishlist(wishlist).build();

        setupForAddIfNotFull(wishlist, size);

        final WishlistDTO foundWishlist = wishlistService.addIfNotFull(USER_ID, nextProductId);
        Assertions.assertEquals(wishlistDTO, foundWishlist);
        final int expectedSize = size + 1;
        Assertions.assertEquals(expectedSize, foundWishlist.getProducts().size());
    }

    private void setupForAddIfNotFull(final Wishlist wishlist, final long size, final Boolean contains) {
        Mockito.when(wishlistRepository.contains(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(ifContainsReturnsWishlist(wishlist, contains));
        setupForAddIfNotFull(wishlist, size);
    }

    private void setupForAddIfNotFull(final Wishlist wishlist, final long size) {
        Mockito.when(wishlistRepository.findById(Mockito.anyString())).thenReturn(Optional.of(wishlist));
        Mockito.when(wishlistRepository.save(Mockito.any(Wishlist.class))).thenReturn(wishlist);
        Mockito.when(wishlistRepository.getProducts(Mockito.anyString())).thenReturn(wishlist.getProducts().stream().toList());
        Mockito.when(wishlistRepository.count(Mockito.anyString())).thenReturn(size);
    }

    @Test
    void addIfNotFull_withAFullWishlist() {
        final int size = 20;
        final String nextProductId = getNextProductId(size);
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);

        setupForAddIfNotFull(wishlist, size);

        Assertions.assertThrows(FullWishlistException.class, () -> wishlistService.addIfNotFull(USER_ID, nextProductId));
    }

    @Test
    void remove_withFullWishlist() {
        final int size = 20;
        final String nextProductId = getLastProductId(size);
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);
        final WishlistDTO wishlistDTO = WishlistDTO.builder().wishlist(wishlist).build();

        setupForAddIfNotFull(wishlist, size);

        final WishlistDTO foundWishlist = wishlistService.remove(USER_ID, nextProductId);
        Assertions.assertEquals(wishlistDTO, foundWishlist);
        final int expectedSize = size - 1;
        Assertions.assertEquals(expectedSize, foundWishlist.getProducts().size());
    }

    @Test
    void remove_withEmptyWishlistList() {
        final int size = 0;
        final String nextProductId = getNextProductId(size);
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);
        final WishlistDTO wishlistDTO = WishlistDTO.builder().wishlist(wishlist).build();

        setupForAddIfNotFull(wishlist, size);

        final WishlistDTO foundWishlist = wishlistService.remove(USER_ID, nextProductId);
        Assertions.assertEquals(wishlistDTO, foundWishlist);
        Assertions.assertEquals(size, foundWishlist.getProducts().size());
    }

    @Test
    void getProducts_withFullWishlist() {
        final int size = 20;
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);

        setupForAddIfNotFull(wishlist, size);

        final List<String> products = wishlistService.getProducts(USER_ID);
        Assertions.assertEquals(size, products.size());
        Assertions.assertEquals(wishlist.getProducts().stream().toList(), products);
    }

    @Test
    void getProducts_withEmptyWishlist() {
        final int size = 0;
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);

        setupForAddIfNotFull(wishlist, size);

        final List<String> products = wishlistService.getProducts(USER_ID);
        Assertions.assertEquals(size, products.size());
        Assertions.assertEquals(wishlist.getProducts().stream().toList(), products);
    }

    @Test
    void contains_withFullWishlistAndExistingProductId() {
        final int size = 20;
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);

        setupForAddIfNotFull(wishlist, size, true);

        final Boolean contains = wishlistService.contains(USER_ID, getLastProductId(size));
        Assertions.assertTrue(contains);
    }

    @Test
    void contains_withFullWishlistAndNonExistingProductId() {
        final int size = 20;
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);

        setupForAddIfNotFull(wishlist, size, false);

        final Boolean contains = wishlistService.contains(USER_ID, getNextProductId(size));
        Assertions.assertFalse(contains);
    }

    @Test
    void contains_withEmptyWishlist() {
        final int size = 0;
        final Wishlist wishlist = wishlistFactory.build(USER_ID, size);

        setupForAddIfNotFull(wishlist, size, false);

        final Boolean contains = wishlistService.contains(USER_ID, getNextProductId(size));
        Assertions.assertFalse(contains);
    }
}