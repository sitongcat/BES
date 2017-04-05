package org.bes.controller;

import org.apache.commons.lang3.StringUtils;
import org.bes.common.JsonUtils;
import org.bes.common.Page;
import org.bes.common.SessionUtils;
import org.bes.model.entity.Budget;
import org.bes.model.entity.User;
import org.bes.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/page")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @RequestMapping("/system/budget")
    public String index() {
        return "/page/system/budget/list";
    }

    @RequestMapping("/system/budget/{id}")
    @ResponseBody
    public String get(@PathVariable String id) {
        Budget budget = this.budgetService.get(id);
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", budget);
        return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/budget/list")
    @ResponseBody
    public String list(Budget budget, Page page) {
        if (page == null) {
            page = new Page();
        }
        User user = SessionUtils.getLoginInfo().getUser();
        if (StringUtils.isBlank(budget.getDeptId())) {
            if (!SessionUtils.isAdminOrFinancial()) {
                budget.setDeptId(user.getDeptId());
            }
        }

        int dataCount = this.budgetService.getCount(budget);
        page.setDataCount(dataCount);
        List<Budget> dataList = new ArrayList<>();
        if (dataCount > 0) {
            dataList = this.budgetService.getList(budget, page.getCurrentPage(), page.getPageSize());
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", dataList);
        resultMap.put("page", page);

        return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/budget/total")
    public String count(@RequestParam(name = "year") String year) {
        Budget condition = new Budget();
        if (StringUtils.isNotBlank(year)) {
            condition.setStartTime(year + "-01-01 00:00:00");
            condition.setEndTime(year + "-12-31 23:59:59");
        }
        double count = this.budgetService.getCostCount(condition);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", count);

        return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/budget/save")
    @ResponseBody
    public String save(Budget budget) {
        Budget condition = new Budget();
        condition.setCategoryId(budget.getCategoryId());
        condition.setDeptId(budget.getDeptId());
        condition.setTime(budget.getTime());
        Budget _budget = this.budgetService.getByCondition(condition);

        if (_budget != null) {
            if (StringUtils.isBlank(budget.getId())) {
                return "{\"status\": -1, \"message\": \"预算已设置(" + _budget.getCost() + ")\"}";
            }
        }

        this.budgetService.save(budget);
        return "{\"status\": 0, \"data\": \"" + budget.getId() + "\"}";
    }

    @RequestMapping("/system/budget/delete")
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        this.budgetService.delete(id);
        return "{\"status\": 0}";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}
