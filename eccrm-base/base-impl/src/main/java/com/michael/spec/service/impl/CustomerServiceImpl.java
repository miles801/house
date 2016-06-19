package com.michael.spec.service.impl;

import com.michael.base.common.BaseParameter;
import com.michael.spec.bo.CustomerBo;
import com.michael.spec.dao.CustomerDao;
import com.michael.spec.domain.Customer;
import com.michael.spec.domain.Room;
import com.michael.spec.service.CustomerService;
import com.michael.spec.service.HouseParams;
import com.michael.spec.vo.CustomerVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.string.StringUtils;
import eccrm.base.parameter.service.ParameterContainer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Michael
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService, BeanWrapCallback<Customer, CustomerVo> {
    @Resource
    private CustomerDao customerDao;

    @Override
    public String save(Customer customer) {
        // 设置编号
        String code = customerDao.maxCode();
        int newNo = 1;
        if (StringUtils.isNotEmpty(code)) {
            newNo = Integer.parseInt(code.substring(1)) + 1;
        }
        DecimalFormat decimalFormat = new DecimalFormat("C000000");
        customer.setCode(decimalFormat.format(newNo));

        // 设置状态
        customer.setStatus(Room.STATUS_INACTIVE);
        ValidatorUtils.validate(customer);
        String id = customerDao.save(customer);
        return id;
    }

    @Override
    public void update(Customer customer) {
        ValidatorUtils.validate(customer);
        customerDao.update(customer);
    }

    @Override
    public PageVo pageQuery(CustomerBo bo) {
        PageVo vo = new PageVo();
        Long total = customerDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Customer> customerList = customerDao.query(bo);
        List<CustomerVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(customerList, CustomerVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public CustomerVo findById(String id) {
        Customer customer = customerDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(customer, CustomerVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            customerDao.deleteById(id);
        }
    }

    @Override
    public void doCallback(Customer customer, CustomerVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        vo.setAgeName(container.getBusinessName(Customer.AGE_STAGE, customer.getAge()));
        vo.setMoneyName(container.getBusinessName(Customer.MONEY_STAGE, customer.getMoney()));
        vo.setSexName(container.getBusinessName(BaseParameter.SEX, customer.getSex()));
        vo.setEducationName(container.getBusinessName(BaseParameter.EDU, customer.getEducation()));
        vo.setMarriageName(container.getBusinessName(BaseParameter.MARRIAGE, customer.getMarriage()));
        vo.setStatusName(container.getSystemName(HouseParams.HOUSE_STATUS, customer.getStatus()));
    }
}
