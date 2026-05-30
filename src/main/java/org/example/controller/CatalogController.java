package org.example.controller;

import org.example.model.Product;
import org.example.service.CategoryService;
import org.example.service.CurrencyService;
import org.example.service.ProductService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class CatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CurrencyService currencyService;

    public CatalogController(ProductService productService, CategoryService categoryService, CurrencyService currencyService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.currencyService = currencyService;
    }

    @GetMapping({"/", "/catalog"})
    public String catalog(@RequestParam(required = false) Long categoryId,
                          @RequestParam(required = false) Double minPrice,
                          @RequestParam(required = false) Double maxPrice,
                          @RequestParam(defaultValue = "RUB") String currency,
                          Model model) {

        List<Product> products = productService.searchProducts(categoryId, minPrice, maxPrice);
        Map<String, Double> rates = currencyService.getRates();
        double rate = rates.getOrDefault(currency, 1.0); //курс выбранной валюты

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("currency", currency);
        model.addAttribute("rate", rate);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
        boolean isMaker = isAuthenticated && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MAKER"));
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("isMaker", isMaker);


        return "products/catalog";
    }
}
