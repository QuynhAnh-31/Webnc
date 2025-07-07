package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tên sách không được để trống")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    @Column(name = "author", nullable = false)
    private String author;

    @NotBlank(message = "Thể loại không được để trống")
    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "available", nullable = false)
    private boolean available = true;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}