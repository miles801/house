package com.michael.spec.dao.impl;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.RoomView;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import com.ycrl.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author Michael
 */
@Repository("roomDao")
public class RoomDaoImpl extends HibernateDaoHelper implements RoomDao {

    @Resource
    private BuildingDao buildingDao;

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
    public List<RoomView> query(RoomBo bo) {
        Criteria criteria = createCriteria(RoomView.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(RoomBo bo) {
        Criteria criteria = createRowCountsCriteria(RoomView.class);
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
    public String maxKey(String buildingId) {
        Assert.hasText(buildingId, "查询失败!楼盘ID不能为空!");
        return (String) createCriteria(RoomView.class)
                .setProjection(Projections.max("roomKey"))
                .add(Restrictions.eq("buildingId", buildingId))
                .uniqueResult();
    }

    @Override
    public void batchSetStatus(String[] ids, String status) {
        Assert.hasText("操作失败!状态不能为空!");
        Assert.notEmpty(ids, "操作失败!房屋ID不能为空!");
        getSession().createQuery("update " + Room.class.getName() + " r set r.status=? where r.id in(:ids)")
                .setParameter(0, status)
                .setParameterList("ids", ids)
                .executeUpdate();
    }

    @Override
    public Room findRoomById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Room) getSession().get(Room.class, id);
    }

    @Override
    public RoomView findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (RoomView) getSession().get(RoomView.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> findCodeByCustomer(String customerId) {
        Assert.hasText(customerId, "查询失败!客户ID不能为空!");
        return createCriteria(Room.class)
                .setProjection(Projections.property("roomKey"))
                .add(Restrictions.eq("customerId", customerId))
                .list();
    }

    @Override
    public Room findByCode(String code) {
        Assert.hasText(code, "查询失败!房屋编号不能为空!");
        return (Room) createCriteria(Room.class)
                .add(Restrictions.eq("roomKey", code))
                .setFirstResult(0)
                .setMaxResults(1)
                .uniqueResult();
    }

    private void initCriteria(Criteria criteria, RoomBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        boolean isNotManager = !(bo != null && bo.getManager() != null && bo.getManager());
        CriteriaUtils.addCondition(criteria, bo);
        if (isNotManager && StringUtils.isEmpty(bo.getBuildingId())) {
            criteria.add(Property.forName("buildingId").in(buildingDao.getPersonalBuilding(SecurityContext.getEmpId())));
        }
    }

}