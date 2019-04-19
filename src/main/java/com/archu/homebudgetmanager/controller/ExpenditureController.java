package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.model.Expenditure;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.service.ExpenditureService;
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
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    @Autowired
    public ExpenditureController(ExpenditureService expenditureService) {
        this.expenditureService = expenditureService;
    }

    @GetMapping("/expenditures")
    public String getExpenditures(@PathVariable Long userId, Model model) {
        List<Expenditure> expenditures = expenditureService.getAllExpenditures(userId);
        BigDecimal sumOfExpenditures = expenditureService.getSumOfExpenditures(userId);
        Map<String, BigDecimal> sumOfExpendituresByCategory = expenditureService.getSumOfExpendituresByCategory(userId);
        model.addAttribute("expenditures", expenditures);
        model.addAttribute("sumOfExpenditures", sumOfExpenditures);
        model.addAttribute("sumOfExpendituresByCategory", sumOfExpendituresByCategory);
        return "expenditures/expenditures";
    }

    @GetMapping("/expenditures/month/{month}")
    public String getExpendituresByMonth(@PathVariable Long userId, @PathVariable Integer month, Model model) {
        List<Expenditure> expendituresByMonth = expenditureService.getExpendituresByMonth(userId, month);
        BigDecimal sumOfExpendituresByMonth = expenditureService.getSumOfExpendituresByMonth(userId, month);
        Map<String, BigDecimal> sumOfExpendituresByMonthAndCategory = expenditureService.getSumOfExpendituresByMonthAndCategory(userId, month);
        model.addAttribute("userId", userId);
        model.addAttribute("expendituresByMonth", expendituresByMonth);
        model.addAttribute("sumOfExpenditures", sumOfExpendituresByMonth);
        model.addAttribute("sumOfExpendituresByCategory", sumOfExpendituresByMonthAndCategory);
        return "expenditures/monthlyExpenditures";
    }

    @DeleteMapping("/expenditures/delete/{id}")
    public String deleteExpenditureById(@PathVariable Long userId, @PathVariable Long id) {
        expenditureService.deleteExpenditureById(userId, id);
        return "redirect:/user/{userId}/expenditures";
    }

    @GetMapping("/expenditures/delete/{id}")
    public String showDeleteForm(@PathVariable Long userId, Model model, @PathVariable Long id) {
        Expenditure expenditure = expenditureService.getExpenditureById(userId, id);
        model.addAttribute("expenditure", expenditure);
        return "expenditures/deleteExpenditure";
    }

    @PostMapping("/expenditures/add")
    public String addExpenditure(@PathVariable Long userId, Expenditure expenditure) {
        expenditure.setUser(new User(userId));
        expenditureService.addExpenditure(expenditure);
        return "redirect:/user/{userId}/expenditures";
    }

    @GetMapping("/expenditures/add")
    public String showAddForm(@PathVariable Long userId, Model model, Expenditure expenditure) {
        model.addAttribute("userId", userId);
        model.addAttribute("expenditure", expenditure);
        return "expenditures/addExpenditure";
    }

    @PutMapping("/expenditures/update/{id}")
    public String updateExpenditure(@PathVariable Long userId, Expenditure expenditure, @PathVariable Long id) {
        expenditure.setUser(new User(userId));
        expenditureService.updateExpenditure(expenditure, id);
        return "redirect:/user/{userId}/expenditures";
    }

    @GetMapping("/expenditures/update/{id}")
    public String showUpdateForm(@PathVariable Long userId, Model model, @PathVariable Long id) {
        Expenditure expenditure = expenditureService.getExpenditureById(userId, id);
        model.addAttribute("expenditure", expenditure);
        return "expenditures/updateExpenditure";
    }

    @InitBinder
    public void initDateBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}
