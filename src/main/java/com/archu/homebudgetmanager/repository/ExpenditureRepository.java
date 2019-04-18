package com.archu.homebudgetmanager.repository;

import com.archu.homebudgetmanager.model.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

    public Expenditure findByUserIdAndId(Long userId, Long id);

    public List<Expenditure> findByUserId(Long userId);

    @Query("SELECT e FROM Expenditure e WHERE FUNCTION('MONTH',e.dateOfTransaction) = ?2 AND e.user.id = ?1")
    public List<Expenditure> findByUserIdAndMonth(Long userId, Integer month);

    @Query("SELECT SUM(e.amount) FROM Expenditure e WHERE e.user.id = ?1")
    public BigDecimal findSumOfExpendituresByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(e.amount),0) FROM Expenditure e WHERE FUNCTION('MONTH', e.dateOfTransaction) =?2 AND e.user.id = ?1")
    public BigDecimal findSumOfExpendituresByUserIdAndMonth(Long userId, Integer month);
}
