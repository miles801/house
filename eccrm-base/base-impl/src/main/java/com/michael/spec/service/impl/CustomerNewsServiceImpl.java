package com.michael.spec.service.impl;

import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.spec.bo.CustomerNewsBo;
import com.michael.spec.dao.CustomerNewsDao;
import com.michael.spec.domain.CustomerNews;
import com.michael.spec.service.CustomerNewsService;
import com.michael.spec.vo.CustomerNewsVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("customerNewsService")
public class CustomerNewsServiceImpl implements CustomerNewsService, BeanWrapCallback<CustomerNews, CustomerNewsVo> {
    @Resource
    private CustomerNewsDao customerNewsDao;

    @Resource
    private EmpDao empDao;

    @Override
    public String save(CustomerNews customerNews) {
        // 绑定员工信息
        String empId = SecurityContext.getEmpId();
        customerNews.setEmpId(empId);
        Emp emp = empDao.findById(empId);
        Assert.notNull(emp, "操作失败!员工不存在!请刷新后重试!");
        customerNews.setEmpName(emp.getName());
        customerNews.setPhone(emp.getPhone());

        ValidatorUtils.validate(customerNews);
        String id = customerNewsDao.save(customerNews);
        return id;
    }

    @Override
    public void update(CustomerNews customerNews) {
        ValidatorUtils.validate(customerNews);
        customerNewsDao.update(customerNews);
    }

    @Override
    public PageVo pageQuery(CustomerNewsBo bo) {
        PageVo vo = new PageVo();
        Long total = customerNewsDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<CustomerNews> customerNewsList = customerNewsDao.query(bo);
        List<CustomerNewsVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(customerNewsList, CustomerNewsVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public CustomerNewsVo findById(String id) {
        CustomerNews customerNews = customerNewsDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(customerNews, CustomerNewsVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            customerNewsDao.deleteById(id);
        }
    }

    @Override
    public void doCallback(CustomerNews customerNews, CustomerNewsVo vo) {
    }
}
