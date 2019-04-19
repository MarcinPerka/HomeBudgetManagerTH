package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.model.Income;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/{userId}")
public class IncomeController {

    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping("/incomes")
    public String getIncomes(@PathVariable Long userId, Model model) {
        List<Income> incomes = incomeService.getAllIncomes(userId);
        BigDecimal sumOfIncomes = incomeService.getSumOfIncomes(userId);
        Map<String, BigDecimal> sumOfIncomesByCategory = incomeService.getSumOfIncomesByCategory(userId);
        model.addAttribute("incomes", incomes);
        model.addAttribute("sumOfIncomes", sumOfIncomes);
        model.addAttribute("sumOfIncomesByCategory", sumOfIncomesByCategory);
        return "incomes/incomes";
    }

    @GetMapping("/incomes/month/{month}")
    public String getIncomesByMonth(@PathVariable Long userId, @PathVariable Integer month, Model model) {
        List<Income> incomesByMonth = incomeService.getIncomesByMonth(userId, month);
        BigDecimal sumOfIncomesByMonth = incomeService.getSumOfIncomesByMonth(userId, month);
        Map<String, BigDecimal> sumOfIncomesByMonthAndCategory = incomeService.getSumOfIncomesByMonthAndCategory(userId, month);
        model.addAttribute("userId", userId);
        model.addAttribute("incomesByMonth", incomesByMonth);
        model.addAttribute("sumOfIncomes", sumOfIncomesByMonth);
        model.addAttribute("sumOfIncomesByCategory", sumOfIncomesByMonthAndCategory);
        return "incomes/monthlyIncomes";
    }

    @DeleteMapping("/incomes/delete/{id}")
    public String deleteIncomeById(@PathVariable Long userId, @PathVariable Long id) {
        incomeService.deleteIncomeById(userId, id);
        return "redirect:/user/{userId}/incomes";
    }

    @GetMapping("/incomes/delete/{id}")
    public String showDeleteForm(@PathVariable Long userId, Model model, @PathVariable Long id) {
        Income income = incomeService.getIncomeById(userId, id);
        model.addAttribute("income", income);
        return "incomes/deleteIncome";
    }

    @PostMapping("/incomes/add")
    public String addIncome(@PathVariable Long userId, Income income) {
        income.setUser(new User(userId));
        incomeService.addIncome(income);
        return "redirect:/user/{userId}/incomes";
    }

    @GetMapping("/incomes/add")
    public String showAddForm(@PathVariable Long userId, Model model, Income income) {
        model.addAttribute("userId", userId);
        model.addAttribute("income", income);
        return "incomes/addIncome";
    }

    @PutMapping("/incomes/update/{id}")
    public String updateIncome(@PathVariable Long userId, Income income, @PathVariable Long id) {
        income.setUser(new User(userId));
        incomeService.updateIncome(income, id);
        return "redirect:/user/{userId}/incomes";
    }

    @GetMapping("/incomes/update/{id}")
    public String showUpdateForm(@PathVariable Long userId, Model model, @PathVariable Long id) {
        Income income = incomeService.getIncomeById(userId, id);
        model.addAttribute("income", income);
        return "incomes/updateIncome";
    }

    @InitBinder
    public void initDateBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}
