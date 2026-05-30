package org.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReviewForm {

    @NotBlank(message = "Введите текст отзыва")
    @Size(max = 1000, message = "Отзыв не должен быть длиннее 1000 символов")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}