package org.bes.controller;

import org.apache.commons.lang3.StringUtils;
import org.bes.common.JsonUtils;
import org.bes.model.entity.CostType;
import org.bes.service.CostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/page")
public class CostTypeController {

	@Autowired
	private CostTypeService costTypeService;

	@RequestMapping("/system/costType")
	public String index() {
		return "/page/system/costType/list";
	}

	@RequestMapping("/system/costType/{id}")
	@ResponseBody
	public String get(@PathVariable String id) {
		CostType costType = this.costTypeService.get(id);
		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("status", 0);
		resultMap.put("data", costType);
		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/system/costType/list")
	@ResponseBody
	public String list(CostType costType) {
		List<CostType> dataList = this.costTypeService.getList();

		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("status", 0);
		resultMap.put("data", dataList);

		return JsonUtils.fromObject(resultMap);
	}

	@RequestMapping("/system/costType/save")
	@ResponseBody
	public String save(CostType costType) {
		if (StringUtils.isBlank(costType.getId())) {
			CostType _costType = this.costTypeService.getByName(costType.getName());
			if (_costType != null) {
				return "{\"status\": 1, \"message\": \"类别(" + costType.getName() + ")已存在!\"}";
			}
		}
		if (costType.getSort() == null) {
			costType.setSort(99);
		}
		this.costTypeService.save(costType);
		return "{\"status\": 0}";
	}

	@RequestMapping("/system/costType/delete")
	@ResponseBody
	public String delete(String id) {
		this.costTypeService.delete(id);
		return "{\"status\": 0}";
	}

}
