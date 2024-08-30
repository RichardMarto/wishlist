package br.com.labs.wishlist.wishlist.implementation.unit;

import br.com.labs.wishlist.core.service.usecases.implementation.AddToWishlistUseCase;
import br.com.labs.wishlist.core.service.usecases.implementation.GetWishlistUseCase;
import br.com.labs.wishlist.core.service.usecases.implementation.RemoveFromWishlistUseCase;
import br.com.labs.wishlist.core.service.usecases.implementation.WishlistContainsUseCase;
import br.com.labs.wishlist.core.service.usecases.AddToWishlistPort;
import br.com.labs.wishlist.core.service.usecases.GetWishlistPort;
import br.com.labs.wishlist.core.service.usecases.RemoveFromWishlistPort;
import br.com.labs.wishlist.core.service.usecases.WishlistContainsPort;
import br.com.labs.wishlist.wishlist.WishlistTest;
import br.com.labs.wishlist.model.exceptions.FullWishlistException;
import br.com.labs.wishlist.wishlist.implementation.WishlistBaseTest;
import br.com.labs.wishlist.model.entity.Wishlist;
import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.ports.output.WishlistPersistencePort;
import br.com.labs.wishlist.core.service.WishlistService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
class WishlistServiceUnitTest extends WishlistBaseTest implements WishlistTest {
    protected static final String USER_ID = "user_id_1";

    private final WishlistService wishlistService;
    private final WishlistPersistencePort wishlistPersistencePort;

    WishlistServiceUnitTest() {
        super();
        this.wishlistPersistencePort = Mockito.mock(WishlistPersistencePort.class);
        AddToWishlistPort addToWishlistPort = new AddToWishlistUseCase(wishlistPersistencePort);
        GetWishlistPort getWishlistPort = new GetWishlistUseCase(wishlistPersistencePort);
        RemoveFromWishlistPort removeFromWishlistPort = new RemoveFromWishlistUseCase(wishlistPersistencePort);
        WishlistContainsPort wishlistContainsPort = new WishlistContainsUseCase(wishlistPersistencePort);
        this.wishlistService = new WishlistService(
                addToWishlistPort,
                getWishlistPort,
                removeFromWishlistPort,
                wishlistContainsPort);
    }

    private static Optional<Wishlist> ifContainsReturnsWishlist(Wishlist wishlist, Boolean contains) {
        return contains ? Optional.of(wishlist) : Optional.empty();
    }

    @Test
    public void addIfNotFull_withEmptyWishlist() {
        final int size = 0;
        addIfNotFull_withEmptyWishlist(getUserId(), size);
    }

    @Test
    public void addIfNotFull_withAlmostFullWishlist() {
        final int size = 19;
        addIfNotFull_withAlmostFullWishlist(getUserId(), size);
    }

    @Test
    public void addIfNotFull_withAFullWishlist() {
        final int size = 20;
        addIfNotFull_withAFullWishlist(getUserId(), size);
    }

    @Test
    public void remove_withFullWishlist() {
        final int size = 20;
        remove_withFullWishlist(getUserId(), size);
    }

    @Test
    public void remove_withEmptyWishlistList() {
        final int size = 0;
        remove_withEmptyWishlistList(getUserId(), size);
    }

    @Test
    public void get_withFullWishlist() {
        final int size = 20;
        get_withFullWishlist(getUserId(), size);
    }

    @Test
    public void get_withEmptyWishlist() {
        final int size = 0;
        get_withEmptyWishlist(getUserId(), size);
    }

    @Test
    public void contains_withFullWishlistAndExistingProductId() {
        final int size = 20;
        contains_withFullWishlistAndExistingProductId(getUserId(), size);
    }

    @Test
    public void contains_withFullWishlistAndNonExistingProductId() {
        final int size = 20;
        contains_withFullWishlistAndNonExistingProductId(getUserId(), size);
    }

    @Test
    public void contains_withEmptyWishlist() {
        final int size = 0;
        contains_withEmptyWishlist(getUserId(), size);
    }

    @Override
    protected WishlistDTO add(final String userId, final String productId) {
        return wishlistService.addIfNotFull(userId, productId);
    }

    @Override
    protected void remove(final String userId, final String productId) {
        wishlistService.remove(userId, productId);
    }

    @Override
    protected WishlistDTO get(final String userId) {
        return wishlistService.get(userId);
    }

    @Override
    protected Boolean contains(final String userId, final String productId) {
        return wishlistService.contains(userId, productId);
    }

    String getUserId() {
        return USER_ID;
    }

    @Override
    protected void setup(final String userId, final int size) {
        final Wishlist wishlist = build(userId, size);
        Mockito.when(wishlistPersistencePort.findById(Mockito.anyString())).thenReturn(Optional.of(wishlist));
        Mockito.when(wishlistPersistencePort.save(Mockito.any(Wishlist.class))).thenReturn(wishlist);
        Mockito.when(wishlistPersistencePort.findById(Mockito.anyString())).thenReturn(Optional.of(wishlist));
    }

    @Override
    public void addIfNotFull_withAFullWishlist(final String userid, final int size) {
        final String nextProductId = getNextProductId(size);
        setup(getUserId(), size);

        Assertions.assertThrows(
                FullWishlistException.class,
                () -> add(getUserId(), nextProductId)
        );
    }

}