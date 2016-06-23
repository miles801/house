package com.michael.spec.dao.impl;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.RoomStarDao;
import com.michael.spec.domain.RoomStar;
import com.michael.spec.domain.RoomView;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("roomStarDao")
public class RoomStarDaoImpl extends HibernateDaoHelper implements RoomStarDao {

    @Override
    public String save(RoomStar roomStar) {
        return (String) getSession().save(roomStar);
    }

    @Override
    public void update(RoomStar roomStar) {
        getSession().update(roomStar);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RoomView> query(RoomBo bo) {
        Criteria criteria = createCriteria(RoomView.class);
        initCriteria(criteria, bo);
        criteria.add(Property.forName("id").in(myStarRoomCriteria()));
        return criteria.list();
    }

    private DetachedCriteria myStarRoomCriteria() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RoomStar.class);
        detachedCriteria.setProjection(Projections.property("roomId"));
        detachedCriteria.add(Restrictions.eq("empId", SecurityContext.getEmpId()));
        return detachedCriteria;
    }

    @Override
    public Long getTotal(RoomBo bo) {
        Criteria criteria = createRowCountsCriteria(RoomView.class);
        initCriteria(criteria, bo);
        criteria.add(Property.forName("id").in(myStarRoomCriteria()));
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + RoomStar.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public RoomStar find(String roomId, String empId) {
        Assert.hasText(roomId, "查询失败!房屋ID不能为空!");
        Assert.hasText(empId, "查询失败!关注人不能为空!");
        return (RoomStar) createCriteria(RoomStar.class)
                .add(Restrictions.eq("roomId", roomId))
                .add(Restrictions.eq("empId", empId))
                .uniqueResult();
    }

    @Override
    public void delete(RoomStar roomStar) {
        Assert.notNull(roomStar, "要删除的对象不能为空!");
        getSession().delete(roomStar);
    }

    @Override
    public RoomStar findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (RoomStar) getSession().get(RoomStar.class, id);
    }

    private void initCriteria(Criteria criteria, RoomBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}