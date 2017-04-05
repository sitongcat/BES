package org.bes.task;

import org.bes.model.entity.CostType;
import org.bes.model.entity.Dept;
import org.bes.model.entity.User;
import org.bes.service.CostTypeService;
import org.bes.service.DeptService;
import org.bes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AppInitTask {

	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	@Autowired
	private CostTypeService costTypeService;

	@PostConstruct
	public void init() {
		initUserAndDept();
		initCostType();

		System.out.println("系统已初始化.");
	}

	private void initUserAndDept() {
		String deptId = "ADMIN";
		Dept dept = new Dept();
		dept.setDeptId(deptId);
		dept.setDeptName("系统维护");
		dept.setType("0");

		User user = new User();
		user.setUserName("admin");
		user.setName("系统管理员");
		user.setPassword("123456");
		user.setType("0");

		try {
			Dept _dept = deptService.getByDeptId(deptId);
			if (_dept == null) {
				deptService.save(dept);
			} else {
				dept = _dept;
			}
			if (userService.getByUserName(user.getUserName()) == null) {
				user.setDeptId(dept.getId());
				userService.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initCostType() {
		String[] datas = {"其他运杂费","职工薪酬","日常操作性支出","对外协议支出",
				"公务性支出","税费性支出","其他","财务费用"};

		try {
			for (int i = 0; i < datas.length; i++) {
				if (this.costTypeService.getByName(datas[i]) != null) {
					continue;
				}
				CostType costType = new CostType();
				//costType.setId(data[0]);
				costType.setName(datas[i]);
				costType.setSort(i);
				this.costTypeService.save(costType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
