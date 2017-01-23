package com.nnayram.expensemanager;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.nnayram.expensemanager.db.DBHelper;
import com.nnayram.expensemanager.model.Budget;
import com.nnayram.expensemanager.model.BudgetDetail;
import com.nnayram.expensemanager.model.TranType;
import com.nnayram.expensemanager.service.BudgetRepository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Rufo on 1/23/2017.
 */
public class BudgetRepositoryTest extends AndroidTestCase {

    private BudgetRepository repository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        repository = new DBHelper(mContext, true);
    }

    public void testCreateDB() {
        SQLiteDatabase db = new DBHelper(mContext, true).getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    public void testAddBudget() {
        long budgetId1 = repository.addBudget("Bisaya");
        long budgetId2 = repository.addBudget("Ilocos");

        assertEquals(2, repository.getBudgets().size());
        assertBudget(repository.getBudget(budgetId1), "Bisaya", new BigDecimal("0.00"));
        assertBudget(repository.getBudget(budgetId2), "Ilocos", new BigDecimal("0.00"));
    }

    public void testAddBudgetDetail() {
        long budgetId1 = repository.addBudget("Bisaya");

        long budgetDetail1 = repository.addBudgetDetail(budgetId1, new Date(), TranType.PLUS.name(), "initial amount", new BigDecimal("1000.00"));
        long budgetDetail2 = repository.addBudgetDetail(budgetId1, new Date(), TranType.MINUS.name(), "fare", new BigDecimal("100.00"));
        long budgetDetail3 = repository.addBudgetDetail(budgetId1, new Date(), TranType.MINUS.name(), "food", new BigDecimal("50.00"));
        long budgetDetail4 = repository.addBudgetDetail(budgetId1, new Date(), TranType.MINUS.name(), "candy", new BigDecimal("70.00"));

        assertEquals(4, repository.getBudgetDetails(budgetId1).size());
        assertBudgetDetail(repository.getBudgetDetail(budgetDetail1), budgetId1, new Date(), TranType.PLUS.name(), "initial amount", new BigDecimal("1000.00"));
        assertBudgetDetail(repository.getBudgetDetail(budgetDetail2), budgetId1, new Date(), TranType.MINUS.name(), "fare", new BigDecimal("100.00"));
        assertBudgetDetail(repository.getBudgetDetail(budgetDetail3), budgetId1, new Date(), TranType.MINUS.name(), "food", new BigDecimal("50.00"));
        assertBudgetDetail(repository.getBudgetDetail(budgetDetail4), budgetId1, new Date(), TranType.MINUS.name(), "candy", new BigDecimal("70.00"));

        assertEquals(new BigDecimal("780.00"), repository.getTotalBudgetAmount(budgetId1));
        assertBudget(repository.getBudget(budgetId1), "Bisaya", new BigDecimal("780.00"));
    }

    public void testDeleteBudgetDetail() {
        long budgetId1 = repository.addBudget("Bisaya");

        long budgetDetail1 = repository.addBudgetDetail(budgetId1, new Date(), TranType.PLUS.name(), "initial amount", new BigDecimal("1000.00"));
        assertNotNull(repository.getBudgetDetail(budgetDetail1));

        repository.deleteBudgetDetail(budgetDetail1);
        assertNull(repository.getBudgetDetail(budgetDetail1));
    }

    public void testDeleteBudget() {
        // delete budget w/o detail
        long budgetId1 = repository.addBudget("Bisaya");
        assertNotNull(repository.getBudget(budgetId1));

        repository.deleteBudget(budgetId1);
        assertNull(repository.getBudget(budgetId1));

        // delete budget with detail
        budgetId1 = repository.addBudget("Bisaya");
        long budgetDetail1 = repository.addBudgetDetail(budgetId1, new Date(), TranType.PLUS.name(), "initial amount", new BigDecimal("1000.00"));
        assertNotNull(repository.getBudget(budgetId1));
        assertNotNull(repository.getBudgetDetail(budgetDetail1));
        assertEquals(1, repository.getBudgetDetails(budgetId1).size());

        repository.deleteBudget(budgetId1);
        assertNull(repository.getBudget(budgetId1));
        assertNull(repository.getBudgetDetail(budgetDetail1));
        assertEquals(0, repository.getBudgetDetails(budgetId1).size());
    }

    public void assertBudget(Budget budget, String description, BigDecimal totalAmount) {
        assertNotNull(budget.getId());
        assertEquals(description, budget.getDescription());
        assertEquals(totalAmount, budget.getTotalAmount());
    }

    public void assertBudgetDetail(BudgetDetail budgetDetail, Long budgetId, Date date, String type, String description, BigDecimal amount) {
        assertNotNull(budgetDetail.getId());
        assertEquals(budgetId, budgetDetail.getBudget().getId());
        assertEquals(date.toString(), budgetDetail.getDate().toString());
        assertEquals(type, budgetDetail.getType());
        assertEquals(description, budgetDetail.getDescription());
        assertEquals(amount, budgetDetail.getAmount());
    }

}
