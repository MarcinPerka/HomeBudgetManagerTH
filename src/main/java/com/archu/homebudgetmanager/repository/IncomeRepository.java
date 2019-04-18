package com.archu.homebudgetmanager.repository;

import com.archu.homebudgetmanager.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    public Income findByUserIdAndId(Long userId, Long id);

    public List<Income> findByUserId(Long userId);

    @Query("SELECT i FROM Income i WHERE FUNCTION('MONTH',i.dateOfTransaction) = ?2 AND i.user.id = ?1")
    public List<Income> findIncomeByUserIdAndMonth(Long userId, Integer month);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = ?1")
    public BigDecimal findSumOfIncomesByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(i.amount),0) FROM Income i WHERE FUNCTION('MONTH', i.dateOfTransaction) =?2 AND i.user.id = ?1")
    public BigDecimal findSumOfIncomesByMonth(Long userId, Integer month);
}
