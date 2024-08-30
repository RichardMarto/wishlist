package br.com.labs.wishlist.adapters.http;

import br.com.labs.wishlist.model.dto.WishlistDTO;
import br.com.labs.wishlist.model.exceptions.FullWishlistException;
import br.com.labs.wishlist.ports.input.WishlistServiceInputPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistServiceInputPort wishlistServicePort;

    WishlistController(final WishlistServiceInputPort wishlistService) {
        this.wishlistServicePort = wishlistService;
    }

    @GetMapping
    public WishlistDTO getProducts(@RequestParam final String userId) {
        return wishlistServicePort.get(userId);
    }

    @PutMapping("/products")
    public WishlistDTO addProduct(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistServicePort.addIfNotFull(userId, productId);
    }

    @DeleteMapping("/products")
    public WishlistDTO removeProduct(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistServicePort.remove(userId, productId);
    }


    @GetMapping("/products")
    public Boolean containsProduct(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistServicePort.contains(userId, productId);
    }

    @ExceptionHandler(FullWishlistException.class)
    public ResponseEntity<WishlistDTO> fullWishlist(FullWishlistException exception) {
        return new ResponseEntity<>(wishlistServicePort.get(exception.getUserid()), HttpStatus.BAD_REQUEST);
    }
}