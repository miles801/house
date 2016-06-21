package com.michael.spec.dao.impl;

import com.michael.spec.bo.RoomNewsBo;
import com.michael.spec.dao.RoomNewsDao;
import com.michael.spec.domain.RoomNews;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("roomNewsDao")
public class RoomNewsDaoImpl extends HibernateDaoHelper implements RoomNewsDao {

    @Override
    public String save(RoomNews roomNews) {
        return (String) getSession().save(roomNews);
    }

    @Override
    public void update(RoomNews roomNews) {
        getSession().update(roomNews);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RoomNews> query(RoomNewsBo bo) {
        Criteria criteria = createCriteria(RoomNews.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(RoomNewsBo bo) {
        Criteria criteria = createRowCountsCriteria(RoomNews.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + RoomNews.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(RoomNews roomNews) {
        Assert.notNull(roomNews, "要删除的对象不能为空!");
        getSession().delete(roomNews);
    }

    @Override
    public RoomNews findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (RoomNews) getSession().get(RoomNews.class, id);
    }

    private void initCriteria(Criteria criteria, RoomNewsBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}