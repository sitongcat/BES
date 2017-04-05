package org.bes.service;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.CostCategory;
import org.bes.model.repository.CostCategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CostCategoryService {

    @Autowired
    private CostCategoryDAO costCategoryDAO;

    public List<CostCategory> getList(CostCategory condition, int page, int pageSize) {
        return this.costCategoryDAO.find(condition, (page - 1) * pageSize, pageSize);
    }

    public int getCount(CostCategory condition) {
        return this.costCategoryDAO.findCount(condition);
    }

    public List<CostCategory> getByParentId(String parentId) {
        CostCategory condition = new CostCategory();
        condition.setParentId(parentId);
        return this.costCategoryDAO.find(condition, -1, 0);
    }

    public CostCategory get(String id) {
        return this.costCategoryDAO.findById(id);
    }

    public CostCategory getByCode(String code) {
        return this.costCategoryDAO.findByCode(code);
    }

    public void save(CostCategory CostCategory) {
        if (StringUtils.isBlank(CostCategory.getId())) {
            CostCategory.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.costCategoryDAO.insert(CostCategory);
        } else {
            this.costCategoryDAO.update(CostCategory);
        }
    }

    public void delete(String id) {
        CostCategory CostCategory = this.costCategoryDAO.findById(id);
        if (CostCategory != null) {
            this.costCategoryDAO.delete(CostCategory);
        }
    }

}
