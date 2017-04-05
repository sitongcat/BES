package org.bes.model.repository;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.Cost;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.tool.schema.internal.StandardTableExporter;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.lang.reflect.Executable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class CostDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Cost findById(String id) {
        String hql = "select c.*,cc.code categoryCode, cc.name categoryName,d.deptName from Cost c "
                + "left join CostCategory cc on c.categoryId=cc.id "
                + "left join Dept d on c.deptId = d.id where c.id = ? ";
        Session session = this.sessionFactory.getCurrentSession();
        NativeQuery query = session.createNativeQuery(hql);
        query.setResultTransformer(Transformers.aliasToBean(Cost.class));
        query.setParameter(1, id);
        return (Cost)query.getSingleResult();
    }

    public List<Cost> find(Cost cost, Integer firstResult, Integer maxResults) {
        Map<String, Object> params = new LinkedHashMap<>();
        String hql = "select c.*,cc.code categoryCode, cc.name categoryName,d.deptName from Cost c "
                + "left join CostCategory cc on c.categoryId=cc.id "
                + "left join Dept d on c.deptId = d.id where 1=1 ";
        if (cost != null) {
            if (StringUtils.isNotBlank(cost.getCategoryId())) {
                hql += " and categoryId = :categoryId ";
                params.put("categoryId", cost.getCategoryId());
            }
            if (StringUtils.isNotBlank(cost.getDeptId())) {
                hql += " and c.deptId = :deptId ";
                params.put("deptId", cost.getDeptId());
            }
            if (StringUtils.isNotBlank(cost.getAudit())) {
                hql += " and c.audit = :audit ";
                params.put("audit", cost.getAudit());
            }
	        if (StringUtils.isNotBlank(cost.getOwnerDeptId())) {
		        hql += "and cc.deptId = :ownerDeptId ";
		        params.put("ownerDeptId", cost.getOwnerDeptId());
	        }
	        if (cost.getOccurrenceTime() != null) {
		        hql += " and DATE_FORMAT(c.OccurrenceTime, '%Y-%m-%d') = :time ";
		        params.put("time", (new SimpleDateFormat("yyyy-MM-dd")).format(cost.getOccurrenceTime()));
	        }
        }
        hql += " order by cc.code, c.deptId";
        Session session = this.sessionFactory.getCurrentSession();
        NativeQuery query = session.createNativeQuery(hql);
        query.setResultTransformer(Transformers.aliasToBean(Cost.class));
        if (!params.isEmpty()) {
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                query.setParameter(key, params.get(key));
            }
        }
        if (firstResult != null) {
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults == null ? 10 : maxResults);
        }
        return query.getResultList();
    }

    public int findCount(Cost cost) {
        Map<String, Object> params = new LinkedHashMap<>();
        String hql = "select count(*) from Cost c "
		        + "left join CostCategory cc on c.categoryId=cc.id "
		        + "where 1=1 ";
        if (cost != null) {
            if (StringUtils.isNotBlank(cost.getCategoryId())) {
                hql += " and c.categoryId = :categoryId ";
                params.put("categoryId", cost.getCategoryId());
            }
            if (StringUtils.isNotBlank(cost.getDeptId())) {
                hql += " and c.deptId = :deptId ";
                params.put("deptId", cost.getDeptId());
            }
            if (StringUtils.isNotBlank(cost.getAudit())) {
                hql += " and c.audit = :audit ";
                params.put("audit", cost.getAudit());
            }
	        if (StringUtils.isNotBlank(cost.getOwnerDeptId())) {
		        hql += "and cc.deptId = :ownerDeptId ";
		        params.put("ownerDeptId", cost.getOwnerDeptId());
	        }
	        if (cost.getOccurrenceTime() != null) {
		        hql += " and DATE_FORMAT(c.OccurrenceTime, '%Y-%m-%d') = :time ";
		        params.put("time", (new SimpleDateFormat("yyyy-MM-dd")).format(cost.getOccurrenceTime()));
	        }
        }
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql, Long.class);
        if (!params.isEmpty()) {
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                query.setParameter(key, params.get(key));
            }
        }
        return ((Number)query.getSingleResult()).intValue();
    }

    public Map<String, Object> findCostAndBudget(Map<String, String> params) {
    	String sql_a = "select sum(c.cost) cost from cost c where 1=1 ";
    	String sql_b = "select sum(b.cost) budget from budget b where 1=1 ";
	    Map<String, Object> parameters = new HashMap<>();
    	if (params != null) {
    		if (StringUtils.isNotBlank(params.get("audit"))) {
    			sql_a += "and c.audit = :audit ";
    			parameters.put("audit", params.get("audit"));
		    }
    		if (StringUtils.isNotBlank(params.get("categoryId"))) {
			    sql_a += "and c.categoryId = :categoryId ";
			    sql_b += "and b.categoryId = :categoryId ";
			    parameters.put("categoryId", params.get("categoryId"));
		    }
		    if (StringUtils.isNotBlank(params.get("deptId"))) {
			    sql_a += "and c.deptId = :deptId ";
			    sql_b += "and b.deptId = :deptId ";
			    parameters.put("deptId", params.get("deptId"));
		    }
		    if (StringUtils.isNotBlank(params.get("year"))) {
			    sql_a += "and DATE_FORMAT(c.occurrenceTime, '%Y')  = :year ";
			    sql_b += "and DATE_FORMAT(b.time, '%Y')  = :year ";
			    parameters.put("year", params.get("year"));
		    }
	    }
	    String sql = "select a1.cost,b1.budget from (" + sql_a + ") a1, (" + sql_b + ") b1";
    	try {
		    NativeQuery query = this.sessionFactory.getCurrentSession().createNativeQuery(sql);
		    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		    if (!parameters.isEmpty()) {
			    for (String key : parameters.keySet()) {
				    query.setParameter(key, parameters.get(key));
			    }
		    }
		    return (Map<String, Object>)query.getSingleResult();
	    } catch (Exception e) {
    		e.printStackTrace();
	    }
	    return null;
    }

	/**
	 * 查找累计金额超过全年预算80%的项目
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findCostAndBudgetAllYear(Map<String, String> params) {
    	String sql = "select a1.categoryId,cc.code costCategoryCode,cc.name as costCategoryName, a1.deptId, d.deptName, a1.cost,b1.budget from ("
			    + "select c.categoryId,c.deptId,sum(c.cost) as cost from cost c "
			    + "left join costCategory cc on c.categoryId=cc.id where 1=1 ";

		Map<String, Object> parameters = new HashMap<>();
		if (params != null) {
			if (StringUtils.isNotBlank(params.get("year"))) {
				sql += "and DATE_FORMAT(c.occurrenceTime, '%Y') = :year ";
				parameters.put("year", params.get("year"));
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
		sql += "group by c.categoryId,c.deptId "
			    + ") a1 left join "
			    + "(select b.categoryId,b.deptId,sum(b.cost) as budget from budget b "
			    + "left join costCategory cc on b.categoryId=cc.id where 1=1 ";

		if (params != null) {
			if (StringUtils.isNotBlank(params.get("year"))) {
				sql += "and DATE_FORMAT(b.time, '%Y') = :year ";
				parameters.put("year", params.get("year"));
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
		sql += "group by b.categoryId,b.deptId "
			    + ") b1 on (a1.categoryId = b1.categoryId and a1.deptId = b1.deptId) "
			    + "left join CostCategory cc on a1.categoryId = cc.id "
			    + "left join dept d on a1.deptId = d.id "
			    + "where a1.cost / IFNULL(b1.budget,0.01) >= 0.8 "
			    + "order by a1.deptId,a1.categoryId";

	    NativeQuery query = this.sessionFactory.getCurrentSession().createNativeQuery(sql);
	    query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (!parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				query.setParameter(key, parameters.get(key));
			}
		}
	    return query.getResultList();
    }

    public List<Map<String, Object>> findCostExecute(Map<String, String> params) {
        String sql = "select c0.*,d.deptName,c1.d0,c1.d1,c1.d2,c1.d3,c1.d4,c1.d5,c1.d6,c1.d7,c1.d8,c1.d9,c1.d10,c1.d11,"
                + "c1.count from costcategory c0 left join ("
                + "select cc.code, cc.name,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '01' then c.cost else 0 end) d0,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '02' then c.cost else 0 end) d1,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '03' then c.cost else 0 end) d2,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '04' then c.cost else 0 end) d3,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '05' then c.cost else 0 end) d4,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '06' then c.cost else 0 end) d5,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '07' then c.cost else 0 end) d6,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '08' then c.cost else 0 end) d7,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '09' then c.cost else 0 end) d8,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '10' then c.cost else 0 end) d9,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '11' then c.cost else 0 end) d10,"
                + "SUM(case when DATE_FORMAT(c.occurrenceTime, '%m') = '12' then c.cost else 0 end) d11,"
		        + "SUM(c.cost) count "
                + "from cost c "
                + "left join costcategory cc on c.categoryId = cc.id "
                + "where c.audit=0 ";
        Map<String, Object> parameters = new HashMap<>();
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
        sql += "group by cc.code, cc.name) c1 on c0.code = c1.code ";
        sql += "left join dept d on c0.deptid = d.id where 1=1 ";
        if (params != null) {
            if (StringUtils.isNotBlank(params.get("ownerDeptId"))) {
                sql += "and c0.deptId = :ownerDeptId ";
                parameters.put("ownerDeptId", params.get("ownerDeptId"));
            }
        }
        sql += "order by c0.parentId,c0.deptId,c0.name";

        NativeQuery query = this.sessionFactory.getCurrentSession().createNativeQuery(sql);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (!parameters.isEmpty()) {
            for (String key : parameters.keySet()) {
                query.setParameter(key, parameters.get(key));
            }
        }
        return query.getResultList();
    }

    public void insert(Cost instance) {
        this.sessionFactory.getCurrentSession().save(instance);
    }

    public void update(Cost instance) {
        this.sessionFactory.getCurrentSession().update(instance);
    }

    public void delete(Cost instance) {
        this.sessionFactory.getCurrentSession().delete(instance);
    }

}
