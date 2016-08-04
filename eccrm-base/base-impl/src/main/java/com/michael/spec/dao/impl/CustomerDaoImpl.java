package com.michael.spec.dao.impl;

import com.michael.base.emp.domain.Emp;
import com.michael.base.position.dao.PositionDao;
import com.michael.base.position.dao.PositionEmpDao;
import com.michael.spec.bo.CustomerBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.CustomerDao;
import com.michael.spec.domain.Customer;
import com.ycrl.core.HibernateDaoHelper;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.criteria.CriteriaUtils;
import com.ycrl.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Michael
 */
@Repository("customerDao")
public class CustomerDaoImpl extends HibernateDaoHelper implements CustomerDao {

    @Resource
    private BuildingDao buildingDao;

    @Resource
    private PositionDao positionDao;
    @Resource
    private PositionEmpDao positionEmpDao;

    @Override
    public String save(Customer customer) {
        return (String) getSession().save(customer);
    }

    @Override
    public void update(Customer customer) {
        getSession().update(customer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> query(CustomerBo bo) {
        Criteria criteria = createCriteria(Customer.class);
        initCriteria(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(CustomerBo bo) {
        Criteria criteria = createRowCountsCriteria(Customer.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Customer.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Customer customer) {
        Assert.notNull(customer, "要删除的对象不能为空!");
        getSession().delete(customer);
    }

    @Override
    public Customer findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Customer) getSession().get(Customer.class, id);
    }

    @Override
    public String maxCode() {
        return (String) createCriteria(Customer.class)
                .setProjection(Projections.max("code"))
                .uniqueResult();
    }

    @Override
    public Customer findByPhone(String phone) {
        Assert.hasText(phone, "查询失败!电话号码不能为空!");
        return (Customer) createCriteria(Customer.class)
                .add(Restrictions.eq("phone1", phone))
                .uniqueResult();
    }

    @Override
    public void batchSetStatus(String[] customerIds, String status) {
        Assert.hasText("操作失败!状态不能为空!");
        Assert.notEmpty(customerIds, "操作失败!客户ID不能为空!");
        getSession().createQuery("update " + Customer.class.getName() + " c set c.status=? where c.id in(:ids)")
                .setParameter(0, status)
                .setParameterList("ids", customerIds)
                .executeUpdate();
    }

    @Override
    public boolean hasPhone(String phone1, String id) {
        Assert.hasText(phone1, "查询失败!电话号码不能为空!");
        Criteria criteria = createRowCountsCriteria(Customer.class)
                .add(Restrictions.eq("phone1", phone1));
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        Long total = (Long) criteria.uniqueResult();
        return total != null && total > 0;
    }

    @Override
    public boolean hasSame(String buildingId, String name, String phone1, String id) {
        Assert.hasText(name, "查询失败!客户姓名不能为空!");
        Assert.hasText(phone1, "查询失败!电话号码不能为空!");
        Criteria criteria = createRowCountsCriteria(Customer.class)
                .add(Restrictions.eq("name", name))
                .add(Restrictions.eq("phone1", phone1));
        if (StringUtils.isNotEmpty(buildingId)) {
            criteria.add(Restrictions.eq("buildingId", buildingId));
        }
        if (StringUtils.isNotEmpty(id)) {
            criteria.add(Restrictions.ne("id", id));
        }
        Long total = (Long) criteria.uniqueResult();
        return total != null && total > 0;
    }

    private void initCriteria(Criteria criteria, CustomerBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        if (bo == null) {
            return;
        }
        CriteriaUtils.addCondition(criteria, bo);
        String phone = bo.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            criteria.add(Restrictions.disjunction()
                    .add(Restrictions.like("phone1", phone, MatchMode.ANYWHERE))
                    .add(Restrictions.like("phone2", phone, MatchMode.ANYWHERE))
                    .add(Restrictions.like("phone3", phone, MatchMode.ANYWHERE))
            );
        }
        boolean isNotManager = !(bo != null && bo.getManager() != null && bo.getManager());
        if (isNotManager && StringUtils.isEmpty(bo.getBuildingId())) {
            // 如果是负责人，则获取负责的楼盘的房屋;否则获取负责的和维护的所有数据
            boolean isMaster = bo.getMaster() != null && bo.getMaster();
            if (isMaster) {
                criteria.add(Property.forName("buildingId").in(buildingDao.getMasterBuilding(SecurityContext.getEmpId())));
            } else {
                List<String> pIds = new ArrayList<>();
                // 获取自己的所创建的人以及下下级创建的人
                pIds.add(SecurityContext.getEmpId());
                belongEmp(pIds, SecurityContext.getEmpId());
                criteria.add(
                        Restrictions.or(
                                Property.forName("creatorId").in(pIds),    // 自己创建的
                                Property.forName("buildingId").in(buildingDao.getPersonalBuilding(SecurityContext.getEmpId()))// 自己楼盘的
                        )
                );
            }
        }
    }

    private void belongEmp(List<String> empIds, String empId) {
        List<String> ids = getSession().createQuery("select e.id from " + Emp.class.getName() + " e where e.creatorId=?")
                .setParameter(0, empId)
                .list();
        if (ids != null && !ids.isEmpty()) {
            empIds.addAll(ids);
            for (String id : ids) {
                belongEmp(empIds, id);
            }
        }
    }

}