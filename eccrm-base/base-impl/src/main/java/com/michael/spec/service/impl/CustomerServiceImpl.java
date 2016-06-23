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
import com.ycrl.utils.number.IntegerUtils;
import com.ycrl.utils.string.StringUtils;
import eccrm.base.parameter.service.ParameterContainer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
                .setCallback(this)
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
    public void applyInvalid(String[] ids) {
        Assert.notEmpty(ids, "申请失败!客户ID不能为空!");
        customerDao.batchSetStatus(ids, Room.STATUS_APPLY_INVALID);
    }

    @Override
    public void batchPassInvalid(String[] ids) {
        Assert.notEmpty(ids, "操作失败!客户ID不能为空!");
        customerDao.batchSetStatus(ids, Room.STATUS_INVALID);
    }

    @Override
    public void batchPass(String[] customerIds) {
        Assert.notEmpty(customerIds, "操作失败!ID不能为空!");
        for (String id : customerIds) {
            Customer customer = customerDao.findById(id);
            if (customer == null) {
                continue;
            }
            String status = customer.getStatus();
            if (Room.STATUS_APPLY_ADD.equals(status) || Room.STATUS_APPLY_MODIFY.equals(status)) {
                customer.setStatus(Room.STATUS_ACTIVE);
            } else if (Room.STATUS_APPLY_INVALID.equals(status)) {
                customer.setStatus(Room.STATUS_INVALID);
            }
        }
    }

    @Override
    public void batchAdd(String[] ids) {
        Assert.notEmpty(ids, "操作失败!ID不能为空!");
        for (String id : ids) {
            Customer customer = customerDao.findById(id);
            // 只有“未录入”可以申请为新增
            if (customer != null && Room.STATUS_INACTIVE.equals(customer.getStatus())) {
                customer.setStatus(Room.STATUS_APPLY_ADD);
            }
        }
    }

    @Override
    public void batchModify(String[] ids) {
        Assert.notEmpty(ids, "操作失败!ID不能为空!");
        for (String id : ids) {
            Customer customer = customerDao.findById(id);
            if (customer == null) {
                continue;
            }
            customer.setStatus(Room.STATUS_APPLY_MODIFY);
        }
    }

    @Override
    public void batchDeny(String[] customerIds) {
        Assert.notEmpty(customerIds, "操作失败!ID不能为空!");
        for (String id : customerIds) {
            Customer customer = customerDao.findById(id);
            if (customer == null) {
                continue;
            }
            String status = customer.getStatus();
            if (Room.STATUS_APPLY_ADD.equals(status)) {
                customer.setStatus(Room.STATUS_INACTIVE);
            } else if (Room.STATUS_APPLY_MODIFY.equals(status) || Room.STATUS_APPLY_INVALID.equals(status)) {
                customer.setStatus(Room.STATUS_ACTIVE);
            }
        }
    }

    @Override
    public Integer addRoom(String customerId, String roomId) {
        Assert.hasText(customerId, "房产添加失败!客户ID不能为空!");
        Assert.hasText(roomId, "房产添加失败!房屋ID不能为空!");
        Customer customer = customerDao.findById(customerId);
        Assert.notNull(customer, "房产添加失败!客户不存在!");
        customer.setRoomCounts(IntegerUtils.add(customer.getRoomCounts(), 1));
        return customer.getRoomCounts();
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
