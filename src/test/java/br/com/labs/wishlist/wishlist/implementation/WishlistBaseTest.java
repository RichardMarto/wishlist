package br.com.labs.wishlist.wishlist.implementation;

import br.com.labs.wishlist.model.exceptions.FullWishlistException;
import br.com.labs.wishlist.wishlist.factory.WishlistFactory;
import br.com.labs.wishlist.model.entity.Wishlist;
import br.com.labs.wishlist.model.dto.WishlistDTO;
import org.junit.jupiter.api.Assertions;

public abstract class WishlistBaseTest {

    private final WishlistFactory wishlistFactory;

    protected WishlistBaseTest() {
        this.wishlistFactory = new WishlistFactory();
    }

    protected static String getNextProductId(int size) {
        return "product_id_" + (size + 1);
    }

    protected static String getLastProductId(int size) {
        return "product_id_" + (size - 1);
    }

    protected Wishlist build(final String userId, int size) {
        return wishlistFactory.build(userId, size);
    }

    protected abstract WishlistDTO add(final String userId, final String productId);

    protected abstract void remove(final String userId, final String productId);

    protected abstract WishlistDTO get(final String userId);

    protected abstract Boolean contains(final String userId, final String productId);

    protected abstract void setup(final String userId, final int size);

    public void addIfNotFull_withEmptyWishlist(final String userId, final int size) {
        final String nextProductId = getNextProductId(size);
        final Wishlist wishlist = build(userId, size);
        setup(userId, size);

        final WishlistDTO wishlistDTO = add(userId, nextProductId);

        Assertions.assertEquals(wishlist.getUserId(), wishlistDTO.getUserId());
        Assertions.assertTrue(wishlistDTO.getProducts().containsAll(wishlist.getProducts()));
        Assertions.assertTrue(wishlistDTO.getProducts().contains(nextProductId));
    }

    public void addIfNotFull_withAlmostFullWishlist(final String userId, final int size) {
        final int expectedSize = size + 1;
        final String nextProductId = getNextProductId(size);
        final Wishlist wishlist = build(userId, size);
        setup(userId, size);

        final WishlistDTO wishlistDTO = add(userId, nextProductId);

        Assertions.assertEquals(wishlist.getUserId(), wishlistDTO.getUserId());
        Assertions.assertEquals(expectedSize, wishlistDTO.getProducts().size());
        Assertions.assertTrue(wishlistDTO.getProducts().containsAll(wishlist.getProducts()));
        Assertions.assertTrue(wishlistDTO.getProducts().contains(nextProductId));
    }

    public void addIfNotFull_withAFullWishlist(final String userId, final int size) {
        final String nextProductId = getNextProductId(size);
        setup(userId, size);

        Assertions.assertThrows(FullWishlistException.class,
                () -> add(userId, nextProductId)
        );
    }

    public void remove_withFullWishlist(final String userId, final int size) {
        final int expectedSize = size - 1;
        final String nextProductId = getLastProductId(size);
        final Wishlist wishlist = build(userId, size);
        setup(userId, size);

        remove(userId, nextProductId);
        final WishlistDTO wishlistDTO = get(userId);

        Assertions.assertEquals(wishlist.getUserId(), wishlistDTO.getUserId());
        Assertions.assertEquals(expectedSize, wishlistDTO.getProducts().size());
        Assertions.assertTrue(wishlist.getProducts().containsAll(wishlistDTO.getProducts()));
        Assertions.assertFalse(wishlistDTO.getProducts().contains(nextProductId));
    }

    public void remove_withEmptyWishlistList(final String userId, final int size) {
        final String nextProductId = getNextProductId(size);
        final Wishlist wishlist = build(userId, size);
        setup(userId, size);

        remove(userId, nextProductId);
        final WishlistDTO wishlistDTO = get(userId);

        Assertions.assertEquals(wishlist.getUserId(), wishlistDTO.getUserId());
        Assertions.assertEquals(wishlist.getProducts(), wishlistDTO.getProducts());
        Assertions.assertEquals(size, wishlistDTO.getProducts().size());
        Assertions.assertFalse(wishlistDTO.getProducts().contains(nextProductId));
    }

    public void get_withFullWishlist(final String userId, final int size) {
        final Wishlist wishlist = build(userId, size);
        setup(userId, size);

        final WishlistDTO wishlistDTO = get(userId);

        Assertions.assertNotEquals(null, wishlistDTO.getProducts());
        Assertions.assertEquals(size, wishlistDTO.getProducts().size());
        Assertions.assertEquals(wishlist.getUserId(), wishlistDTO.getUserId());
        Assertions.assertEquals(wishlist.getProducts(), wishlistDTO.getProducts());
    }

    public void get_withEmptyWishlist(final String userId, final int size) {
        final Wishlist wishlist = build(userId, size);
        setup(userId, size);

        final WishlistDTO wishlistDTO = get(userId);

        Assertions.assertNotEquals(null, wishlistDTO.getProducts());
        Assertions.assertEquals(size, wishlistDTO.getProducts().size());
        Assertions.assertEquals(wishlist.getUserId(), wishlistDTO.getUserId());
        Assertions.assertEquals(wishlist.getProducts(), wishlistDTO.getProducts());
    }

    public void contains_withFullWishlistAndExistingProductId(final String userId, final int size) {
        setup(userId, size);

        final Boolean contains = contains(userId, getLastProductId(size));

        Assertions.assertTrue(contains);
    }

    public void contains_withFullWishlistAndNonExistingProductId(final String userId, final int size) {
        setup(userId, size);

        final Boolean contains = contains(userId, getNextProductId(size));

        Assertions.assertFalse(contains);
    }

    public void contains_withEmptyWishlist(final String userId, final int size) {
        setup(userId, size);

        final Boolean contains = contains(userId, getNextProductId(size));
        Assertions.assertFalse(contains);
    }
}