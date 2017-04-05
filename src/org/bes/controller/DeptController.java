package org.bes.controller;

import com.sun.beans.editors.IntegerEditor;
import org.apache.commons.lang3.StringUtils;
import org.bes.common.JsonUtils;
import org.bes.common.Page;
import org.bes.model.entity.Dept;
import org.bes.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/page")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @RequestMapping("/system/dept")
    public String index() {
        return "/page/system/dept/list";
    }

    @RequestMapping("/system/dept/{id}")
    @ResponseBody
    public String get(@PathVariable String id) {
        Dept dept = this.deptService.get(id);
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", dept);
        return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/dept/list")
    @ResponseBody
    public String list(Dept dept, @RequestParam(name = "currentPage", required = false) Integer currentPage,
                       @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        Page page = new Page();
        if (currentPage != null) {
            page.setCurrentPage(currentPage);
            page.setPageSize(pageSize == null ? 12 : pageSize);
        }
        int dataCount = this.deptService.getCount(dept);
        page.setDataCount(dataCount);
        List<Dept> dataList = new ArrayList<>();
        if (dataCount > 0) {
            if (currentPage != null) {
                dataList = this.deptService.getList(dept, page.getCurrentPage(), page.getPageSize());
            } else {
                dataList = this.deptService.getList(dept);
            }
        }
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("status", 0);
        resultMap.put("data", dataList);
        resultMap.put("page", page);

        return JsonUtils.fromObject(resultMap);
    }

    @RequestMapping("/system/dept/save")
    @ResponseBody
    public String save(Dept dept) {
        this.deptService.save(dept);
        return "{\"status\": 0, \"data\": \"" + dept.getId() + "\"}";
    }

    @RequestMapping("/system/dept/delete")
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        this.deptService.delete(id);
        return "{\"status\": 0}";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new IntegerEditor());
    }

}
