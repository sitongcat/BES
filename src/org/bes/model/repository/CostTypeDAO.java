package org.bes.model.repository;

import org.bes.model.entity.CostType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class CostTypeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public CostType findById(String id) {
        return this.sessionFactory.getCurrentSession().get(CostType.class, id);
    }

    public CostType findByName(String name) {
        String hql = "from CostType where name = ?";
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql, CostType.class);
        query.setParameter(0, name);
        List<CostType> dataList = query.getResultList();
        if (dataList != null && !dataList.isEmpty()) {
            return dataList.get(0);
        }
        return null;
    }

    public List<CostType> findAll() {
        String hql = "from CostType where 1=1 order by sort, name";
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(hql, CostType.class);
        return query.getResultList();
    }

    public void insert(CostType instance) {
        this.sessionFactory.getCurrentSession().save(instance);
    }

    public void update(CostType instance) {
        this.sessionFactory.getCurrentSession().update(instance);
    }

    public void delete(CostType instance) {
        this.sessionFactory.getCurrentSession().delete(instance);
    }

}
