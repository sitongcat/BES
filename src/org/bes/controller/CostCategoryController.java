package org.bes.controller;

import org.bes.common.JsonUtils;
import org.bes.common.Page;
import org.bes.common.SessionUtils;
import org.bes.common.UserType;
import org.bes.model.entity.CostCategory;
import org.bes.model.entity.CostType;
import org.bes.model.entity.User;
import org.bes.service.CostCategoryService;
import org.bes.service.CostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/page")
public class CostCategoryController {

    @Autowired
    private CostCategoryService costCategoryService;
    @Autowired
    private CostTypeService costTypeService;

    @RequestMapping("/system/costCategory")
    public String index() {
        return "/page/system/costCategory/list";
    }

    @RequestMapping("/system/costCategory/{id}")
    @ResponseBody
    public String get(@PathVariable String id) {
        CostCategory costCategory = this.costCategoryService.get(id);
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", costCategory);
        return JsonUtils.fromObject(resultMap);
    }

    @GetMapping("/system/costCategory/datas")
    @ResponseBody
    public String datas(String rel) {
        User user = SessionUtils.getLoginInfo().getUser();

        List<Map<String, Object>> nodes = new ArrayList<>();
        List<CostType> costTypes = this.costTypeService.getList();
        costTypes.forEach((e) -> {
            List<CostCategory> costCategories = this.costCategoryService.getByParentId(e.getId());

            Map<String, Object> node = new LinkedHashMap<>();
            node.put("id", e.getId());
            node.put("text", e.getName());
            node.put("selectable", false);
            node.put("data", JsonUtils.fromObject(e));
            List<Map<String, String>> childNodes = new ArrayList<>();
            if (costCategories != null && costCategories.size() > 0) {
                costCategories.forEach((cc) -> {
                    if ("1".equals(rel) && (UserType.NORMAL == UserType.getType(user.getType())
                        || (UserType.DEPTADMIN == UserType.getType(user.getType())
                            && !user.getDeptId().equals(cc.getDeptId())))) {
                        return;
                    }

                    Map<String, String> node1 = new LinkedHashMap<>();
                    node1.put("id", cc.getId());
                    node1.put("text", cc.getName());
                    node1.put("data", JsonUtils.fromObject(cc));
                    childNodes.add(node1);
                });
            }
            if (childNodes.size() > 0) {
                node.put("nodes", childNodes);
            }
            nodes.add(node);
        });

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", nodes);
        return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/costCategory/list")
    @ResponseBody
    public String list(CostCategory costCategory, Page page) {
        if (page == null) {
            page = new Page();
        }
        int dataCount = this.costCategoryService.getCount(costCategory);
        page.setDataCount(dataCount);
        List<CostCategory> dataList = new ArrayList<>();
        if (dataCount > 0) {
            dataList = this.costCategoryService.getList(costCategory, page.getCurrentPage(), page.getPageSize());
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", dataList);
        resultMap.put("page", page);

        return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/costCategory/save")
    @ResponseBody
    public String save(CostCategory costCategory) {
        CostCategory category = this.costCategoryService.getByCode(costCategory.getCode());
        if (category != null) {
            return "{\"status\": -1, \"message\": \"费用项目" + costCategory.getCode() + "已存在.\"}";
        }

        this.costCategoryService.save(costCategory);
        return "{\"status\": 0, \"data\": \"" + costCategory.getId() + "\"}";
    }

    @RequestMapping("/system/costCategory/delete")
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        this.costCategoryService.delete(id);
        return "{\"status\": 0}";
    }

}
