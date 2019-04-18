package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.model.UserDetailsImpl;
import com.archu.homebudgetmanager.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class FragmentController {

    private final UserDetailsServiceImpl userService;

    @Autowired
    public FragmentController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/expenditures")
    public String viewExpenditures(Principal principal) {
        UserDetailsImpl user = (UserDetailsImpl) userService.loadUserByUsername(principal.getName());
        Long id = user.getId();
        return "redirect:/user/" + id + "/expenditures";
    }

    @RequestMapping("/incomes")
    public String viewIncomes(Principal principal) {
        UserDetailsImpl user = (UserDetailsImpl) userService.loadUserByUsername(principal.getName());
        Long id = user.getId();
        return "redirect:/user/" + id + "/incomes";
    }

    @RequestMapping("/balance")
    public String viewBalance(Principal principal) {
        UserDetailsImpl user = (UserDetailsImpl) userService.loadUserByUsername(principal.getName());
        Long id = user.getId();
        return "redirect:/user/" + id + "/balance";
    }

    @RequestMapping("/user")
    public String viewUser(Principal principal) {
        UserDetailsImpl user = (UserDetailsImpl) userService.loadUserByUsername(principal.getName());
        Long id = user.getId();
        return "redirect:/user/" + id;
    }
}
