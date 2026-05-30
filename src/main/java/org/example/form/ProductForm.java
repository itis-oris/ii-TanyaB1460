package org.example.form;

import jakarta.validation.constraints.*;
import org.example.model.Product;
import org.example.model.Tag;

import java.util.stream.Collectors;

public class ProductForm {

    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 2, max = 100, message = "Название: от 2 до 100 символов")
    private String title;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 10, max = 1000, message = "Описание: от 10 до 1000 символов")
    private String description;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть больше 0")
    @DecimalMax(value = "999999.99", message = "Цена слишком большая")
    private Double price;

    @NotNull(message = "Выберите категорию")
    private Long categoryId;

    public ProductForm() {}

    private String tags;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }

    public ProductForm(Product product) {
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        if (product.getTags() != null && !product.getTags().isEmpty()) {
            this.tags = product.getTags().stream()
                    .map(Tag::getName).collect(Collectors.joining(", "));
        }
    }

}
