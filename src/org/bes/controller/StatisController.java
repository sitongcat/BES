package org.bes.controller;


import org.bes.common.JsonUtils;
import org.bes.common.SessionUtils;
import org.bes.model.entity.Dept;
import org.bes.model.entity.User;
import org.bes.service.BudgetService;
import org.bes.service.CostService;
import org.bes.service.StatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/page/statis")
public class StatisController {

	@Autowired
	private BudgetService budgetService;
	@Autowired
	private CostService costService;
	@Autowired
	private StatisService statisService;


	@RequestMapping("/total/{year}")
	@ResponseBody
	public String total(@PathVariable String year) {
		User user = SessionUtils.getLoginInfo().getUser();
		String deptId = user.getDeptId();
		if (SessionUtils.isAdminOrFinancial()) {
			deptId = null;
		}

		Map<String, String> params = new HashMap<>();
		params.put("deptId", deptId);
		params.put("year", year);

		Map<String, Object> result = this.costService.getCostAndBudget(params);


		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", 0);
		resultMap.put("data", result);

		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/budget/sumByCategory")
	@ResponseBody
	public String byCategory(@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime) {
		User user = SessionUtils.getLoginInfo().getUser();
		String deptId = user.getDeptId();
		if (SessionUtils.isAdminOrFinancial()) {
			deptId = null;
		}

		Map<String, String> params = new HashMap<>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("deptId", deptId);
		params.put("audit", "0");

		List<Map<String, Object>> datas = this.budgetService.getSumByCategory(params);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", 0);
		resultMap.put("data", datas);

		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/getCostAndBudgetByDept")
	@ResponseBody
	public String getCostAndBudgetByDept(@RequestParam(name = "startTime", required = false) String startTime,
	                                     @RequestParam(name = "endTime", required = false) String endTime) {
		Map<String, String> params = new HashMap<>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		User user = SessionUtils.getLoginInfo().getUser();
		if (SessionUtils.isAdminOrFinancial()) {}
		else if (SessionUtils.isManagement()) {
			params.put("ownerDeptId", user.getDeptId());
		} else {
			params.put("deptId", user.getDeptId());
		}

		List<Map<String, Object>> datas = this.statisService.getCostAndBudgetByDept(params);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", 0);
		resultMap.put("data", datas);

		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/getCostAndBudgetByCategory")
	@ResponseBody
	public String getCostAndBudgetByCategory(@RequestParam(name = "startTime", required = false) String startTime,
	                                     @RequestParam(name = "endTime", required = false) String endTime) {
		Map<String, String> params = new HashMap<>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		User user = SessionUtils.getLoginInfo().getUser();
		Dept dept = SessionUtils.getLoginInfo().getDept();
		if (SessionUtils.isAdminOrFinancial()) {

		} else if ("1".equals(dept.getType()) && "1".equals(user.getType())) {

		} else {
			params.put("deptId", user.getDeptId());
		}

		List<Map<String, Object>> datas = this.statisService.getCostAndBudgetByCategory(params);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", 0);
		resultMap.put("data", datas);

		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/byCategoryMonth")
	@ResponseBody
	public String byCategoryMonth() {
		List<Map<String, Object>> budgets = this.budgetService.getByMonth(null);
		List<Map<String, Object>> costs = this.costService.getCostExecute(null);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", 0);
		resultMap.put("budgets", budgets);
		resultMap.put("costs", costs);

		return JsonUtils.fromObject(resultMap);
	}

}
