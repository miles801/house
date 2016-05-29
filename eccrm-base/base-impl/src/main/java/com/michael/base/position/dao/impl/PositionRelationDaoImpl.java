package com.michael.base.position.dao.impl;

import com.michael.base.position.bo.PositionRelationBo;
import com.michael.base.position.dao.PositionRelationDao;
import com.michael.base.position.domain.PositionRelation;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("positionRelationDao")
public class PositionRelationDaoImpl extends HibernateDaoHelper implements PositionRelationDao {

    @Override
    public String save(PositionRelation positionRelation) {
        return (String) getSession().save(positionRelation);
    }

    @Override
    public void update(PositionRelation positionRelation) {
        getSession().update(positionRelation);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PositionRelation> query(PositionRelationBo bo) {
        Criteria criteria = createCriteria(PositionRelation.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(PositionRelationBo bo) {
        Criteria criteria = createRowCountsCriteria(PositionRelation.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + PositionRelation.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(PositionRelation positionRelation) {
        Assert.notNull(positionRelation, "要删除的对象不能为空!");
        getSession().delete(positionRelation);
    }

    @Override
    public PositionRelation findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (PositionRelation) getSession().get(PositionRelation.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> findParents(String childId) {
        Assert.hasText(childId, "查询失败!ID不能为空!");
        return createCriteria(PositionRelation.class)
                .setProjection(Projections.property("positionId"))
                .add(Restrictions.eq("childId", childId))
                .list();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<String> findChildren(String positionId) {
        Assert.hasText(positionId, "查询失败!ID不能为空!");
        return createCriteria(PositionRelation.class)
                .setProjection(Projections.property("childId"))
                .add(Restrictions.eq("positionId", positionId))
                .list();
    }

    @Override
    public void deleteByChild(String childId) {
        Assert.hasText(childId, "删除失败!ID不能为空!");
        getSession().createQuery("delete from " + PositionRelation.class.getName() + " pr where pr.childId=?")
                .setParameter(0, childId)
                .executeUpdate();
    }

    private void initCriteria(Criteria criteria, PositionRelationBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}