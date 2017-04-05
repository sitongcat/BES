package org.bes.model.repository;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public User findById(String id) {
        return this.sessionFactory.getCurrentSession().get(User.class, id);
    }

    public User findByUserName(String userName) {
        String hql = "select u.*, d.deptName from User u left join Dept d on u.deptId = d.id where userName = ?";
        Session session = this.sessionFactory.getCurrentSession();
        NativeQuery query = session.createNativeQuery(hql, User.class);
        query.setParameter(1, userName);
	    List<User> users = query.getResultList();
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public List<User> find(User user, Integer firstResult, Integer maxResults) {
        Map<String, Object> params = new LinkedHashMap<>();
        String hql = "select u.*, d.deptName from User u left join Dept d on u.deptId = d.id where 1=1 ";
        if (user != null) {
            if (StringUtils.isNotBlank(user.getDeptId())) {
                hql += " and u.deptId = :deptId ";
                params.put("deptId", user.getDeptId());
            }
            if (StringUtils.isNotBlank(user.getName())) {
                hql += " and name like :name ";
                params.put("name", "%" + user.getName() + "%");
            }
            if (StringUtils.isNotBlank(user.getUserName())) {
                hql += " and userName like :userName ";
                params.put("userName", "%" + user.getUserName() + "%");
            }
        }
        hql += " order by u.deptId, name";
        Session session = this.sessionFactory.getCurrentSession();
        NativeQuery query = session.createNativeQuery(hql);
        query.setResultTransformer(Transformers.aliasToBean(User.class));
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

    public int findCount(User user) {
        Map<String, Object> params = new LinkedHashMap<>();
        String hql = "select count(*) from User where 1=1 ";
        if (user != null) {
            if (StringUtils.isNotBlank(user.getDeptId())) {
                hql += " and deptId = :deptId ";
                params.put("deptId", user.getDeptId());
            }
            if (StringUtils.isNotBlank(user.getName())) {
                hql += " and name like :name ";
                params.put("name", "%" + user.getName() + "%");
            }
            if (StringUtils.isNotBlank(user.getUserName())) {
                hql += " and userName like :userName ";
                params.put("userName", "%" + user.getUserName() + "%");
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

    public void insert(User instance) {
        this.sessionFactory.getCurrentSession().save(instance);
    }

    public void update(User instance) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = session.get(User.class, instance.getId());
        if (user == null) {
            throw new NullPointerException("用户不存在!");
        }
        user.setName(instance.getName());
        user.setDeptId(instance.getDeptId());
        user.setType(instance.getType());
        user.setPassword(instance.getPassword());
        session.update(user);
    }

    public void delete(User instance) {
        this.sessionFactory.getCurrentSession().delete(instance);
    }

}
