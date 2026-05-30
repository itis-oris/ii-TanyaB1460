package org.example.service;

import org.example.dto.ProductDto;
import org.example.form.ProductForm;
import org.example.model.*;
import org.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public List<Product> getAllActive() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getByMaker(User maker) {
        return productRepository.findByMakerAndActiveTrue(maker);
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден: " + id));
    }

    public List<Product> searchProducts(Long categoryId, Double minPrice, Double maxPrice) {
        return productRepository.findByFilter(categoryId, minPrice, maxPrice);
    }

    public ProductForm toForm(Product product) {
        return new ProductForm(product);
    }

    @Transactional
    public Product create(ProductForm form, User maker) {
        Product product = new Product();
        fillProduct(product, form);
        product.setMaker(maker);
        log.info("Создание товара: {}", form.getTitle());
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, ProductForm form, User maker) {
        Product product = getById(id);
        if (!product.getMaker().getId().equals(maker.getId()))
            throw new SecurityException("Нет прав редактировать этот товар");
        fillProduct(product, form);
        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id, User maker) {
        Product product = getById(id);
        if (!product.getMaker().getId().equals(maker.getId()))
            throw new SecurityException("Нет прав удалить этот товар");
        product.setActive(false);
        productRepository.save(product);
    }

    private void fillProduct(Product product, ProductForm form) {
        product.setTitle(form.getTitle());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setActive(true);
        product.setCategory(categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категория не найдена")));

        if (form.getTags() != null && !form.getTags().isBlank()) {
            Set<Tag> tags = new HashSet<>();
            for (String name : form.getTags().split(",")) {
                tags.add(findOrCreateTag(name.trim()));
            }
            product.setTags(tags);
        }
    }

    private Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(new Tag(name)));
    }
}