package com.example.servingwebcontent.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Họ tên không được để trống")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Vai trò không được để trống")
    @Column(name = "role", nullable = false)
    private String role; // "student" hoặc "admin"

    @Column(name = "phone")
    private String phone;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "class_name")
    private String className;
}