package com.archu.homebudgetmanager;

import com.archu.homebudgetmanager.model.Expenditure;
import com.archu.homebudgetmanager.model.Income;
import com.archu.homebudgetmanager.model.Transaction;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.repository.ExpenditureRepository;
import com.archu.homebudgetmanager.repository.IncomeRepository;
import com.archu.homebudgetmanager.service.BalanceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BalanceServiceTest {

    @InjectMocks
    private BalanceService balanceService;

    @Mock
    private ExpenditureRepository expenditureRepository;

    @Mock
    private IncomeRepository incomeRepository;

    private User user;
    private Income income1, income2, income3;
    private Expenditure expenditure1, expenditure2, expenditure3;

    @Before
    public void setUp() {
        user = new User("test", "test", "test@gmail.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        income1 = new Income("Parents", new BigDecimal(900.39), new Date(2019, 10, 1), Income.IncomeCategory.PARENTS);
        income1.setUser(user);
        ReflectionTestUtils.setField(income1, "id", 1L);

        income2 = new Income("Some stuff", new BigDecimal(1000.39), new Date(2019, 06, 10), Income.IncomeCategory.WORK);
        income2.setTitle("Some stuff");
        income2.setAmount(new BigDecimal(1000.39));
        income2.setDateOfTransaction(new Date(2019, 06, 10));
        income2.setIncomeCategory(Income.IncomeCategory.WORK);
        income2.setUser(user);
        ReflectionTestUtils.setField(income2, "id", 2L);

        income3 = new Income("Some stuff", new BigDecimal(700), new Date(2019, 10, 1), Income.IncomeCategory.WORK);
        income3.setUser(user);
        ReflectionTestUtils.setField(income3, "id", 3L);

        expenditure1 = new Expenditure();
        expenditure1.setTitle("Food");
        expenditure1.setAmount(new BigDecimal(-100));
        expenditure1.setDateOfTransaction(new Date(2019, 10, 10));
        expenditure1.setExpenditureCategory(Expenditure.ExpenditureCategory.FOOD);
        expenditure1.setUser(user);
        ReflectionTestUtils.setField(expenditure1, "id", 4L);

        expenditure2 = new Expenditure();
        expenditure2.setTitle("Stuff");
        expenditure2.setAmount(new BigDecimal(-10.12));
        expenditure2.setDateOfTransaction(new Date(2019, 11, 10));
        expenditure2.setExpenditureCategory(Expenditure.ExpenditureCategory.UNCATEGORIZED);
        expenditure2.setUser(user);
        ReflectionTestUtils.setField(expenditure2, "id", 5L);

        expenditure3 = new Expenditure();
        expenditure3.setTitle("Stuff");
        expenditure3.setAmount(new BigDecimal(-10.12));
        expenditure3.setDateOfTransaction(new Date(2019, 10, 15));
        expenditure3.setExpenditureCategory(Expenditure.ExpenditureCategory.UNCATEGORIZED);
        expenditure3.setUser(user);
        ReflectionTestUtils.setField(expenditure3, "id", 6L);

    }

    @Test
    public void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>(Arrays.asList(income1, income2, income3, expenditure1, expenditure2, expenditure3));
        List<Income> incomes = new ArrayList<>(Arrays.asList(income1, income2, income3));
        List<Expenditure> expenditures = new ArrayList<>(Arrays.asList(expenditure1, expenditure2, expenditure3));

        when(incomeRepository.findByUserId(user.getId())).thenReturn(incomes);
        when(expenditureRepository.findByUserId(user.getId())).thenReturn(expenditures);
        List<Transaction> found = balanceService.getAllTransactions(user.getId());
        assertThat(found).isEqualTo(transactions);
        verify(incomeRepository).findByUserId(anyLong());
        verify(expenditureRepository).findByUserId(anyLong());

    }

    @Test
    public void testGetBalanceByMonth() {
        BigDecimal incomesSum = income1.getAmount().add(income3.getAmount());
        BigDecimal expendituresSum = expenditure1.getAmount().add(expenditure3.getAmount());
        BigDecimal balance = incomesSum.add(expendituresSum);

        when(incomeRepository.findSumOfIncomesByMonth(user.getId(), 10)).thenReturn(incomesSum);
        when(expenditureRepository.findSumOfExpendituresByUserIdAndMonth(user.getId(), 10)).thenReturn(expendituresSum);
        BigDecimal found = balanceService.getBalanceByMonth(user.getId(), 10);
        assertThat(found).isEqualTo(balance);
        verify(incomeRepository).findSumOfIncomesByMonth(anyLong(), anyInt());
        verify(expenditureRepository).findSumOfExpendituresByUserIdAndMonth(anyLong(), anyInt());
    }

    @Test
    public void testGetTotalBalance() {
        BigDecimal incomesSum = income1.getAmount().add(income3.getAmount()).add(income2.getAmount());
        BigDecimal expendituresSum = expenditure1.getAmount().add(expenditure3.getAmount()).add(expenditure2.getAmount());
        BigDecimal balance = incomesSum.add(expendituresSum);

        when(incomeRepository.findSumOfIncomesByUserId(user.getId())).thenReturn(incomesSum);
        when(expenditureRepository.findSumOfExpendituresByUserId(user.getId())).thenReturn(expendituresSum);
        BigDecimal found = balanceService.getTotalBalance(user.getId());
        assertThat(found).isEqualTo(balance);
        verify(incomeRepository).findSumOfIncomesByUserId(anyLong());
        verify(expenditureRepository).findSumOfExpendituresByUserId(anyLong());
    }
}
