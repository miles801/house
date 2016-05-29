package com.michael.base.position.dao.impl;

import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.dao.PositionDao;
import com.michael.base.position.domain.Position;
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
@Repository("positionDao")
public class PositionDaoImpl extends HibernateDaoHelper implements PositionDao {

    @Override
    public String save(Position position) {
        return (String) getSession().save(position);
    }

    @Override
    public void update(Position position) {
        getSession().update(position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Position> query(PositionBo bo) {
        Criteria criteria = createCriteria(Position.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.desc("sequenceNo"));
        return criteria.list();
    }

    @Override
    public Long getTotal(PositionBo bo) {
        Criteria criteria = createRowCountsCriteria(Position.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Position.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Position position) {
        Assert.notNull(position, "要删除的对象不能为空!");
        getSession().delete(position);
    }

    @Override
    public Position findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Position) getSession().get(Position.class, id);
    }

    private void initCriteria(Criteria criteria, PositionBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}