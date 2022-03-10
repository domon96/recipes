package com.recipes.controller;

import com.recipes.model.User;
import com.recipes.service.SecurityService;
import com.recipes.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegisterController {
    private final UserService userService;
    private final SecurityService securityService;

    public RegisterController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/register")
    public String register(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String register(HttpServletRequest req, @ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }

        if (userService.findUserByEmail(user.getEmail()) != null) {
            model.addAttribute("existingEmail", true);
            return "add-user";
        }

        final String password = user.getPassword();
        userService.save(user);
        securityService.autoLogin(user.getEmail(), password);

        return "home";
    }
}
