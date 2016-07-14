package com.michael.spec.dao.impl;

import com.michael.spec.bo.UnitBo;
import com.michael.spec.dao.UnitDao;
import com.michael.spec.domain.Block;
import com.michael.spec.domain.Unit;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("unitDao")
public class UnitDaoImpl extends HibernateDaoHelper implements UnitDao {

    @Override
    public String save(Unit unit) {
        return (String) getSession().save(unit);
    }

    @Override
    public void update(Unit unit) {
        getSession().update(unit);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Unit> query(UnitBo bo) {
        Criteria criteria = createCriteria(Unit.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("code"));
        criteria.addOrder(Order.asc("doorCode"));
        return criteria.list();
    }

    @Override
    public Long getTotal(UnitBo bo) {
        Criteria criteria = createRowCountsCriteria(Unit.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Unit.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public Long getUnitCount(String blockId) {
        Assert.hasText(blockId, "查询失败!楼栋ID不能为空!");
        Long total = (Long) getSession().createQuery("select count(b.code) from " + Block.class.getName() + " b where b.blockId=? group by b.code ")
                .setParameter(0, blockId)
                .uniqueResult();
        return total;
    }

    @Override
    public void delete(Unit unit) {
        Assert.notNull(unit, "要删除的对象不能为空!");
        getSession().delete(unit);
    }

    @Override
    public Unit findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Unit) getSession().get(Unit.class, id);
    }

    private void initCriteria(Criteria criteria, UnitBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}