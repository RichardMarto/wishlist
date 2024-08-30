package br.com.labs.wishlist.controller;

import br.com.labs.wishlist.exceptions.FullWishlistException;
import br.com.labs.wishlist.model.WishlistDTO;
import br.com.labs.wishlist.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    WishlistController(final WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public WishlistDTO getProducts(@RequestParam final String userId) {
        return wishlistService.get(userId);
    }

    @PutMapping("/products")
    public WishlistDTO addProduct(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistService.addIfNotFull(userId, productId);
    }

    @DeleteMapping("/products")
    public WishlistDTO removeProduct(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistService.remove(userId, productId);
    }


    @GetMapping("/products")
    public Boolean containsProduct(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistService.contains(userId, productId);
    }

    @ExceptionHandler(FullWishlistException.class)
    public ResponseEntity<WishlistDTO> fullWishlist(FullWishlistException exception) {
        return new ResponseEntity<>(wishlistService.get(exception.getUserid()), HttpStatus.BAD_REQUEST);
    }
}