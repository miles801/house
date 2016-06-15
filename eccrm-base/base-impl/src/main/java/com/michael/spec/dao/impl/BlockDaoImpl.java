package com.michael.spec.dao.impl;

import com.michael.spec.bo.BlockBo;
import com.michael.spec.dao.BlockDao;
import com.michael.spec.domain.Block;
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
@Repository("blockDao")
public class BlockDaoImpl extends HibernateDaoHelper implements BlockDao {

    @Override
    public String save(Block block) {
        return (String) getSession().save(block);
    }

    @Override
    public void update(Block block) {
        getSession().update(block);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Block> query(BlockBo bo) {
        Criteria criteria = createCriteria(Block.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("code"));
        return criteria.list();
    }

    @Override
    public Long getTotal(BlockBo bo) {
        Criteria criteria = createRowCountsCriteria(Block.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Block.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Block block) {
        Assert.notNull(block, "要删除的对象不能为空!");
        getSession().delete(block);
    }

    @Override
    public Block findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Block) getSession().get(Block.class, id);
    }

    private void initCriteria(Criteria criteria, BlockBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}