package br.com.labs.wishlist.repository;

import br.com.labs.wishlist.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    @Query(value = "{ 'userId' : ?0, 'products.productId' : ?1 }", fields = "{ 'products.productId' : 1 }")
    Optional<Wishlist> contains(String userId, String productId);

    @Query(value = "{ 'userId' : ?0 }", fields = "{ 'products.productId' : 1 }", count = true)
    Long count(String userId);
}
