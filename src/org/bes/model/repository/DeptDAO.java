package org.bes.model.repository;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.Dept;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DeptDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Dept findById(String id) {
        return this.sessionFactory.getCurrentSession().get(Dept.class, id);
    }

    public Dept findByDeptId(String deptId) {
        String sql = "from Dept where deptId = ?";
        Query query = this.sessionFactory.getCurrentSession().createQuery(sql, Dept.class);
        query.setParameter(0, deptId);
        List<Dept> list = query.getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<Dept> find(Dept dept, Integer firstResult, Integer maxResults) {
        Map<String, Object> params = new LinkedHashMap<>();
        String hql = "from Dept where 1=1 ";
        if (dept != null) {
            if (StringUtils.isNotBlank(dept.getDeptId())) {
                hql += " and deptId like :deptId ";
                params.put("deptId", dept.getDeptId());
            }
            if (StringUtils.isNotBlank(dept.getDeptName())) {
                hql += " and deptName like :deptName ";
                params.put("deptName", "%" + dept.getDeptName() + "%");
            }
        }
        hql += " order by deptName";
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql, Dept.class);
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

    public int findCount(Dept dept) {
        Map<String, Object> params = new LinkedHashMap<>();
        String hql = "select count(*) from Dept where 1=1 ";
        if (dept != null) {
            if (StringUtils.isNotBlank(dept.getDeptId())) {
                hql += " and deptId like :deptId ";
                params.put("deptId", dept.getDeptId());
            }
            if (StringUtils.isNotBlank(dept.getDeptName())) {
                hql += " and deptName like :deptName ";
                params.put("deptName", "%" + dept.getDeptName() + "%");
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

    public void insert(Dept instance) {
        this.sessionFactory.getCurrentSession().save(instance);
    }

    public void update(Dept instance) {
        this.sessionFactory.getCurrentSession().update(instance);
    }

    public void delete(Dept instance) {
        this.sessionFactory.getCurrentSession().delete(instance);
    }

}
