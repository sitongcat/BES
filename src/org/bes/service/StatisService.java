package org.bes.service;

import org.bes.model.repository.StatisDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisService {

	@Autowired
	private StatisDAO statisDAO;


	public List<Map<String, Object>> getCostAndBudgetByDept(Map<String, String> params) {
		return this.statisDAO.findCostAndBudgetByDept(params);
	}


	public List<Map<String, Object>> getCostAndBudgetByCategory(Map<String, String> params) {
		return this.statisDAO.findCostAndBudgetByCategory(params);
	}
}
