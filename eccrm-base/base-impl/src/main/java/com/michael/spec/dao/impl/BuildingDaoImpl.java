package com.michael.spec.dao.impl;

import com.michael.spec.bo.BuildingBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.domain.Building;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import com.ycrl.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("buildingDao")
public class BuildingDaoImpl extends HibernateDaoHelper implements BuildingDao {

    @Override
    public String save(Building building) {
        return (String) getSession().save(building);
    }

    @Override
    public void update(Building building) {
        getSession().update(building);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Building> query(BuildingBo bo) {
        Criteria criteria = createCriteria(Building.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(BuildingBo bo) {
        Criteria criteria = createRowCountsCriteria(Building.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Building.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Building building) {
        Assert.notNull(building, "要删除的对象不能为空!");
        getSession().delete(building);
    }

    @Override
    public boolean hasName(String name, String id) {
        Assert.hasText(name, "查询失败!楼盘名称不能为空!");
        Criteria criteria = createRowCountsCriteria(Building.class)
                .add(Restrictions.eq("name", name));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        Long total = (Long) criteria.uniqueResult();
        return total != null && total > 0;
    }

    @Override
    public Building findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Building) getSession().get(Building.class, id);
    }

    private void initCriteria(Criteria criteria, BuildingBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}