package org.example.controller;

import jakarta.validation.Valid;
import org.example.form.ProductForm;
import org.example.form.ReviewForm;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.CategoryService;
import org.example.service.ProductService;
import org.example.service.ReviewService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             ReviewService reviewService,
                             UserRepository userRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);

        model.addAttribute("product", product);
        model.addAttribute("reviews", reviewService.getByProduct(product));

        if (!model.containsAttribute("reviewForm")) {
            model.addAttribute("reviewForm", new ReviewForm());
        }

        return "products/product-detail";
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('MAKER')")
    public String myProducts(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("products", productService.getByMaker(getUser(userDetails)));
        return "products/product-list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('MAKER')")
    public String newProductForm(Model model) {
        model.addAttribute("form", new ProductForm());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("isEdit", false);
        return "products/product-form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('MAKER')")
    public String createProduct(@Valid @ModelAttribute("form") ProductForm form,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("isEdit", false);
            return "products/product-form";
        }

        productService.create(form, getUser(userDetails));
        redirectAttributes.addFlashAttribute("success", "Товар успешно создан");
        return "redirect:/products/my";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('MAKER')")
    public String editProductForm(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        User maker = getUser(userDetails);
        Product product = productService.getById(id);

        if (!product.getMaker().getId().equals(maker.getId())) {
            return "redirect:/products/my";
        }

        model.addAttribute("form", productService.toForm(product));
        model.addAttribute("productId", id);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("isEdit", true);
        return "products/product-form";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('MAKER')")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("form") ProductForm form,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("isEdit", true);
            model.addAttribute("productId", id);
            return "products/product-form";
        }

        productService.update(id, form, getUser(userDetails));
        redirectAttributes.addFlashAttribute("success", "Товар обновлён");
        return "redirect:/products/my";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('MAKER')")
    public String deleteProduct(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        productService.delete(id, getUser(userDetails));
        redirectAttributes.addFlashAttribute("success", "Товар удалён");
        return "redirect:/products/my";
    }

    @PostMapping("/{id}/reviews")
    public String addReview(@PathVariable Long id,
                            @Valid @ModelAttribute("reviewForm") ReviewForm form,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("reviewError", "Введите текст отзыва");
            redirectAttributes.addFlashAttribute("reviewForm", form);
            return "redirect:/products/" + id;
        }

        reviewService.addReview(productService.getById(id), getUser(userDetails), form);
        redirectAttributes.addFlashAttribute("success", "Отзыв добавлен");
        return "redirect:/products/" + id;
    }
}