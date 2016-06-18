package com.michael.spec.dao.impl;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.Room;
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
@Repository("roomDao")
public class RoomDaoImpl extends HibernateDaoHelper implements RoomDao {

    @Override
    public String save(Room room) {
        return (String) getSession().save(room);
    }

    @Override
    public void update(Room room) {
        getSession().update(room);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Room> query(RoomBo bo) {
        Criteria criteria = createCriteria(Room.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(RoomBo bo) {
        Criteria criteria = createRowCountsCriteria(Room.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Room.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Room room) {
        Assert.notNull(room, "要删除的对象不能为空!");
        getSession().delete(room);
    }

    @Override
    public long getUnitRoomCounts(String unitId) {
        Assert.hasText(unitId, "查询失败!单元ID不能为空!");
        Long total = (Long) createRowCountsCriteria(Room.class)
                .add(Restrictions.eq("unitId", unitId))
                .uniqueResult();
        return total == null ? 0 : total;
    }

    @Override
    public Room findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Room) getSession().get(Room.class, id);
    }

    private void initCriteria(Criteria criteria, RoomBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}