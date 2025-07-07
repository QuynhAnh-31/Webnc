package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Lấy tất cả người dùng
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Tìm kiếm người dùng theo tên hoặc email
    public List<User> searchUsers(String keyword) {
        return userRepository.findByNameOrEmailContainingIgnoreCase(keyword);
    }

    // Thêm người dùng
    public void addUser(User user) {
        userRepository.save(user);
    }

    // Lấy người dùng theo ID
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Cập nhật người dùng
    public void updateUser(Integer id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            user.setPhone(updatedUser.getPhone());
            user.setStudentId(updatedUser.getStudentId());
            user.setClassName(updatedUser.getClassName());
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Người dùng với ID " + id + " không tồn tại");
        }
    }

    // Xóa người dùng
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Người dùng với ID " + id + " không tồn tại");
        }
        userRepository.deleteById(id);
    }
}