package com.michael.spec.dao.impl;

import com.michael.spec.bo.RoomBusinessBo;
import com.michael.spec.dao.RoomBusinessDao;
import com.michael.spec.domain.RoomBusiness;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("roomBusinessDao")
public class RoomBusinessDaoImpl extends HibernateDaoHelper implements RoomBusinessDao {

    @Override
    public String save(RoomBusiness roomBusiness) {
        return (String) getSession().save(roomBusiness);
    }

    @Override
    public void update(RoomBusiness roomBusiness) {
        getSession().update(roomBusiness);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RoomBusiness> query(RoomBusinessBo bo) {
        Criteria criteria = createCriteria(RoomBusiness.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(RoomBusinessBo bo) {
        Criteria criteria = createRowCountsCriteria(RoomBusiness.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + RoomBusiness.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(RoomBusiness roomBusiness) {
        Assert.notNull(roomBusiness, "要删除的对象不能为空!");
        getSession().delete(roomBusiness);
    }

    @Override
    public RoomBusiness findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (RoomBusiness) getSession().get(RoomBusiness.class, id);
    }

    private void initCriteria(Criteria criteria, RoomBusinessBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}