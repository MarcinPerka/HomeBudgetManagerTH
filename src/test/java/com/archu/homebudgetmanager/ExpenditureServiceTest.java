package com.archu.homebudgetmanager;

import com.archu.homebudgetmanager.model.Expenditure;
import com.archu.homebudgetmanager.model.User;
import com.archu.homebudgetmanager.repository.ExpenditureRepository;
import com.archu.homebudgetmanager.service.ExpenditureService;
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
public class ExpenditureServiceTest {

    @InjectMocks
    private ExpenditureService expenditureService;

    @Mock
    private ExpenditureRepository expenditureRepository;

    private User user;
    private Expenditure expenditure1, expenditure2, expenditure3, expenditure4;

    @Before
    public void setUp() {
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
    public void testGetExpenditureById() {
        when(expenditureRepository.findByUserIdAndId(user.getId(), expenditure1.getId())).thenReturn(expenditure1);
        Expenditure found = expenditureService.getExpenditureById(1L, 1L);
        assertThat(found).isEqualTo(expenditure1);
        verify(expenditureRepository).findByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    public void testGetAllExpenditures() {
        List<Expenditure> expenditures = new ArrayList<>(Arrays.asList(expenditure1, expenditure2));

        when(expenditureRepository.findByUserId(user.getId())).thenReturn(expenditures);
        List<Expenditure> found = expenditureService.getAllExpenditures(1L);
        assertThat(found).isEqualTo(expenditures);
        verify(expenditureRepository).findByUserId(anyLong());
    }

    @Test
    public void testGetSumOfExpendituresByCategory() {
        List<Expenditure> expenditures = new ArrayList<>(Arrays.asList(expenditure1, expenditure2, expenditure3));
        Map<String, BigDecimal> expendituresByCategory = new HashMap<>();
        expendituresByCategory.put("FOOD", new BigDecimal(-100));
        expendituresByCategory.put("UNCATEGORIZED", expenditure2.getAmount().add(expenditure3.getAmount()));

        when(expenditureRepository.findByUserId(user.getId())).thenReturn(expenditures);
        Map<String, BigDecimal> found = expenditureService.getSumOfExpendituresByCategory(1L);
        assertThat(found).isEqualTo(expendituresByCategory);
        verify(expenditureRepository).findByUserId(anyLong());
    }

    @Test
    public void testGetSumOfExpendituresByMonthAndCategory() {
        List<Expenditure> expenditures = new ArrayList<>(Arrays.asList(expenditure1, expenditure3, expenditure4));
        Map<String, BigDecimal> expendituresByCategory = new HashMap<>();
        expendituresByCategory.put("FOOD", expenditure1.getAmount());
        expendituresByCategory.put("UNCATEGORIZED", expenditure3.getAmount());
        expendituresByCategory.put("UNCATEGORIZED", expendituresByCategory.get(expenditure4.getAmount()));

        when(expenditureRepository.findByUserIdAndMonth(user.getId(), 10)).thenReturn(expenditures);
        Map<String, BigDecimal> found = expenditureService.getSumOfExpendituresByMonthAndCategory(1L, 10);
        assertThat(found).isEqualTo(expendituresByCategory);
        verify(expenditureRepository).findByUserIdAndMonth(anyLong(), anyInt());
    }

    @Test
    public void testGetSumOfExpenditures() {
        BigDecimal sum = expenditure1.getAmount().add(expenditure2.getAmount());

        when(expenditureRepository.findSumOfExpendituresByUserId(user.getId())).thenReturn(sum);
        BigDecimal found = expenditureService.getSumOfExpenditures(1L);
        assertThat(found).isEqualTo(sum);
        verify(expenditureRepository).findSumOfExpendituresByUserId(anyLong());
    }

    @Test
    public void testGetSumOfExpendituresByMonth() {
        List<Expenditure> expenditures = new ArrayList<>();
        expenditures.add(expenditure1);
        expenditures.add(expenditure2);
        BigDecimal sum = expenditure1.getAmount();

        when(expenditureRepository.findSumOfExpendituresByUserIdAndMonth(user.getId(), 10)).thenReturn(sum);
        BigDecimal found = expenditureService.getSumOfExpendituresByMonth(1L, 10);
        assertThat(found).isEqualTo(sum);
        verify(expenditureRepository).findSumOfExpendituresByUserIdAndMonth(anyLong(), anyInt());
    }

    @Test
    public void testAddExpenditure() {
        doAnswer((i) -> {
            System.out.println("Created");
            return null;
        }).when(expenditureRepository).save(any(Expenditure.class));
        expenditureService.addExpenditure(expenditure1);
        verify(expenditureRepository).save(any(Expenditure.class));
    }

    @Test
    public void testDeleteExpenditureById() {
        doAnswer((i) -> {
            System.out.println("Deleted");
            return null;
        }).when(expenditureRepository).deleteById(anyLong());
        expenditureService.deleteExpenditureById(user.getId(), expenditure1.getId());
        verify(expenditureRepository).deleteById(anyLong());
    }

    @Test
    public void testUpdateExpenditure() {
        Expenditure updatedExpenditure = new Expenditure("Venezia", new BigDecimal(-100), new Date(2019, 3, 3), Expenditure.ExpenditureCategory.HOLIDAYS);
        ReflectionTestUtils.setField(updatedExpenditure, "id", 1L);

        when(expenditureRepository.findById(expenditure1.getId())).thenReturn(Optional.of(expenditure1));
        doAnswer((i) -> {
            System.out.println("Updated");
            return null;
        }).when(expenditureRepository).save(any(Expenditure.class));
        expenditureService.updateExpenditure(updatedExpenditure, user.getId());
        verify(expenditureRepository).findById(anyLong());
        verify(expenditureRepository).save(any(Expenditure.class));
    }
}
