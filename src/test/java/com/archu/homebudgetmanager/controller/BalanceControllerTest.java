package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.model.Expenditure;
import com.archu.homebudgetmanager.model.Income;
import com.archu.homebudgetmanager.model.Transaction;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.service.BalanceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
public class BalanceControllerTest {

    @InjectMocks
    private BalanceController balanceController;

    @Mock
    private BalanceService balanceService;

    private MockMvc mockMvc;
    private User user;
    private Income income1, income2, income3;
    private Expenditure expenditure1, expenditure2, expenditure3;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(balanceController).build();

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
    public void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>(Arrays.asList(income1, income2, income3, expenditure1, expenditure2, expenditure3));

        when(balanceService.getAllTransactions(user.getId())).thenReturn(transactions);
        mockMvc.perform(get("/user/{userId}/balance", 1))
                .andExpect(view().name("balance/balance"))
                .andExpect(status().isOk());
        verify(balanceService).getAllTransactions(anyLong());
    }

}
