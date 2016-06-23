package com.michael.spec.service.impl;

import com.michael.spec.bo.RoomBusinessBo;
import com.michael.spec.dao.CustomerDao;
import com.michael.spec.dao.RoomBusinessDao;
import com.michael.spec.domain.Customer;
import com.michael.spec.domain.RoomBusiness;
import com.michael.spec.service.RoomBusinessService;
import com.michael.spec.vo.RoomBusinessVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import eccrm.base.parameter.service.ParameterContainer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("roomBusinessService")
public class RoomBusinessServiceImpl implements RoomBusinessService, BeanWrapCallback<RoomBusiness, RoomBusinessVo> {
    @Resource
    private RoomBusinessDao roomBusinessDao;

    @Resource
    private CustomerDao customerDao;

    @Override
    public String save(RoomBusiness roomBusiness) {
        ValidatorUtils.validate(roomBusiness);
        String originCustomerId = roomBusiness.getOriginCustomerId();
        Customer originCustomer = customerDao.findById(originCustomerId);
        Assert.notNull(originCustomer, "房屋交易失败!卖方客户不存在!请刷新后重试!");
        String newCustomerId = roomBusiness.getNewCustomerId();
        Customer newCustomer = customerDao.findById(newCustomerId);
        Assert.notNull(originCustomer, "房屋交易失败!买方客户不存在!请刷新后重试!");
        roomBusiness.setOriginCustomerName(originCustomer.getName());
        roomBusiness.setNewCustomerName(newCustomer.getName());
        String id = roomBusinessDao.save(roomBusiness);
        return id;
    }


    @Override
    public PageVo pageQuery(RoomBusinessBo bo) {
        PageVo vo = new PageVo();
        Long total = roomBusinessDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<RoomBusiness> roomBusinessList = roomBusinessDao.query(bo);
        List<RoomBusinessVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(roomBusinessList, RoomBusinessVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public RoomBusinessVo findById(String id) {
        RoomBusiness roomBusiness = roomBusinessDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(roomBusiness, RoomBusinessVo.class);
    }

    @Override
    public void doCallback(RoomBusiness roomBusiness, RoomBusinessVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        // 交易类型
        vo.setTypeName(container.getBusinessName(RoomBusiness.TYPE, roomBusiness.getType()));
    }
}
