package com.michael.spec.service.impl;

import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.CustomerDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.Building;
import com.michael.spec.domain.Customer;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.RoomView;
import com.michael.spec.service.CustomerService;
import com.michael.spec.service.HouseParams;
import com.michael.spec.service.RoomService;
import com.michael.spec.vo.RoomVo;
import com.ycrl.core.SystemContainer;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
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
@Service("roomService")
public class RoomServiceImpl implements RoomService, BeanWrapCallback<RoomView, RoomVo> {
    @Resource
    private RoomDao roomDao;

    @Resource
    private BuildingDao buildingDao;

    @Resource
    private CustomerDao customerDao;

    @Override
    public String save(Room room) {
        // 生成房屋编号
        room.setStatus(Room.STATUS_INACTIVE);
        String key = roomDao.maxKey(room.getBuildingId());
        if (key == null) {
            String buildingId = room.getBuildingId();
            Building building = buildingDao.findById(buildingId);
            Assert.notNull(building, "操作失败!楼盘不存在!");
            String name = building.getName();
            String keyCode = "";
            for (char c : name.toCharArray()) {
                keyCode += new SimplePinYin().toPinYin(c + "", new StandardStrategy()).substring(0, 1);
            }
            room.setRoomKey(keyCode.toUpperCase() + "-000001");
        } else {
            int index = key.indexOf("-");
            String no = key.substring(index + 1);
            String newKey = new DecimalFormat("000000").format(Integer.parseInt(no) + 1);
            room.setRoomKey(key.substring(0, index) + "-" + newKey);
        }
        ValidatorUtils.validate(room);
        String id = roomDao.save(room);
        return id;
    }

    @Override
    public void update(Room room) {
        ValidatorUtils.validate(room);
        roomDao.update(room);
    }

    @Override
    public void addCustomer(String id, Customer customer) {
        Assert.hasText(id, "操作失败!ID不能为空!");
        Assert.notNull(customer, "操作失败!客户信息不能为空!");
        String customerId = customer.getId();
        if (StringUtils.isEmpty(customerId)) {
            customerId = SystemContainer.getInstance().getBean(CustomerService.class).save(customer);
        } else {
            customerDao.update(customer);
        }
        Room room = roomDao.findById(id);
        Assert.notNull(room, "操作失败!房屋已经不存在，请刷新后重试!");
        room.setCustomerId(customerId);
    }

    @Override
    public void setRoomInfo(Room room) {

    }

    @Override
    public PageVo pageQuery(RoomBo bo) {
        PageVo vo = new PageVo();
        Long total = roomDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        ParameterContainer container = ParameterContainer.getInstance();
        List<RoomView> roomViews = roomDao.query(bo);
        for (RoomView roomView : roomViews) {
            roomView.setStatusName(container.getSystemName(HouseParams.HOUSE_STATUS, roomView.getStatus()));
            roomView.setOrientName(container.getBusinessName(HouseParams.ORIENT, roomView.getOrient()));
            roomView.setHousePropertyName(container.getBusinessName(HouseParams.HOUSE_PROPERTY, roomView.getHouseProperty()));
            roomView.setHouseUseTypeName(container.getBusinessName(HouseParams.HOUSE_USE_TYPE, roomView.getHouseUseType()));
        }
        vo.setData(roomViews);
        return vo;
    }


    @Override
    public RoomVo findById(String id) {
        Room room = roomDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(room, RoomVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            roomDao.deleteById(id);
        }
    }

    @Override
    public List<RoomView> query(RoomBo bo) {
        List<RoomView> roomViews = roomDao.query(bo);
        ParameterContainer container = ParameterContainer.getInstance();
        for (RoomView roomView : roomViews) {
            roomView.setStatusName(container.getSystemName(HouseParams.HOUSE_STATUS, roomView.getStatus()));
            roomView.setOrientName(container.getBusinessName(HouseParams.ORIENT, roomView.getOrient()));
            roomView.setHousePropertyName(container.getBusinessName(HouseParams.HOUSE_PROPERTY, roomView.getHouseProperty()));
            roomView.setHouseUseTypeName(container.getBusinessName(HouseParams.HOUSE_USE_TYPE, roomView.getHouseUseType()));
        }
        return roomViews;
    }

    @Override
    public void doCallback(RoomView room, RoomVo vo) {
    }
}
