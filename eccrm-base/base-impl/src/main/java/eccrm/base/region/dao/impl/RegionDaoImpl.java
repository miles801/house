package eccrm.base.region.dao.impl;

import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import com.ycrl.core.pager.OrderNode;
import com.ycrl.core.pager.Pager;
import eccrm.base.region.bo.RegionBo;
import eccrm.base.region.dao.RegionDao;
import eccrm.base.region.domain.Region;
import eccrm.utils.Argument;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author: miles
 * @datetime: 2014-03-25
 */
@Repository("regionDao")
public class RegionDaoImpl extends HibernateDaoHelper implements RegionDao {
    @Override
    public String save(Region region) {
        return (String) getSession().save(region);
    }

    @Override
    public void update(Region region) {
        getSession().update(region);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Region> query(RegionBo bo) {
        Criteria criteria = createCriteria(Region.class);
        initCriteria(criteria, bo);
        OrderNode order = Pager.getOrder();
        if (order == null) {
            //默认按照类型和序列号的升序进行排序
            criteria.addOrder(Order.asc("type"));
            criteria.addOrder(Order.asc("sequenceNo"));
        }
        return criteria.list();
    }

    @Override
    public Long getTotal(RegionBo bo) {
        Criteria criteria = createRowCountsCriteria(Region.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public int deleteById(String id) {
        Argument.isEmpty(id, "根据ID删除行政区域时,ID不能为空!");
        return getSession().createQuery("delete from " + Region.class.getSimpleName() + " r where r.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public Region findById(String id) {
        Assert.hasText(id, "查询失败!行政区域ID不能为空!");
        return (Region) getSession().get(Region.class, id);
    }

    @Override
    public synchronized int nextSequenceNo(String parentId) {
        Integer max = 0;
        if (parentId == null) {
            max = (Integer) getSession().createQuery("select max(r.sequenceNo) from " + Region.class.getName() + " r where r.parentId is null").uniqueResult();
        } else {
            max = (Integer) getSession().createQuery("select max(r.sequenceNo) from  " + Region.class.getName() + " r where r.parentId = ?")
                    .setParameter(0, parentId).uniqueResult();
        }
        return (max == null ? 1 : max + 1);
    }

    private void initCriteria(Criteria criteria, RegionBo bo) {
        CriteriaUtils.addCondition(criteria,bo);
    }

    @Override
    public String getName(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null!");
        }
        return (String) createCriteria(Region.class)
                .setProjection(Projections.property("name"))
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public String getNameByCode(String cityCode) {
        com.ycrl.core.exception.Argument.isEmpty(cityCode, "根据区号查询城市名称时,城市区号不能为空!");
        return (String) createCriteria(Region.class)
                .setProjection(Projections.property("name"))
                .add(Restrictions.eq("code", cityCode))
                .uniqueResult();
    }

    @Override
    public Region getBelongProvence(String city) {
        if (city == null || "".equals(city.trim())) {
            return null;
        }
        // 查询上级ID的子查询
        DetachedCriteria parentIdDetached = DetachedCriteria.forClass(Region.class)
                .setProjection(Projections.property("parentId"))
                .add(Restrictions.idEq(city));
        return (Region) createCriteria(Region.class)
                .add(Property.forName("id").eq(parentIdDetached))
                .setFirstResult(0)
                .setMaxResults(1)
                .uniqueResult();
    }
}