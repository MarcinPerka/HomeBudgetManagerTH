package com.archu.homebudgetmanager.controller;

import com.archu.homebudgetmanager.model.Expenditure;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.service.ExpenditureService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
public class ExpenditureControllerTest {

    @Mock
    private ExpenditureService expenditureService;

    @InjectMocks
    private ExpenditureController expenditureController;

    private MockMvc mockMvc;
    private User user;
    private Expenditure expenditure1, expenditure2, expenditure3, expenditure4;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(expenditureController).build();

        user = new User("test", "test", "test@gmail.com");
        ReflectionTestUtils.setField(user, "id", 1L);

        expenditure1 = new Expenditure();
        expenditure1.setTitle("Food");
        expenditure1.setAmount(new BigDecimal(-100));
        expenditure1.setDateOfTransaction(new Date(2019, 10, 10));
        expenditure1.setExpenditureCategory(Expenditure.ExpenditureCategory.FOOD);
        expenditure1.setUser(user);
        ReflectionTestUtils.setField(expenditure1, "id", 1L);

        expenditure2 = new Expenditure("Stuff", new BigDecimal(-10.12), new Date(2019, 11, 10), Expenditure.ExpenditureCategory.UNCATEGORIZED);
        expenditure2.setUser(user);
        ReflectionTestUtils.setField(expenditure2, "id", 2L);

        expenditure3 = new Expenditure();
        expenditure3.setTitle("Stuff");
        expenditure3.setAmount(new BigDecimal(-10.12));
        expenditure3.setDateOfTransaction(new Date(2019, 10, 15));
        expenditure3.setExpenditureCategory(Expenditure.ExpenditureCategory.UNCATEGORIZED);
        expenditure3.setUser(user);
        ReflectionTestUtils.setField(expenditure3, "id", 3L);

        expenditure4 = new Expenditure("Stuff", new BigDecimal(-10.12), new Date(2019, 10, 10), Expenditure.ExpenditureCategory.UNCATEGORIZED);
        expenditure4.setUser(user);
        ReflectionTestUtils.setField(expenditure4, "id", 4L);
    }

    @Test
    public void testGetExpenditures() throws Exception {
        List<Expenditure> expenditures = new ArrayList<>(Arrays.asList(expenditure1, expenditure2, expenditure3));

        when(expenditureService.getAllExpenditures(user.getId())).thenReturn(expenditures);
        mockMvc.perform(get("/user/{userId}/expenditures", 1))
                .andExpect(view().name("expenditures/expenditures"))
                .andExpect(status().isOk());
        verify(expenditureService).getAllExpenditures(anyLong());
    }

    @Test
    public void testGetExpenditureByMonth() throws Exception {
        List<Expenditure> expenditures = new ArrayList<>(Arrays.asList(expenditure1, expenditure3));

        when(expenditureService.getExpendituresByMonth(user.getId(), 10)).thenReturn(expenditures);
        mockMvc.perform(get("/user/{userId}/expenditures/month/{month}", 1, 10))
                .andExpect(view().name("expenditures/monthlyExpenditures"))
                .andExpect(status().isOk());
        verify(expenditureService).getExpendituresByMonth(anyLong(), anyInt());
    }

    @Test
    public void testShowAddForm() throws Exception {
        mockMvc.perform(get("/user/{userId}/expenditures/add", anyLong()))
                .andExpect(view().name("expenditures/addExpenditure"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddExpenditure() throws Exception {
        doAnswer((i) -> {
            System.out.println("Created");
            return null;
        }).when(expenditureService).addExpenditure(any(Expenditure.class));
        mockMvc.perform(post("/user/{userId}/expenditures/add", anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
        verify(expenditureService).addExpenditure(any(Expenditure.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        mockMvc.perform(get("/user/{userId}/expenditures/update/{id}",anyLong(), anyLong()))
                .andExpect(view().name("expenditures/updateExpenditure"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateExpenditure() throws Exception {
        doAnswer((i) -> {
            System.out.println("Updated");
            return null;
        }).when(expenditureService).updateExpenditure(any(Expenditure.class), anyLong());
        mockMvc.perform(put("/user/{userId}/expenditures/update/{id}", anyLong(), anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
        verify(expenditureService).updateExpenditure(any(Expenditure.class), anyLong());
    }

    @Test
    public void testShowDeleteForm() throws Exception {
        mockMvc.perform(get("/user/{userId}/expenditures/delete/{id}",anyLong(), anyLong()))
                .andExpect(view().name("expenditures/deleteExpenditure"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteExpenditure() throws Exception {
        doAnswer((i) -> {
            System.out.println("Deleted");
            return null;
        }).when(expenditureService).deleteExpenditureById(anyLong(), anyLong());
        mockMvc.perform(delete("/user/{userId}/expenditures/delete/{id}", anyLong(), anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
        verify(expenditureService).deleteExpenditureById(anyLong(), anyLong());
    }
}

