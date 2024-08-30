package br.com.labs.wishlist.wishlist;

public interface WishlistTest {
    void addIfNotFull_withEmptyWishlist();

    void addIfNotFull_withAlmostFullWishlist();

    void addIfNotFull_withAFullWishlist();

    void remove_withFullWishlist();

    void remove_withEmptyWishlistList();

    void get_withFullWishlist();

    void get_withEmptyWishlist();

    void contains_withFullWishlistAndExistingProductId();

    void contains_withFullWishlistAndNonExistingProductId();

    void contains_withEmptyWishlist();
}