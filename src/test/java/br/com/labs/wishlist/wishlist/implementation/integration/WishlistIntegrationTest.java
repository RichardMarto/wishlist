package br.com.labs.wishlist.wishlist.implementation.integration;

import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.model.entity.Wishlist;
import br.com.labs.wishlist.model.exceptions.FullWishlistException;
import br.com.labs.wishlist.wishlist.MongoDBIntegrationTest;
import br.com.labs.wishlist.wishlist.WishlistTest;
import br.com.labs.wishlist.wishlist.implementation.WishlistBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Random;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
public class WishlistIntegrationTest extends WishlistBaseTest implements WishlistTest, MongoDBIntegrationTest {
    private static final String host = "http://localhost:";
    private static final String api = "/api/v1/wishlist";
    private static final String USER_ID = "user_id_";

    private final Random random = new Random();
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

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
    public WishlistDTO add(final String userId, final String productId) {
        ResponseEntity<WishlistDTO> response = this.restTemplate.exchange(
                host + port + api + "/products?userId=" + userId + "&productId=" + productId,
                HttpMethod.PUT,
                null,
                WishlistDTO.class);

        if (response.getStatusCode().is4xxClientError()) {
            throw new FullWishlistException(userId, productId);
        }

        return response.getBody();
    }

    @Override
    protected void remove(final String userId, final String productId) {
        this.restTemplate.delete(
                host + port + api + "/products?userId=" + userId + "&productId=" + productId,
                null,
                WishlistDTO.class);
    }

    @Override
    protected WishlistDTO get(final String userId) {
        return this.restTemplate.getForObject(
                host + port + api + "?userId=" + userId,
                WishlistDTO.class);
    }

    @Override
    protected Boolean contains(final String userId, final String productId) {
        return this.restTemplate.getForObject(
                host + port + api + "/products?userId=" + userId + "&productId=" + productId,
                Boolean.class);
    }

    String getUserId() {
        return USER_ID + Math.abs(random.nextInt());
    }

    @Override
    protected void setup(final String userId, int size) {
        final Wishlist wishlist = build(userId, size);
        wishlist.getProducts().forEach(product -> add(userId, product));
    }
}