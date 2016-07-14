package com.michael.spec.dao.impl;

import com.michael.spec.bo.CustomerNewsBo;
import com.michael.spec.dao.CustomerNewsDao;
import com.michael.spec.domain.CustomerNews;
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
@Repository("customerNewsDao")
public class CustomerNewsDaoImpl extends HibernateDaoHelper implements CustomerNewsDao {

    @Override
    public String save(CustomerNews customerNews) {
        return (String) getSession().save(customerNews);
    }

    @Override
    public void update(CustomerNews customerNews) {
        getSession().update(customerNews);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CustomerNews> query(CustomerNewsBo bo) {
        Criteria criteria = createCriteria(CustomerNews.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.desc("createdDatetime"));
        return criteria.list();
    }

    @Override
    public Long getTotal(CustomerNewsBo bo) {
        Criteria criteria = createRowCountsCriteria(CustomerNews.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + CustomerNews.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(CustomerNews customerNews) {
        Assert.notNull(customerNews, "要删除的对象不能为空!");
        getSession().delete(customerNews);
    }

    @Override
    public CustomerNews findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (CustomerNews) getSession().get(CustomerNews.class, id);
    }

    private void initCriteria(Criteria criteria, CustomerNewsBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}