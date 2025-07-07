package com.example.servingwebcontent.converter;

import com.example.servingwebcontent.model.User;
import com.example.servingwebcontent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<String, User> {
    @Autowired
    private UserService userService;

    @Override
    public User convert(String id) {
        if (id == null || id.isEmpty()) return null;
        try {
            Integer userId = Integer.valueOf(id);
            return userService.getUserById(userId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}