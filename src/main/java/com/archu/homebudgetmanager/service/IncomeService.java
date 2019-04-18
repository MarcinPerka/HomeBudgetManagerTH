package com.archu.homebudgetmanager.service;

import com.archu.homebudgetmanager.model.Income;
import com.archu.homebudgetmanager.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    @Autowired
    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public List<Income> getAllIncomes(Long userId) {
        List<Income> incomes = new ArrayList<>();
        incomeRepository.findByUserId(userId)
                .forEach(incomes::add);
        return incomes;
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public Income getIncomeById(Long userId, Long id) {
        return incomeRepository.findByUserIdAndId(userId, id);
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public List<Income> getIncomesByMonth(Long userId, Integer month) {
        return incomeRepository.findIncomeByUserIdAndMonth(userId, month);
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public Map<String, BigDecimal> getSumOfIncomesByCategory(Long userId) {
        Map<String, BigDecimal> sumIncomeByCategory = new HashMap<>();
        List<Income> incomes = incomeRepository.findByUserId(userId);
        incomes.forEach((income) -> {
            if (sumIncomeByCategory.containsKey(income.getIncomeCategory().name())) {
                sumIncomeByCategory.put(income.getIncomeCategory().name(), sumIncomeByCategory.get(income.getIncomeCategory().name()).add(income.getAmount()));
            } else {
                sumIncomeByCategory.put(income.getIncomeCategory().name(), new BigDecimal(String.valueOf(income.getAmount())));
            }

        });
        return sumIncomeByCategory;
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public BigDecimal getSumOfIncomes(Long userId) {
        BigDecimal sumOfIncomes = incomeRepository.findSumOfIncomesByUserId(userId);
        if (sumOfIncomes == null)
            return BigDecimal.ZERO;
        return sumOfIncomes;
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public BigDecimal getSumOfIncomesByMonth(Long userId, Integer month) {
        BigDecimal sumOfIncomesByMonth = incomeRepository.findSumOfIncomesByMonth(userId, month);
        if (sumOfIncomesByMonth == null)
            return BigDecimal.ZERO;
        return sumOfIncomesByMonth;
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public Map<String, BigDecimal> getSumOfIncomesByMonthAndCategory(Long userId, Integer month) {
        Map<String, BigDecimal> sumIncomeByMonthAndCategory = new HashMap<>();
        List<Income> incomes = getIncomesByMonth(userId, month);
        incomes.forEach((income) -> {
            if (sumIncomeByMonthAndCategory.containsKey(income.getIncomeCategory().name())) {
                sumIncomeByMonthAndCategory.put(income.getIncomeCategory().name(), sumIncomeByMonthAndCategory.get(income.getAmount()));
            } else {
                sumIncomeByMonthAndCategory.put(income.getIncomeCategory().name(), new BigDecimal(String.valueOf(income.getAmount())));
            }
        });
        return sumIncomeByMonthAndCategory;
    }

    @PreAuthorize("#income.user.id == authentication.principal.id OR hasRole('ADMIN')")
    public void addIncome(Income income) {
        incomeRepository.save(income);
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ADMIN')")
    public void deleteIncomeById(Long userId, Long id) {
        incomeRepository.deleteById(id);
    }

    @PreAuthorize("#income.user.id == authentication.principal.id OR hasRole('ADMIN')")
    public void updateIncome(Income income, Long id) {
        Income incomeToUpdate = incomeRepository.findById(id).orElse(null);

        if (incomeToUpdate == null)

            income.setId(id);
        incomeRepository.save(income);
    }
}
