package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.exception.UserAlreadyExistAuthenticationException;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/users";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users/user";
    }

    @GetMapping("/registration")
    public String showRegisterForm() {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerUser(User user) throws UserAlreadyExistAuthenticationException {
        userService.createUser(user);
        return "auth/login";
    }

    @PutMapping("user/update/{id}")
    public String updateUser(User user, @PathVariable Long id) throws Exception {
        userService.updateUser(user, id);
        return "redirect:/user/{id}";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(Model model, @PathVariable Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users/updateUser";
    }

    @PreAuthorize("#id == authentication.principal.id OR hasRole('ADMIN')")
    @DeleteMapping("user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "/index";
    }

    @GetMapping("/user/delete/{id}")
    public String showDeleteForm(Model model, @PathVariable Long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "users/deleteUser";
    }
}
