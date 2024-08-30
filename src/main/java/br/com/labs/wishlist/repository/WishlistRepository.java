package br.com.labs.wishlist.repository;

import br.com.labs.wishlist.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
}