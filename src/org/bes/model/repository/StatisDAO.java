package org.bes.model.repository;


import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatisDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public List<Map<String, Object>> findCostAndBudgetByDept(Map<String, String> params) {
		Map<String, Object> parameters = new HashMap<>();

		String sql = "select d.*, a.ccount, b.bcount from dept d "
				+ "left join ( "
				+ "select c.deptId,sum(c.cost) ccount from cost c "
				+ "left join costCategory cc on c.categoryId = cc.id "
				+ "where c.audit=0 ";
		if (params != null) {
			if (StringUtils.isNotBlank(params.get("startTime"))) {
				sql += "and c.occurrenceTime >= :startTime ";
				parameters.put("startTime", params.get("startTime"));
			}
			if (StringUtils.isNotBlank(params.get("endTime"))) {
				sql += "and c.occurrenceTime <= :endTime ";
				parameters.put("endTime", params.get("endTime"));
			}
			if (StringUtils.isNotBlank(params.get("deptId"))) {
				sql += "and c.deptId = :deptId ";
				parameters.put("deptId", params.get("deptId"));
			}
			if (StringUtils.isNotBlank(params.get("ownerDeptId"))) {
				sql += "and cc.deptId = :ownerDeptId ";
				parameters.put("ownerDeptId", params.get("ownerDeptId"));
			}
		}
		sql += "group by c.deptId "
				+ ") a on d.deptId = a.deptId  "
				+ "left join ( "
				+ "select b.deptId,sum(b.cost) bcount from budget b "
				+ "left join costCategory cc on b.categoryId = cc.id "
				+ "where 1=1 ";
		if (params != null) {
			if (StringUtils.isNotBlank(params.get("startTime"))) {
				sql += "and b.time >= :startTime ";
				parameters.put("startTime", params.get("startTime"));
			}
			if (StringUtils.isNotBlank(params.get("endTime"))) {
				sql += "and b.time <= :endTime ";
				parameters.put("endTime", params.get("endTime"));
			}
			if (StringUtils.isNotBlank(params.get("deptId"))) {
				sql += "and b.deptId = :deptId ";
				parameters.put("deptId", params.get("deptId"));
			}
			if (StringUtils.isNotBlank(params.get("ownerDeptId"))) {
				sql += "and cc.deptId = :ownerDeptId ";
				parameters.put("ownerDeptId", params.get("ownerDeptId"));
			}
		}
		sql += "group by b.deptId "
				+ ") b on d.deptId = b.deptId where 1=1 ";
		if (params != null) {
			if (StringUtils.isNotBlank(params.get("deptId"))) {
				sql += "and d.deptId = :deptId ";
				parameters.put("deptId", params.get("deptId"));
			}
		}
		sql += "order by a.ccount desc,b.bcount desc";

		NativeQuery query = this.sessionFactory.getCurrentSession().createNativeQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (!parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				query.setParameter(key, parameters.get(key));
			}
		}
		return query.getResultList();
	}
	
	public List<Map<String, Object>> findCostAndBudgetByCategory(Map<String, String> params) {
		Map<String, Object> parameters = new HashMap<>();
		String sql = "select cc.*, a.ccount, b.bcount from costcategory cc "
				+ "left join ( "
				+ "select c.categoryId,sum(c.cost) ccount from cost c where c.audit=0 ";
		if (params != null) {
			if (StringUtils.isNotBlank(params.get("startTime"))) {
				sql += "and c.occurrenceTime >= :startTime ";
				parameters.put("startTime", params.get("startTime"));
			}
			if (StringUtils.isNotBlank(params.get("endTime"))) {
				sql += "and c.occurrenceTime <= :endTime ";
				parameters.put("endTime", params.get("endTime"));
			}
			if (StringUtils.isNotBlank(params.get("deptId"))) {
				sql += "and c.deptId = :deptId ";
				parameters.put("deptId", params.get("deptId"));
			}
		}
		sql += "group by c.categoryId "
				+ ") a on cc.id = a.categoryId  "
				+ "left join ( "
				+ "select b.categoryId,sum(b.cost) bcount from budget b where 1=1 ";
		if (params != null) {
			if (StringUtils.isNotBlank(params.get("startTime"))) {
				sql += "and b.time >= :startTime ";
				parameters.put("startTime", params.get("startTime"));
			}
			if (StringUtils.isNotBlank(params.get("endTime"))) {
				sql += "and b.time <= :endTime ";
				parameters.put("endTime", params.get("endTime"));
			}
			if (StringUtils.isNotBlank(params.get("deptId"))) {
				sql += "and b.deptId = :deptId ";
				parameters.put("deptId", params.get("deptId"));
			}
		}
		sql += "group by b.categoryId "
				+ ") b on cc.id = b.categoryId  "
				+ "order by a.ccount desc,b.bcount desc";


		NativeQuery query = this.sessionFactory.getCurrentSession().createNativeQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (!parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				query.setParameter(key, parameters.get(key));
			}
		}
		return query.getResultList();
	}
}
