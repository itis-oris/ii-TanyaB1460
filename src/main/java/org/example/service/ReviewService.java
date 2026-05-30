package org.example.service;

import org.example.form.ReviewForm;
import org.example.model.Product;
import org.example.model.Review;
import org.example.model.User;
import org.example.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    @Transactional
    public void addReview(Product product, User author, ReviewForm form) {
        Review review = new Review();
        review.setProduct(product);
        review.setAuthor(author);
        review.setContent(form.getContent());
        reviewRepository.save(review);
    }
}
