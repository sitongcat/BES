package org.bes.service;

import org.apache.commons.lang3.StringUtils;
import org.bes.model.entity.CostCategory;
import org.bes.model.entity.CostType;
import org.bes.model.repository.CostCategoryDAO;
import org.bes.model.repository.CostTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CostTypeService {

    @Autowired
    private CostTypeDAO costTypeDAO;

    public List<CostType> getList() {
        return this.costTypeDAO.findAll();
    }

    public CostType get(String id) {
        return this.costTypeDAO.findById(id);
    }

    public CostType getByName(String name) {
        return this.costTypeDAO.findByName(name);
    }

    public void save(CostType costType) {
        if (StringUtils.isBlank(costType.getId())) {
            costType.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            this.costTypeDAO.insert(costType);
        } else {
            this.costTypeDAO.update(costType);
        }
    }

    public void delete(String id) {
        CostType instance = this.costTypeDAO.findById(id);
        if (instance != null) {
            this.costTypeDAO.delete(instance);
        }
    }

}
