package org.bes.model.repository;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.CostCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Repository
public class CostCategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public CostCategory findById(String id) {
		return this.sessionFactory.getCurrentSession().get(CostCategory.class, id);
	}

	public CostCategory findByCode(String code) {
		Map<String, Object> params = new LinkedHashMap<>();
		String hql = "select c.*,d.deptName from CostCategory c left join dept d on c.deptId = d.id where c.code = ?";
		Session session = this.sessionFactory.getCurrentSession();
		NativeQuery query = session.createNativeQuery(hql);
		query.setParameter(1, code);
		query.setResultTransformer(Transformers.aliasToBean(CostCategory.class));

		List<CostCategory> dataList = query.getResultList();
		if (dataList != null && !dataList.isEmpty()) {
			return dataList.get(0);
		}
		return null;
	}

	public List<CostCategory> find(CostCategory costCategory, int firstResult, int maxResults) {
		Map<String, Object> params = new LinkedHashMap<>();
		String hql = "select c.*,d.deptName from CostCategory c left join dept d on c.deptId = d.id where 1=1 ";
		if (costCategory != null) {
			if (StringUtils.isNotBlank(costCategory.getCode())) {
				hql += " and code like :code ";
				params.put("code", "%" + costCategory.getCode() + "%");
			}
			if (StringUtils.isNotBlank(costCategory.getName())) {
				hql += " and name like :name ";
				params.put("name", "%" + costCategory.getName() + "%");
			}
			if (StringUtils.isNotBlank(costCategory.getParentId())) {
				hql += " and parentId = :parentId ";
				params.put("parentId", costCategory.getParentId());
			}
		}
		hql += " order by deptId, name";
		Session session = this.sessionFactory.getCurrentSession();
		NativeQuery query = session.createNativeQuery(hql);
		query.setResultTransformer(Transformers.aliasToBean(CostCategory.class));
		if (!params.isEmpty()) {
			Iterator<String> keys = params.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				query.setParameter(key, params.get(key));
			}
		}
		if (firstResult >= 0) {
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	public int findCount(CostCategory costCategory) {
		Map<String, Object> params = new LinkedHashMap<>();
		String hql = "select count(*) from CostCategory where 1=1 ";
		if (costCategory != null) {
			if (StringUtils.isNotBlank(costCategory.getCode())) {
				hql += " and code like :code ";
				params.put("code", "%" + costCategory.getCode() + "%");
			}
			if (StringUtils.isNotBlank(costCategory.getName())) {
				hql += " and name like :name ";
				params.put("name", "%" + costCategory.getName() + "%");
			}
			if (StringUtils.isNotBlank(costCategory.getParentId())) {
				hql += " and parentId = :parentId ";
				params.put("parentId", costCategory.getParentId());
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

	public void insert(CostCategory instance) {
		this.sessionFactory.getCurrentSession().save(instance);
	}

	public void update(CostCategory instance) {
		this.sessionFactory.getCurrentSession().update(instance);
	}

	public void delete(CostCategory instance) {
		this.sessionFactory.getCurrentSession().delete(instance);
	}
	
}
