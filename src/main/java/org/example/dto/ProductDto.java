package org.example.dto;

import org.example.model.Product;
import org.example.model.Tag;

import java.io.Serializable;
import java.util.List;

public class ProductDto implements Serializable {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private String categoryName;
    private String makerUsername;
    private List<String> tags;

    public ProductDto() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMakerUsername() {
        return makerUsername;
    }
    public void setMakerUsername(String makerUsername) {
        this.makerUsername = makerUsername;
    }

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ProductDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.categoryName = product.getCategory() != null ? product.getCategory().getName() : null;
        this.makerUsername = product.getMaker() != null ? product.getMaker().getUsername() : null;
        this.tags = product.getTags().stream().map(Tag::getName).toList();
    }
}