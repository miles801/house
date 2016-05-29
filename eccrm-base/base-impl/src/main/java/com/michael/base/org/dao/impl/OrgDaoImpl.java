package com.michael.base.org.dao.impl;

import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.dao.OrgDao;
import com.michael.base.org.domain.Org;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import com.ycrl.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;


/**
 * @author Michael
 */
@Repository("orgDao")
public class OrgDaoImpl extends HibernateDaoHelper implements OrgDao {

    @Override
    public String save(Org org) {
        return (String) getSession().save(org);
    }

    @Override
    public void update(Org org) {
        getSession().update(org);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Org> query(OrgBo bo) {
        Criteria criteria = createCriteria(Org.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("sequenceNo"));
        return criteria.list();
    }

    @Override
    public Long getTotal(OrgBo bo) {
        Criteria criteria = createRowCountsCriteria(Org.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Org.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public Boolean hasCode(String code, String id) {
        Assert.hasText(code, "编号不能为空!");
        Criteria criteria = createRowCountsCriteria(Org.class)
                .add(Restrictions.eq("code", code));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        Long total = (Long) criteria.uniqueResult();
        return !(total == null || total == 0);
    }

    @Override
    public void delete(Org org) {
        Assert.notNull(org, "要删除的对象不能为空!");
        getSession().delete(org);
    }

    @Override
    public Org findParent(String orgId) {
        Assert.hasText(orgId, "查询上级机构失败!机构ID不能为空!");
        Criteria criteria = createCriteria(Org.class);
        DetachedCriteria parent = DetachedCriteria.forClass(Org.class)
                .setProjection(Projections.property("parentId"))
                .add(Restrictions.idEq(orgId));

        return (Org) criteria.add(Property.forName("id").eq(parent))
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Org> allChildren(String orgId) {
        Assert.hasText(orgId, "查询子机构失败!机构ID不能为空!");
        // 查询的是path匹配 /pid/cid1/cid2 这样的数据
        return createCriteria(Org.class)
                .add(Restrictions.like("path", "/" + orgId + "/"))
                .add(Restrictions.ne("id", orgId))
                .list();
    }

    @Override
    public Org findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Org) getSession().get(Org.class, id);
    }

    @Override
    public void resetIsParent(String orgId) {

    }


    @Override
    public void disableAll(String orgId) {
        Assert.hasText(orgId, "禁用失败!组织机构ID不能为空!");
        getSession().createQuery("update " + Org.class.getName() + " o set o.isHide=true where o.path like ?")
                .setParameter(0, "%/" + orgId + "/%")
                .executeUpdate();
    }

    @Override
    public void enableAll(Set<String> orgIds) {
        Assert.notEmpty(orgIds, "启用失败!组织机构ID不能为空!");
        getSession().createQuery("update " + Org.class.getName() + " o set o.isHide=false where o.id in (:ids)")
                .setParameterList("ids", orgIds)
                .executeUpdate();
    }

    /**
     * 通过全名找到对应的组织对象
     *
     * @param longName
     * @return
     * @author:dufan
     */

    @Override
    public Org findOrgByOrgFullName(String longName) {
        Assert.hasText(longName, "机构的名称不能为空!");
        Criteria criteria = createCriteria(Org.class);
        criteria.add(Restrictions.eq("name", longName));
        return (Org) criteria.uniqueResult();
    }


    @Override
    public String findName(String id) {
        Assert.hasText(id, "查询机构名称失败!机构ID不能为空!");
        return (String) createCriteria(Org.class)
                .setProjection(Projections.property("name"))
                .add(Restrictions.idEq(id))
                .uniqueResult();
    }

    private void initCriteria(Criteria criteria, OrgBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}