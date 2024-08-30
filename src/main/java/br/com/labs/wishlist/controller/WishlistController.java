package br.com.labs.wishlist.controller;

import br.com.labs.wishlist.exceptions.FullWishlistException;
import br.com.labs.wishlist.model.WishlistDTO;
import br.com.labs.wishlist.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    WishlistController(final WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add")
    public WishlistDTO add(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistService.addIfNotFull(userId, productId);
    }

    @DeleteMapping("/remove")
    public WishlistDTO remove(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistService.remove(userId, productId);
    }

    @GetMapping
    public WishlistDTO get(@RequestParam final String userId) {
        return wishlistService.get(userId);
    }

    @GetMapping("/contains")
    public Boolean contains(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistService.contains(userId, productId);
    }

    @ExceptionHandler(FullWishlistException.class)
    public ResponseEntity<WishlistDTO> fullWishlist(FullWishlistException exception) {
        return new ResponseEntity<WishlistDTO>(wishlistService.get(exception.getUserid()), HttpStatus.BAD_REQUEST);
    }
}