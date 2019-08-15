package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.model.Income;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.service.IncomeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
public class IncomeControllerTest {

    @Mock
    private IncomeService incomeService;

    @InjectMocks
    private IncomeController incomeController;

    private MockMvc mockMvc;
    private User user;
    private Income income1, income2, income3, income4;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(incomeController).build();

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

        income4 = new Income("Some stuff", new BigDecimal(1700), new Date(2019, 10, 11), Income.IncomeCategory.WORK);
        income4.setUser(user);
        ReflectionTestUtils.setField(income4, "id", 4L);
    }

    @Test
    public void testGetIncomes() throws Exception {
        List<Income> incomes = new ArrayList<>(Arrays.asList(income1, income2, income3));

        when(incomeService.getAllIncomes(user.getId())).thenReturn(incomes);
        mockMvc.perform(get("/user/{userId}/incomes", 1))
                .andExpect(view().name("incomes/incomes"))
                .andExpect(status().isOk());
        verify(incomeService).getAllIncomes(anyLong());
    }

    @Test
    public void testGetIncomeByMonth() throws Exception {
        List<Income> incomes = new ArrayList<>(Arrays.asList(income1, income3));

        when(incomeService.getIncomesByMonth(user.getId(), 10)).thenReturn(incomes);
        mockMvc.perform(get("/user/{userId}/incomes/month/{month}", 1, 10))
                .andExpect(view().name("incomes/monthlyIncomes"))
                .andExpect(status().isOk());
        verify(incomeService).getIncomesByMonth(anyLong(), anyInt());
    }

    @Test
    public void testShowAddForm() throws Exception {
        mockMvc.perform(get("/user/{userId}/incomes/add", eq(anyLong())))
                .andExpect(view().name("incomes/addIncome"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddIncome() throws Exception {
        doAnswer((i) -> {
            System.out.println("Created");
            return null;
        }).when(incomeService).addIncome(any(Income.class));
        mockMvc.perform(post("/user/{userId}/incomes/add", anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
        verify(incomeService).addIncome(any(Income.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        mockMvc.perform(get("/user/{userId}/incomes/update/{id}", anyLong(), anyLong()))
                .andExpect(view().name("incomes/updateIncome"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateIncome() throws Exception {
        doAnswer((i) -> {
            System.out.println("Updated");
            return null;
        }).when(incomeService).updateIncome(any(Income.class), anyLong());
        mockMvc.perform(put("/user/{userId}/incomes/update/{id}", anyLong(), anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
        verify(incomeService).updateIncome(any(Income.class), anyLong());
    }

    @Test
    public void testShowDeleteForm() throws Exception {
        mockMvc.perform(get("/user/{userId}/incomes/delete/{id}", anyLong(), anyLong()))
                .andExpect(view().name("incomes/deleteIncome"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteIncome() throws Exception {
        doAnswer((i) -> {
            System.out.println("Deleted");
            return null;
        }).when(incomeService).deleteIncomeById(anyLong(), anyLong());
        mockMvc.perform(delete("/user/{userId}/incomes/delete/{id}", anyLong(), anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
        verify(incomeService).deleteIncomeById(anyLong(), anyLong());
    }
}

