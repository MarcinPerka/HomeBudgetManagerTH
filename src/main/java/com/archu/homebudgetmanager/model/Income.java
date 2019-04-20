package com.archu.homebudgetmanager.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "income")
public class Income extends Transaction {

    private static final long serialVersionUID = 3L;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "income_category", columnDefinition = "ENUM('STUDY', 'WORK','PARENTS', 'UNCATEGORIZED')")
    private IncomeCategory incomeCategory;

    public enum IncomeCategory {STUDY, WORK, PARENTS, UNCATEGORIZED;}

    Income() {
    }

    public Income(String title, BigDecimal amount, Date dateOfTransaction, IncomeCategory incomeCategory) {
        this.title = title;
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
        this.incomeCategory = incomeCategory;
    }

    public IncomeCategory getIncomeCategory() {
        return incomeCategory;
    }

    public void setIncomeCategory(IncomeCategory incomeCategory) {
        this.incomeCategory = incomeCategory;
    }


}
