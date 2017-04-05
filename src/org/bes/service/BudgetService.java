package org.bes.service;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.Budget;
import org.bes.model.repository.BudgetDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BudgetService {

    @Autowired
    private BudgetDAO budgetDAO;

    public List<Budget> getList(Budget condition) {
        return this.budgetDAO.find(condition, null, 0);
    }

    public List<Budget> getList(Budget condition, int page, int pageSize) {
        return this.budgetDAO.find(condition, (page - 1) * pageSize, pageSize);
    }

    public int getCount(Budget condition) {
        return this.budgetDAO.findCount(condition);
    }

    public double getCostCount(Budget condition) {
        return this.budgetDAO.findCostCount(condition);
    }

    public Budget get(String id) {
        return this.budgetDAO.findById(id);
    }

    public Budget getByCondition(Budget condition) {
        List<Budget> budgets = this.budgetDAO.find(condition, null, 0);
        if (budgets != null && !budgets.isEmpty()) {
            return budgets.get(0);
        }
        return null;
    }

    public List<Map<String, Object>> getSumByCategory(Map<String, String> params) {
        return this.budgetDAO.findSumByCategory(params);
    }

    public List<Map<String, Object>> getByMonth(Map<String, String> params) {
        return this.budgetDAO.findByMonth(params);
    }

    public List<Map<String, Object>> getByCategory(Map<String, String> params) {
        return this.budgetDAO.findByCategory(params);
    }

    public void save(Budget Budget) {
        if (StringUtils.isBlank(Budget.getId())) {
            Budget.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.budgetDAO.insert(Budget);
        } else {
            this.budgetDAO.update(Budget);
        }
    }

    public void delete(String id) {
        Budget Budget = this.budgetDAO.findById(id);
        if (Budget != null) {
            this.budgetDAO.delete(Budget);
        }
    }

}
