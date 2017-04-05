package org.bes.service;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.Dept;
import org.bes.model.repository.DeptDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptService {

    @Autowired
    private DeptDAO deptDAO;

    public List<Dept> getList(Dept condition) {
        return this.deptDAO.find(condition, null, 0);
    }

    public List<Dept> getList(Dept condition, int page, int pageSize) {
        return this.deptDAO.find(condition, (page - 1) * pageSize, pageSize);
    }

    public int getCount(Dept condition) {
        return this.deptDAO.findCount(condition);
    }

    public Dept get(String id) {
        return this.deptDAO.findById(id);
    }

    public Dept getByDeptId(String deptId) {
        return this.deptDAO.findByDeptId(deptId);
    }

    public void save(Dept dept) {
        if (StringUtils.isBlank(dept.getId())) {
            dept.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.deptDAO.insert(dept);
        } else {
            this.deptDAO.update(dept);
        }
    }

    public void delete(String id) {
        Dept dept = this.deptDAO.findById(id);
        if (dept != null) {
            this.deptDAO.delete(dept);
        }
    }

}
