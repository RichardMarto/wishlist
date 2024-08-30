package br.com.labs.wishlist.wishlist.implementation.integration;

import br.com.labs.wishlist.wishlist.WishlistTest;
import br.com.labs.wishlist.exceptions.FullWishlistException;
import br.com.labs.wishlist.wishlist.implementation.WishlistBaseTest;
import br.com.labs.wishlist.model.Wishlist;
import br.com.labs.wishlist.model.WishlistDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Random;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
public class WishlistIntegrationTest extends WishlistBaseTest implements WishlistTest {
    private static final String host = "http://localhost:";
    private static final String api = "/api/wishlist";
    private static final String USER_ID = "user_id_";
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017);
    private final Random random = new Random();
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
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
    public WishlistDTO add(final String userId, final String productId) {
        ResponseEntity<WishlistDTO> response = this.restTemplate.postForEntity(
                host + port + api + "/add?userId=" + userId + "&productId=" + productId,
                null,
                WishlistDTO.class);

        if (response.getStatusCode().is4xxClientError()) {
            throw new FullWishlistException(userId, productId);
        }

        WishlistDTO wishlistDTO = response.getBody();
        return wishlistDTO;
    }

    @Override
    protected void remove(final String userId, final String productId) {
        this.restTemplate.delete(
                host + port + api + "/remove?userId=" + userId + "&productId=" + productId,
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
                host + port + api + "/contains?userId=" + userId + "&productId=" + productId,
                Boolean.class);
    }

    String getUserId() {
        return USER_ID + Math.abs(random.nextInt());
    }

    @Override
    protected void setup(final String userId, int size) {
        final Wishlist wishlist = build(userId, size);
        wishlist.getProducts().forEach(product -> {
            add(userId, product);
        });
    }
}