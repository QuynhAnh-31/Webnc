package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Hiển thị danh sách người dùng
    @GetMapping
    public String listUsers(Model model, @RequestParam(name = "search", required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("users", userService.searchUsers(search));
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }
        return "users/list";
    }

    // Hiển thị form thêm người dùng
    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/add";
    }

    // Xử lý thêm người dùng
    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/add";
        }
        try {
            userService.addUser(user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Email đã tồn tại hoặc dữ liệu không hợp lệ!");
            return "users/add";
        }
        return "redirect:/users";
    }

    // Hiển thị form sửa người dùng
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng với ID " + id + " không tồn tại"));
        model.addAttribute("user", user);
        return "users/edit";
    }

    // Xử lý sửa người dùng
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/edit";
        }
        userService.updateUser(id, user);
        return "redirect:/users"; // Sửa lại đường dẫn này
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/users"; // Sửa lại đường dẫn này
    }
}