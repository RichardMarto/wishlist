package br.com.labs.wishlist.ports.output;

import br.com.labs.wishlist.model.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WishlistPersistencePort extends MongoRepository<Wishlist, String> {
}