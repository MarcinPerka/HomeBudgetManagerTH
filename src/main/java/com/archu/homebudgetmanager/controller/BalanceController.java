package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.model.Transaction;
import com.archu.homebudgetmanager.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/user/{userId}")
public class BalanceController {

    private final BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/balance")
    public String getTransactions(@PathVariable Long userId, Model model) {
        List<Transaction> transactions = balanceService.getAllTransactions(userId);
        BigDecimal totalBalance = balanceService.getTotalBalance(userId);
        model.addAttribute("totalBalance", BigDecimal.ZERO);
        model.addAttribute("transactions", transactions);
        model.addAttribute("totalBalance", totalBalance);
        return "balance/balance";
    }
}
