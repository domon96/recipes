package com.recipes.controller;

import com.recipes.model.User;
import com.recipes.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "add-user";
        }

        if (userService.findUserByEmail(user.getEmail()) != null) {
            model.addAttribute("existingEmail", true);
            return "add-user";
        }

        userService.save(user);
        return "home";
    }
}
