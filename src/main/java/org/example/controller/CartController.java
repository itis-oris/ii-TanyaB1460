package org.example.controller;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public CartController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    @GetMapping
    public String cartPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getUser(userDetails);
        model.addAttribute("cart", orderService.getCart(user));
        model.addAttribute("total", orderService.getCartTotal(user));
        model.addAttribute("orders", orderService.getOrderHistory(user));
        return "orders/cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam Long productId,
                                       @RequestParam(defaultValue = "1") int quantity,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            orderService.addToCart(getUser(userDetails), productId, quantity);
            return ResponseEntity.ok(Map.of("success", true, "message", "Товар добавлен в корзину"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(@RequestParam Long orderId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = getUser(userDetails);
            orderService.removeFromCart(user, orderId);
            return ResponseEntity.ok(Map.of("success", true, "total", orderService.getCartTotal(user)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        try {
            orderService.checkout(getUser(userDetails));
            redirectAttributes.addFlashAttribute("success", "Заказ успешно оформлен!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }
}
