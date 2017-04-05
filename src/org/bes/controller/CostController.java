package org.bes.controller;

import org.apache.commons.lang3.StringUtils;
import org.bes.common.JsonUtils;
import org.bes.common.Page;
import org.bes.common.SessionUtils;
import org.bes.common.UserType;
import org.bes.model.entity.*;
import org.bes.service.BudgetService;
import org.bes.service.CostService;
import org.bes.service.CostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/page")
public class CostController {

    @Autowired
    private CostService costService;
    @Autowired
    private CostTypeService costTypeService;
    @Autowired
    private BudgetService budgetService;

    @RequestMapping("/system/cost")
    public String index() {
        return "/page/system/cost/list";
    }

    @RequestMapping("/system/cost/{id}")
    @ResponseBody
    public String get(@PathVariable String id) {
        Cost cost = this.costService.get(id);
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", cost);
        return JsonUtils.fromObject(resultMap);
    }

	@RequestMapping("/system/cost/count")
	@ResponseBody
    public String count(Cost cost) {
	    int dataCount = this.costService.getCount(cost);
	    Map<String, Object> resultMap = new LinkedHashMap<>();
	    resultMap.put("status", 0);
	    resultMap.put("data", dataCount);
	    return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/cost/list")
    @ResponseBody
    public String list(Cost cost, Page page) {
        if (page == null) {
            page = new Page();
        }

        User user = SessionUtils.getLoginInfo().getUser();
	    if (StringUtils.isBlank(cost.getDeptId())) {
		    if (!SessionUtils.isAdminOrFinancial()) {
			    cost.setDeptId(user.getDeptId());
		    } else if (SessionUtils.isManagement()) {
			    cost.setOwnerDeptId(user.getDeptId());
		    }
	    }

        /*if (UserType.NORMAL == UserType.getType(user.getType())) {
            cost.setDeptId(user.getDeptId());
        } else {
	        if (UserType.DEPTADMIN == UserType.getType(user.getType())) {
		        cost.setOwnerDeptId(user.getDeptId());
	        }
        }*/

        int dataCount = this.costService.getCount(cost);
        page.setDataCount(dataCount);
        List<Cost> dataList = new ArrayList<>();
        if (dataCount > 0) {
            dataList = this.costService.getList(cost, page.getCurrentPage(), page.getPageSize());
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", dataList);
        resultMap.put("page", page);

        return JsonUtils.fromObject(resultMap);
    }

	@RequestMapping("/system/cost/exec")
	public String costExecutePage() {
		return "/page/system/cost/execute";
	}

	@RequestMapping("/system/cost/execAlarm")
	public String costExecAlarm() {
		return "/page/system/cost/execAlarm";
	}

    @RequestMapping("/system/cost/execute")
    @ResponseBody
    public String costExecute(String startTime, String endTime, String deptId, String ownerDeptId) {
        Map<String, String> params = new HashMap<>();
	    params.put("startTime", startTime);
	    params.put("endTime", endTime);
	    params.put("deptId", deptId);
	    params.put("ownerDeptId", ownerDeptId);

        User loginUser = SessionUtils.getLoginInfo().getUser();
        Dept dept = SessionUtils.getLoginInfo().getDept();

        /*if (StringUtils.isBlank(deptId) && StringUtils.isBlank(ownerDeptId)) {
	        params.put("deptId", loginUser.getDeptId());
        }*/

        /*if ("0".equals(dept.getType())) {

        } else if ("1".equals(dept.getType())) {
            if (UserType.getType(loginUser.getType()) == UserType.DEPTADMIN) {
                params.put("ownerDeptId", loginUser.getDeptId());
            } else {
                params.put("deptId", loginUser.getDeptId());
            }
        } else {
            params.put("deptId", loginUser.getDeptId());
        }

        if (loginUser.getDeptId().equals(params.get("deptId"))) {
        	params.remove("ownerDeptId");
        }*/

        /*switch (UserType.getType(loginUser.getType())) {
            case DEPTADMIN:
	            params.put("ownerDeptId", loginUser.getDeptId());
	            break;
            case NORMAL:
	            params.put("deptId", loginUser.getDeptId());
                break;
        }*/

        List<Map<String, Object>> costList = this.costService.getCostExecute(params);

        params.remove("startTime");
        params.remove("endTime");
	    params.put("time", startTime.substring(0, 4));
	    List<Map<String, Object>> budgetList = this.budgetService.getByCategory(params);

        List<CostType> costTypes = this.costTypeService.getList();
	    Map<String, Object> costMap = new LinkedHashMap<>();
        Map<String, Object> budgetMap = new LinkedHashMap<>();

        costTypes.forEach((e) -> {
            //costTypeList.add(e);

            List<Map<String, Object>> _costList = new ArrayList<>();
            costMap.put(e.getId(), _costList);
            for (Map<String, Object> c : costList) {
                if (c.get("parentId") != null && e.getId().equals(c.get("parentId").toString())) {
                    _costList.add(c);
                }
            }

            /*List<Map<String, Object>> _budgetList = new ArrayList<>();
            budgetMap.put(e.getId(), _budgetList);
            for (Map<String, Object> c : budgetList) {
                if (c.get("parentId") != null && e.getId().equals(c.get("parentId").toString())) {
                    _budgetList.add(c);
                }
            }*/

            for (Map<String, Object> c : budgetList) {
                budgetMap.put(c.get("code").toString(), c);
            }
        });

        Map<String, Object> datas = new LinkedHashMap<>();
        datas.put("costTypes", costTypes);
        datas.put("costs", costMap);
        datas.put("budgets", budgetMap);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", datas);

        return JsonUtils.fromObject(resultMap);
    }

	@RequestMapping("/system/cost/getCostAndBudget")
	@ResponseBody
    public String getCostAndBudget(String categoryId, String deptId, String year) {
		Map<String, String> params = new HashMap<>();
		params.put("categoryId", categoryId);
		params.put("deptId", deptId);
		params.put("year", year);

    	Map<String, Object> data = this.costService.getCostAndBudget(params);
		return "{\"status\": 0, \"data\": " + JsonUtils.fromObject(data) + "}";
    }

	@RequestMapping("/system/cost/getCostAndBudgetAllYear")
	@ResponseBody
	public String getCostAndBudgetAllYear(String year) {
    	if (StringUtils.isBlank(year)) {
    		year = Calendar.getInstance().get(Calendar.YEAR) + "";
	    }

	    Map<String, String> params = new HashMap<>();
    	params.put("year", year);

		User user = SessionUtils.getLoginInfo().getUser();

		if (SessionUtils.isManagement()) {
			params.put("ownerDeptId", user.getDeptId());
		} else if (!SessionUtils.isAdminOrFinancial()) {
			params.put("deptId", user.getDeptId());
		}

		List<Map<String, Object>> data = this.costService.getCostAndBudgetAllYear(params);
		return "{\"status\": 0, \"data\": " + JsonUtils.fromObject(data) + "}";
	}

    @RequestMapping("/system/cost/save")
    @ResponseBody
    public String save(Cost cost) {
        this.costService.save(cost);
        return "{\"status\": 0, \"data\": \"" + cost.getId() + "\"}";
    }

    @RequestMapping("/system/cost/delete")
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        this.costService.delete(id);
        return "{\"status\": 0}";
    }

    @RequestMapping("/system/cost/audit")
    @ResponseBody
    public String audit(@RequestParam("id") String id, @RequestParam("status") String status) {
        this.costService.doAudit(id, status);
        return "{\"status\": 0}";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    }
}
