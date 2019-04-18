package com.archu.homebudgetmanager.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "expenditure")
public class Expenditure extends Transaction {

    private static final long serialVersionUID = 2L;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "expenditure_category", columnDefinition = "ENUM('FOOD', 'TRANSPORT', 'PAYMENTS', 'ENTERTAINMENT','HOLIDAYS', 'UNCATEGORIZED')")
    private ExpenditureCategory expenditureCategory;

    Expenditure() {
    }

    public Expenditure(String title, BigDecimal amount, Date dateOfTransaction, ExpenditureCategory expenditureCategory) {
        this.title = title;
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
        this.expenditureCategory = expenditureCategory;
    }

    public ExpenditureCategory getExpenditureCategory() {
        return expenditureCategory;
    }

    public void setExpenditureCategory(ExpenditureCategory expenditureCategory) {
        this.expenditureCategory = expenditureCategory;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (id == null || obj == null || getClass() != obj.getClass())
            return false;
        Expenditure that = (Expenditure) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}


