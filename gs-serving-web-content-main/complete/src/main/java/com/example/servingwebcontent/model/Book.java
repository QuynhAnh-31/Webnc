package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề là bắt buộc")
    private String title;

    @NotBlank(message = "Tác giả là bắt buộc")
    private String author;

    @NotBlank(message = "Thể loại là bắt buộc")
    private String category;

    private boolean available = true;
}