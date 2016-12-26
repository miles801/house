package com.michael.spec.dao.impl;

import com.michael.spec.bo.RoomRentBo;
import com.michael.spec.dao.RoomRentDao;
import com.michael.spec.domain.RoomRent;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("roomRentDao")
public class RoomRentDaoImpl extends HibernateDaoHelper implements RoomRentDao {

    @Override
    public String save(RoomRent roomRent) {
        return (String) getSession().save(roomRent);
    }

    @Override
    public void update(RoomRent roomRent) {
        getSession().update(roomRent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RoomRent> query(RoomRentBo bo) {
        Criteria criteria = createCriteria(RoomRent.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(RoomRentBo bo) {
        Criteria criteria = createRowCountsCriteria(RoomRent.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + RoomRent.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(RoomRent roomRent) {
        Assert.notNull(roomRent, "要删除的对象不能为空!");
        getSession().delete(roomRent);
    }

    @Override
    public RoomRent findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (RoomRent) getSession().get(RoomRent.class, id);
    }

    @Override
    public RoomRent findCurrent(String roomId) {
        Assert.hasText(roomId, "查询失败!房屋ID不能为空!");
        return (RoomRent) createCriteria(RoomRent.class)
                .add(Restrictions.eq("roomId", roomId))
                .add(Restrictions.eq("finish", false))
                .uniqueResult();
    }

    @Override
    public void deleteByRoom(String roomId) {
        Assert.hasText("操作失败!房屋ID不能为空!");
        getSession().createQuery("delete from " + RoomRent.class.getName() + " rr where rr.roomId=?")
                .setParameter(0, roomId)
                .executeUpdate();

    }

    @Override
    public boolean isRent(String customerId) {
        Assert.hasText(customerId, "查询失败!客户ID不能为空!");
        Long total = (Long) createRowCountsCriteria(RoomRent.class)
                .add(Restrictions.eq("newCustomerId", customerId))
                .uniqueResult();
        return total != null && total > 0;
    }

    private void initCriteria(Criteria criteria, RoomRentBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}