package org.bes.model.repository;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.Budget;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class BudgetDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Budget findById(String id) {
        String hql = "select c.*,cc.code categoryCode, cc.name categoryName,"
                + "d.deptName from Budget c "
                + "left join CostCategory cc on c.categoryId=cc.id "
                + "left join Dept d on c.deptId = d.id where c.id = ?";
        Session session = this.sessionFactory.getCurrentSession();
        NativeQuery query = session.createNativeQuery(hql);
        query.setResultTransformer(Transformers.aliasToBean(Budget.class));
        query.setParameter(1, id);
        return (Budget)query.getSingleResult();
    }

    public List<Budget> find(Budget budget, Integer firstResult, Integer maxResults) {
        Map<String, Object> params = new LinkedHashMap<>();
        String hql = "select b.*,cc.code categoryCode, cc.name categoryName,"
                + "d.deptName from Budget b "
                + "left join CostCategory cc on b.categoryId=cc.id "
                + "left join Dept d on b.deptId = d.id where 1=1 ";
        if (budget != null) {
            if (StringUtils.isNotBlank(budget.getCategoryId())) {
                hql += " and b.categoryId = :categoryId ";
                params.put("categoryId", budget.getCategoryId());
            }
            if (StringUtils.isNotBlank(budget.getDeptId())) {
                hql += " and b.deptId = :deptId ";
                params.put("deptId", budget.getDeptId());
            }
            if (StringUtils.isNotBlank(budget.getOwnerDeptId())) {
                hql += " and cc.deptId = :ownerDeptId ";
                params.put("ownerDeptId", budget.getOwnerDeptId());
            }
	        if (StringUtils.isNotBlank(budget.getStartTime())) {
		        hql += " and b.time >= :startTime ";
		        params.put("startTime", budget.getStartTime());
	        }
	        if (StringUtils.isNotBlank(budget.getEndTime())) {
		        hql += " and b.time <= :endTime ";
		        params.put("startTime", budget.getEndTime());
	        }
        }
        hql += " order by cc.code, b.deptId";
        Session session = this.sessionFactory.getCurrentSession();
        NativeQuery query = session.createNativeQuery(hql);
        query.setResultTransformer(Transformers.aliasToBean(Budget.class));
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

    public int findCount(Budget budget) {
        Map<String, Object> params = new LinkedHashMap<>();
        String sql = "select count(*) from Budget b "
                + "left join CostCategory cc on b.categoryId=cc.id "
                + "where 1=1 ";
        if (budget != null) {
            if (StringUtils.isNotBlank(budget.getCategoryId())) {
	            sql += " and b.categoryId = :categoryId ";
                params.put("categoryId", budget.getCategoryId());
            }
            if (StringUtils.isNotBlank(budget.getDeptId())) {
	            sql += " and b.deptId = :deptId ";
                params.put("deptId", budget.getDeptId());
            }
            if (StringUtils.isNotBlank(budget.getOwnerDeptId())) {
	            sql += " and cc.deptId = :ownerDeptId ";
                params.put("ownerDeptId", budget.getOwnerDeptId());
            }
            if (StringUtils.isNotBlank(budget.getStartTime())) {
	            sql += " and b.time >= :startTime ";
	            params.put("startTime", budget.getStartTime());
            }
	        if (StringUtils.isNotBlank(budget.getEndTime())) {
		        sql += " and b.time <= :endTime ";
		        params.put("startTime", budget.getEndTime());
	        }
	        if (budget.getTime() != null) {
		        sql += " and DATE_FORMAT(b.time, '%Y') = :time ";
		        params.put("time", (new SimpleDateFormat("yyyy")).format(budget.getTime()));
	        }
        }
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createNativeQuery(sql);
        if (!params.isEmpty()) {
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                query.setParameter(key, params.get(key));
            }
        }
        return ((Number)query.getSingleResult()).intValue();
    }

    public double findCostCount(Budget budget) {
	    Map<String, Object> params = new LinkedHashMap<>();
	    String sql = "select sum(b.cost) from Budget b "
			    + "left join CostCategory cc on b.categoryId=cc.id "
			    + "where 1=1 ";
	    if (budget != null) {
		    if (StringUtils.isNotBlank(budget.getCategoryId())) {
			    sql += " and b.categoryId = :categoryId ";
			    params.put("categoryId", budget.getCategoryId());
		    }
		    if (StringUtils.isNotBlank(budget.getDeptId())) {
			    sql += " and b.deptId = :deptId ";
			    params.put("deptId", budget.getDeptId());
		    }
		    if (StringUtils.isNotBlank(budget.getOwnerDeptId())) {
			    sql += " and cc.deptId = :ownerDeptId ";
			    params.put("ownerDeptId", budget.getOwnerDeptId());
		    }
		    if (StringUtils.isNotBlank(budget.getStartTime())) {
			    sql += " and b.time >= :startTime ";
			    params.put("startTime", budget.getStartTime());
		    }
		    if (StringUtils.isNotBlank(budget.getEndTime())) {
			    sql += " and b.time <= :endTime ";
			    params.put("startTime", budget.getEndTime());
		    }
		    if (budget.getTime() != null) {
		    	sql += " and DATE_FORMAT(b.time, '%Y') = :time ";
		    	params.put("time", (new SimpleDateFormat("yyyy")).format(budget.getTime()));
		    }
	    }
	    Session session = this.sessionFactory.getCurrentSession();
	    Query query = session.createNativeQuery(sql, Double.class);
	    if (!params.isEmpty()) {
		    Iterator<String> keys = params.keySet().iterator();
		    while (keys.hasNext()) {
			    String key = keys.next();
			    query.setParameter(key, params.get(key));
		    }
	    }
	    return ((Number)query.getSingleResult()).doubleValue();
    }

	public List<Map<String, Object>> findSumByCategory(Map<String, String> params) {
    	String sql = "select cc.*, b.count from costcategory cc "
			    + "left join ( "
			    + "select b.categoryId, sum(b.cost) count from budget b where 1=1 ";

		Map<String, Object> parameters = new HashMap<>();
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
			    + ") b on cc.id = b.categoryId "
			    + "order by b.count desc";

		NativeQuery query = this.sessionFactory.getCurrentSession().createNativeQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (!parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				query.setParameter(key, parameters.get(key));
			}
		}
		return query.getResultList();
	}

    public List<Map<String, Object>> findByMonth(Map<String, String> params) {
        String sql = "select c0.*,d.deptName,c1.d0,c1.d1,c1.d2,c1.d3,c1.d4,c1.d5,c1.d6,c1.d7,c1.d8,c1.d9,c1.d10,c1.d11,"
                + "c1.count from costcategory c0 left join ("
                + "select cc.code, cc.name,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '01' then c.cost else 0 end) d0,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '02' then c.cost else 0 end) d1,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '03' then c.cost else 0 end) d2,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '04' then c.cost else 0 end) d3,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '05' then c.cost else 0 end) d4,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '06' then c.cost else 0 end) d5,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '07' then c.cost else 0 end) d6,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '08' then c.cost else 0 end) d7,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '09' then c.cost else 0 end) d8,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '10' then c.cost else 0 end) d9,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '11' then c.cost else 0 end) d10,"
                + "SUM(case when DATE_FORMAT(c.time, '%m') = '12' then c.cost else 0 end) d11,"
                + "SUM(c.cost) count "
                + "from budget c "
                + "left join costcategory cc on c.categoryId = cc.id "
                + "where 1=1 ";
        Map<String, Object> parameters = new HashMap<>();
        if (params != null) {
            if (StringUtils.isNotBlank(params.get("startTime"))) {
                sql += "and c.time >= :startTime ";
                parameters.put("startTime", params.get("startTime"));
            }
            if (StringUtils.isNotBlank(params.get("endTime"))) {
                sql += "and c.time <= :endTime ";
                parameters.put("endTime", params.get("endTime"));
            }
            if (StringUtils.isNotBlank(params.get("deptId"))) {
                sql += "and c.deptId = :deptId ";
                parameters.put("deptId", params.get("deptId"));
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


	public List<Map<String, Object>> findByCategory(Map<String, String> params) {
		String sql = "select c0.*,d.deptName,c1.cost "
				+ "from costcategory c0 left join ("
				+ "select cc.code, cc.name,sum(c.cost) as cost "
				+ "from budget c "
				+ "left join costcategory cc on c.categoryId = cc.id "
				+ "where 1=1 ";
		Map<String, Object> parameters = new HashMap<>();
		if (params != null) {
			if (StringUtils.isNotBlank(params.get("startTime"))) {
				sql += "and c.time >= :startTime ";
				parameters.put("startTime", params.get("startTime"));
			}
			if (StringUtils.isNotBlank(params.get("endTime"))) {
				sql += "and c.time <= :endTime ";
				parameters.put("endTime", params.get("endTime"));
			}
			if (StringUtils.isNotBlank(params.get("deptId"))) {
				sql += "and c.deptId = :deptId ";
				parameters.put("deptId", params.get("deptId"));
			}
			if (StringUtils.isNotBlank(params.get("time"))) {
				sql += " and DATE_FORMAT(c.time, '%Y') = :time ";
				parameters.put("time", params.get("time"));
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

    public void insert(Budget instance) {
        this.sessionFactory.getCurrentSession().save(instance);
    }

    public void update(Budget instance) {
        this.sessionFactory.getCurrentSession().update(instance);
    }

    public void delete(Budget instance) {
        this.sessionFactory.getCurrentSession().delete(instance);
    }

}
