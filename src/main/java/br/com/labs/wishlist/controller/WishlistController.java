package br.com.labs.wishlist.controller;

import br.com.labs.wishlist.model.WishlistDTO;
import br.com.labs.wishlist.service.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public List<String> getProducts(@RequestParam final String userId) {
        return wishlistService.getProducts(userId);
    }

    @GetMapping("/contains")
    public Boolean contains(@RequestParam final String userId, @RequestParam final String productId) {
        return wishlistService.contains(userId, productId);
    }
}