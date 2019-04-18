package com.archu.homebudgetmanager.service;

import com.archu.homebudgetmanager.model.Transaction;
import com.archu.homebudgetmanager.repository.ExpenditureRepository;
import com.archu.homebudgetmanager.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceService {

    private final ExpenditureRepository expenditureRepository;
    private final IncomeRepository incomeRepository;

    @Autowired
    public BalanceService(ExpenditureRepository expenditureRepository, IncomeRepository incomeRepository) {
        this.expenditureRepository = expenditureRepository;
        this.incomeRepository = incomeRepository;
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ROLE_ADMIN')")
    public List<Transaction> getAllTransactions(Long userId) {
        List<Transaction> transactions = new ArrayList<>();
        incomeRepository.findByUserId(userId)
                .forEach(transactions::add);
        expenditureRepository.findByUserId(userId)
                .forEach(transactions::add);
        return transactions;
    }

    @PreAuthorize("#userId == authentication.principal.id OR hasRole('ROLE_ADMIN')")
    public BigDecimal getTotalBalance(Long userId) {
        BigDecimal sumOfExpenditures = expenditureRepository.findSumOfExpendituresByUserId(userId);
        BigDecimal sumOfIncomes = incomeRepository.findSumOfIncomesByUserId(userId);
        if (sumOfExpenditures == null)
            sumOfExpenditures = BigDecimal.ZERO;
        if (sumOfIncomes == null)
            sumOfIncomes = BigDecimal.ZERO;

        return sumOfIncomes.add(sumOfExpenditures);
    }
}
