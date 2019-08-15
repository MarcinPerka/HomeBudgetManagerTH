package com.archu.homebudgetmanager.service;

import com.archu.homebudgetmanager.model.Income;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.repository.IncomeRepository;
import com.archu.homebudgetmanager.service.IncomeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IncomeServiceTest {

    @InjectMocks
    IncomeService incomeService;

    @Mock
    IncomeRepository incomeRepository;

    @Mock
    Income income;

    private User user;
    private Income income1, income2, income3, income4;

    @Before
    public void setUp() {
        user = new User("test", "test", "test@gmail.com");
        ReflectionTestUtils.setField(user, "id", 1L);
        income1 = new Income("Parents", new BigDecimal(900.39), new Date(2019, 10, 1), Income.IncomeCategory.PARENTS);
        income1.setUser(user);
        ReflectionTestUtils.setField(income1, "id", 1L);

        income2 = new Income();
        income2.setTitle("Some stuff");
        income2.setAmount(new BigDecimal(1000.39));
        income2.setDateOfTransaction(new Date(2019, 06, 10));
        income2.setIncomeCategory(Income.IncomeCategory.WORK);
        income2.setUser(user);
        ReflectionTestUtils.setField(income2, "id", 2L);

        income3 = new Income("Some stuff", new BigDecimal(700), new Date(2019, 10, 1), Income.IncomeCategory.WORK);
        income3.setUser(user);
        ReflectionTestUtils.setField(income3, "id", 3L);

        income4 = new Income("Some stuff", new BigDecimal(1000), new Date(2019, 10, 10), Income.IncomeCategory.WORK);
        income4.setUser(user);
        ReflectionTestUtils.setField(income4, "id", 4L);
    }

    @Test
    public void testGetIncomeById() {
        Income income = mock(Income.class);
        User user = mock(User.class);
        when(incomeRepository.findByUserIdAndId(user.getId(), income.getId())).thenReturn(income);
        Income found = incomeService.getIncomeById(user.getId(), income.getId());
        assertThat(found).isEqualTo(income);
        verify(incomeRepository).findByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void testGetAllIncomes() {
        List<Income> incomes = new ArrayList<>();
        incomes.add(income);

        when(incomeRepository.findByUserId(user.getId())).thenReturn(incomes);
        List<Income> found = incomeService.getAllIncomes(1L);
        assertThat(found).isEqualTo(incomes);
        verify(incomeRepository).findByUserId(anyLong());
    }

    @Test
    public void testGetSumOfIncomesByCategory() {
        List<Income> incomes = new ArrayList<>(Arrays.asList(income1, income2, income3));
        Map<String, BigDecimal> incomeByCategory = new HashMap<>();
        incomeByCategory.put("PARENTS", income1.getAmount());
        incomeByCategory.put("WORK", income2.getAmount().add(income3.getAmount()));

        when(incomeRepository.findByUserId(user.getId())).thenReturn(incomes);
        Map<String, BigDecimal> found = incomeService.getSumOfIncomesByCategory(1L);
        assertThat(found).isEqualTo(incomeByCategory);
        verify(incomeRepository).findByUserId(anyLong());
    }

    @Test
    public void testGetSumOfIncomesByMonthAndCategory() {
        List<Income> incomes = new ArrayList<>(Arrays.asList(income1, income3, income4));
        Map<String, BigDecimal> incomesByCategory = new HashMap<>();
        incomesByCategory.put("PARENTS", income1.getAmount());
        incomesByCategory.put("WORK", income3.getAmount());
        incomesByCategory.put("WORK", incomesByCategory.get(income4.getAmount()));

        when(incomeRepository.findIncomeByUserIdAndMonth(user.getId(), 10)).thenReturn(incomes);
        Map<String, BigDecimal> found = incomeService.getSumOfIncomesByMonthAndCategory(1L, 10);
        assertThat(found).isEqualTo(incomesByCategory);
        verify(incomeRepository).findIncomeByUserIdAndMonth(anyLong(), anyInt());
    }

    @Test
    public void testGetSumOfIncomes() {
        BigDecimal sum = income1.getAmount().add(income2.getAmount());

        when(incomeRepository.findSumOfIncomesByUserId(user.getId())).thenReturn(sum);
        BigDecimal found = incomeService.getSumOfIncomes(1L);
        assertThat(found).isEqualTo(sum);
        verify(incomeRepository).findSumOfIncomesByUserId(anyLong());
    }

    @Test
    public void testGetSumOfIncomesByMonth() {
        BigDecimal sum = income1.getAmount();

        when(incomeRepository.findSumOfIncomesByMonth(user.getId(), 10)).thenReturn(sum);
        BigDecimal found = incomeService.getSumOfIncomesByMonth(1L, 10);
        assertThat(found).isEqualTo(sum);
        verify(incomeRepository).findSumOfIncomesByMonth(anyLong(), anyInt());
    }

    @Test
    public void testAddIncome() {
        doAnswer((i) -> {
            System.out.println("Created");
            return null;
        }).when(incomeRepository).save(any(Income.class));
        incomeService.addIncome(income1);
        verify(incomeRepository).save(any(Income.class));
    }

    @Test
    public void testDeleteIncomeById() {
        doAnswer((i) -> {
            System.out.println("Deleted");
            return null;
        }).when(incomeRepository).deleteById(anyLong());
        incomeService.deleteIncomeById(user.getId(), income1.getId());
        verify(incomeRepository).deleteById(anyLong());
    }

    @Test
    public void testUpdateIncome() {
        Income updatedIncome = new Income("Found on the ground", new BigDecimal(100.03), new Date(2019, 3, 3), Income.IncomeCategory.UNCATEGORIZED);
        ReflectionTestUtils.setField(updatedIncome, "id", 1L);

        when(incomeRepository.findById(income1.getId())).thenReturn(Optional.of(income1));
        doAnswer((i) -> {
            System.out.println("Updated");
            return null;
        }).when(incomeRepository).save(any(Income.class));
        incomeService.updateIncome(updatedIncome, user.getId());
        verify(incomeRepository).findById(anyLong());
        verify(incomeRepository).save(any(Income.class));
    }
}
