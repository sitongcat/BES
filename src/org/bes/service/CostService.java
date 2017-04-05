package org.bes.service;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.Cost;
import org.bes.model.repository.BudgetDAO;
import org.bes.model.repository.CostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CostService {

    @Autowired
    private CostDAO costDAO;

    public List<Cost> getList(Cost condition) {
        return this.costDAO.find(condition, null, 0);
    }

    public List<Cost> getList(Cost condition, int page, int pageSize) {
        return this.costDAO.find(condition, (page - 1) * pageSize, pageSize);
    }

    public int getCount(Cost condition) {
        return this.costDAO.findCount(condition);
    }

    public Cost get(String id) {
        return this.costDAO.findById(id);
    }

	public Map<String, Object> getCostAndBudget(Map<String, String> params) {
		Map<String, Object> data = this.costDAO.findCostAndBudget(params);
		if (data == null) {
			data = new HashMap<>();
		}
		return data;
	}

	public List<Map<String, Object>> getCostAndBudgetAllYear(Map<String, String> params) {
		return this.costDAO.findCostAndBudgetAllYear(params);
	}

    public List<Map<String, Object>> getCostExecute(Map<String, String> params) {
        return this.costDAO.findCostExecute(params);
    }

    public void save(Cost cost) {
        if (StringUtils.isBlank(cost.getId())) {
            cost.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.costDAO.insert(cost);
        } else {
            this.costDAO.update(cost);
        }
    }

    public void delete(String id) {
        Cost cost = this.costDAO.findById(id);
        if (cost != null) {
            this.costDAO.delete(cost);
        }
    }

	public void doAudit(String id, String status) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(status)) {
            throw new NullPointerException("Incomplete data");
        }
        Cost cost = this.costDAO.findById(id);
        cost.setAudit(status);
        cost.setAuditTime(new Date());
        this.costDAO.update(cost);
	}
}
