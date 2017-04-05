package org.bes.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bes.common.POIUtils;
import org.bes.model.entity.Budget;
import org.bes.model.entity.Cost;
import org.bes.model.entity.CostCategory;
import org.bes.model.entity.Dept;
import org.bes.service.BudgetService;
import org.bes.service.CostCategoryService;
import org.bes.service.CostService;
import org.bes.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Part;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/page")
public class DataImportController {

	@Autowired
	private DeptService deptService;
	@Autowired
	private CostCategoryService costCategoryService;
	@Autowired
	private BudgetService budgetService;
	@Autowired
	private CostService costService;

	@RequestMapping(value = "/system/dataImp")
	public String index() {
		return "/page/system/dataImp/dataImp";
	}

	@RequestMapping(value = "/system/data/imp", method = RequestMethod.POST)
	@ResponseBody
	public String imp(@RequestParam("file") Part file) {
		if (file == null) {
			return "{\"status\": -1, \"message\": \"文件错误.\"}";
		}

		Workbook workbook;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
		} catch (Exception e) {
			try {
				workbook = new HSSFWorkbook(file.getInputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
				return "{\"status\": -1, \"message\": \"文件错误.\"}";
			}
		}

		try {
			this.getBudgets(workbook);
			this.getCosts(workbook);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "{\"status\": 0}";
	}

	private List<Budget> getBudgets(Workbook workbook) {
		if (workbook == null) {
			return null;
		}
		Sheet sheet = workbook.getSheet("预算");
		if (sheet == null) {
			return null;
		}

		List<Budget> dataList = new ArrayList<>();
		try {
			DateFormat df = new SimpleDateFormat("yyyy");
			int rowCount = sheet.getLastRowNum();
			for (int i = 1; i <= rowCount; i++) {
				Row row = sheet.getRow(i);
				String categoryCode = POIUtils.getCellValue(row.getCell(0));
				String deptCode = POIUtils.getCellValue(row.getCell(2));
				String cost = POIUtils.getCellValue(row.getCell(4));
				String date = POIUtils.getDateCellValue(row.getCell(5), "yyyy");
				date = date.substring(0, date.indexOf("."));

				if (StringUtils.isBlank(categoryCode) || StringUtils.isBlank(deptCode)
						|| StringUtils.isBlank(cost) || StringUtils.isBlank(date)) {
					continue;
				}

				//System.out.println(categoryCode + "\t" + deptCode + "\t" + cost + "\t" + date);
				Budget budget = new Budget();
				budget.setCategoryCode(categoryCode);
				if (categoryCode.contains(".")) {
					budget.setCategoryCode(categoryCode.substring(0, categoryCode.lastIndexOf(".")));
				}
				budget.setDeptId(deptCode);
				budget.setCost(Double.parseDouble(cost));
				budget.setTime(df.parse(date));

				dataList.add(budget);
			}

			this.saveBudgets(dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	private List<Cost> getCosts(Workbook workbook) {
		if (workbook == null) {
			return null;
		}
		Sheet sheet = workbook.getSheet("执行");
		if (sheet == null) {
			return null;
		}

		List<Cost> dataList = new ArrayList<>();
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			int rowCount = sheet.getLastRowNum();
			for (int i = 1; i <= rowCount; i++) {
				Row row = sheet.getRow(i);
				String categoryCode = POIUtils.getCellValue(row.getCell(0));
				String deptCode = POIUtils.getCellValue(row.getCell(2));
				String _cost = POIUtils.getCellValue(row.getCell(4));
				String date = POIUtils.getDateCellValue(row.getCell(5), "yyyy-MM-dd");

				if (StringUtils.isBlank(categoryCode) || StringUtils.isBlank(deptCode)
						|| StringUtils.isBlank(_cost) || StringUtils.isBlank(date)) {
					continue;
				}

				//System.out.println(categoryCode + "\t" + deptCode + "\t" + _cost + "\t" + date);
				Cost cost = new Cost();
				cost.setCategoryCode(categoryCode);
				if (categoryCode.contains(".")) {
					cost.setCategoryCode(categoryCode.substring(0, categoryCode.lastIndexOf(".")));
				}
				cost.setDeptId(deptCode);
				cost.setCost(Double.parseDouble(_cost));
				cost.setOccurrenceTime(df.parse(date));
				cost.setAudit("0");

				dataList.add(cost);
			}

			this.saveCosts(dataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}

	private void saveBudgets(List<Budget> dataList) {
		if (dataList == null || dataList.isEmpty()) {
			return;
		}
		for (Budget budget : dataList) {
			Dept dept = this.deptService.getByDeptId(budget.getDeptId());
			if (dept == null) {
				continue;
			}
			budget.setDeptId(dept.getId());
			CostCategory costCategory = this.costCategoryService.getByCode(budget.getCategoryCode());
			if (costCategory == null) {
				continue;
			}
			budget.setCategoryId(costCategory.getId());


			List<Budget> datas = this.budgetService.getList(budget);
			if (datas != null && !datas.isEmpty()) {
				Budget _budget = datas.get(0);
				_budget.setCost(budget.getCost());
				budget = _budget;
			}

			this.budgetService.save(budget);
		}
	}

	private void saveCosts(List<Cost> dataList) {
		if (dataList == null || dataList.isEmpty()) {
			return;
		}
		for (Cost cost : dataList) {
			Dept dept = this.deptService.getByDeptId(cost.getDeptId());
			if (dept == null) {
				continue;
			}
			cost.setDeptId(dept.getId());
			CostCategory costCategory = this.costCategoryService.getByCode(cost.getCategoryCode());
			if (costCategory == null) {
				continue;
			}
			cost.setCategoryId(costCategory.getId());


			List<Cost> datas = this.costService.getList(cost);
			if (datas != null && !datas.isEmpty()) {
				Cost _cost = datas.get(0);
				_cost.setCost(cost.getCost());
				cost = _cost;
			}

			this.costService.save(cost);
		}
	}

}
