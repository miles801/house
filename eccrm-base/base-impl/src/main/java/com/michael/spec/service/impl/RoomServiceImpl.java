package com.michael.spec.service.impl;

import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.CustomerDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.*;
import com.michael.spec.service.*;
import com.michael.spec.vo.BuildingVo;
import com.michael.spec.vo.CustomerVo;
import com.michael.spec.vo.RoomVo;
import com.ycrl.core.SystemContainer;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.string.StringUtils;
import eccrm.base.parameter.service.ParameterContainer;
import eccrm.utils.BeanCopyUtils;
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
        String id = room.getId();
        room.setId(null);
        Room origin = roomDao.findRoomById(id);
        Assert.notNull(origin, "更新失败!房屋不存在!请刷新后重试!");
        RoomNews news = new RoomNews();
        news.setRoomId(id);
        StringBuilder builder = new StringBuilder();
        String template = "<span style=\"margin:0 15px;\">--></span><span style=\"font-weight:700;color:#ff0000;\">";
        if (!StringUtils.equals(origin.getType1() + origin.getType2() + origin.getType3() + origin.getType4(), room.getType1() + room.getType2() + room.getType3() + room.getType4())) {
            builder.append("户型：").append(origin.getType1()).append("-").append(origin.getType2()).append("-").append(origin.getType3()).append("-").append(origin.getType4())
                    .append(template)
                    .append(room.getType1()).append("-").append(room.getType2()).append("-").append(room.getType3()).append("-").append(room.getType4())
                    .append("</spa><br/>");
        }
        ParameterContainer container = ParameterContainer.getInstance();
        if (!StringUtils.equals(origin.getOrient(), room.getOrient())) {
            builder.append("朝向：").append(container.getBusinessName(HouseParams.ORIENT, origin.getOrient()))
                    .append(template)
                    .append(container.getBusinessName(HouseParams.ORIENT, room.getOrient()))
                    .append("</spa><br/>");
        }
        if (!StringUtils.equals(origin.getHouseUseType(), room.getHouseUseType())) {
            builder.append("房屋现状：").append(container.getBusinessName(HouseParams.HOUSE_USE_TYPE, origin.getHouseUseType()))
                    .append(template)
                    .append(container.getBusinessName(HouseParams.HOUSE_USE_TYPE, room.getHouseUseType()))
                    .append("</spa><br/>");
        }
        if (!StringUtils.equals(origin.getSquare() + "", room.getSquare() + "")) {
            builder.append("面积：").append(origin.getSquare())
                    .append(template)
                    .append(room.getSquare())
                    .append("</spa><br/>");
        }
        news.setContent(builder.toString());
        SystemContainer.getInstance().getBean(RoomNewsService.class).save(news);
        BeanCopyUtils.copyPropertiesExclude(room, origin, new String[]{"id"});
    }

    @Override
    public void addCustomer(String id, Customer customer, RoomBusiness roomBusiness) {
        Assert.hasText(id, "操作失败!ID不能为空!");
        Assert.notNull(customer, "操作失败!客户信息不能为空!");
        SystemContainer beanContainer = SystemContainer.getInstance();
        RoomNewsService roomNewsService = beanContainer.getBean(RoomNewsService.class);
        CustomerService customerService = beanContainer.getBean(CustomerService.class);
        String customerId = customer.getId();
        if (StringUtils.isEmpty(customerId)) {
            customerId = beanContainer.getBean(CustomerService.class).save(customer);
        } else {
            customerService.update(customer);
        }
        Room room = roomDao.findRoomById(id);
        Assert.notNull(room, "操作失败!房屋已经不存在，请刷新后重试!");

        // 设置变更信息（添加到最新动态）
        String originCustomerId = room.getCustomerId();
        RoomNews news = new RoomNews();
        news.setRoomId(id);
        if (StringUtils.isEmpty(originCustomerId)) {
            news.setContent(String.format("添加业主：<span style=\"color:#ff0000\">%s</span>", customer.getName()));
        } else if (!StringUtils.equals(originCustomerId, customerId)) {
            Customer originCustomer = customerDao.findById(originCustomerId);
            news.setContent(String.format("变更业主：<span>%s</span><span style=\"margin:0 15px;\">--></span><span style=\"color:#ff0000;font-weight:700;\">%s</span>", originCustomer != null ? originCustomer.getName() : "", customer.getName()));

            // 保存成交记录
            Assert.notNull(roomBusiness, "变更业主时成交记录不能为空!");
            Assert.notNull(roomBusiness.getPrice(), "变更业主时，成交价格不能为空!");
            roomBusiness.setRoomId(id);
            roomBusiness.setOriginCustomerId(originCustomerId);
            roomBusiness.setNewCustomerId(customerId);
            beanContainer.getBean(RoomBusinessService.class).save(roomBusiness);
        } else {
            news.setContent("修改业主信息");
        }

        roomNewsService.save(news);

        room.setCustomerId(customerId);
    }


    @Override
    public void batchAdd(String[] ids) {
        Assert.notEmpty(ids, "操作失败!ID不能为空!");
        for (String id : ids) {
            Room room = roomDao.findRoomById(id);
            // 只有“未录入”可以申请为新增
            if (room != null && Room.STATUS_INACTIVE.equals(room.getStatus())) {
                room.setStatus(Room.STATUS_APPLY_ADD);
            }
        }
    }

    @Override
    public void batchModify(String[] ids) {
        Assert.notEmpty(ids, "操作失败!ID不能为空!");
        for (String id : ids) {
            Room room = roomDao.findRoomById(id);
            if (room == null) {
                continue;
            }
            room.setStatus(Room.STATUS_APPLY_MODIFY);
        }
    }

    @Override
    public void applyInvalid(String[] ids) {
        Assert.notEmpty(ids, "申请失败!房屋ID不能为空!");
        roomDao.batchSetStatus(ids, Room.STATUS_APPLY_INVALID);
    }

    @Override
    public void batchPass(String[] ids) {
        roomDao.batchSetStatus(ids, Room.STATUS_ACTIVE);
    }

    @Override
    public void batchDeny(String[] ids) {
        roomDao.batchSetStatus(ids, Room.STATUS_INACTIVE);
    }

    @Override
    public PageVo pageQuery(RoomBo bo) {
        PageVo vo = new PageVo();
        Long total = roomDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<RoomView> roomViews = roomDao.query(bo);
        for (RoomView roomView : roomViews) {
            wrapParamName(roomView);
        }
        vo.setData(roomViews);
        return vo;
    }


    @Override
    public RoomView findById(String id) {
        RoomView roomView = roomDao.findById(id);
        wrapParamName(roomView);
        return roomView;
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
        for (RoomView roomView : roomViews) {
            wrapParamName(roomView);
        }
        return roomViews;
    }

    @Override
    public CustomerVo getCustomer(String roomId) {
        Assert.hasText(roomId, "查询失败!房屋ID不能为空!");
        Room room = roomDao.findRoomById(roomId);
        Assert.notNull(room, "查询失败!房屋不存在!");
        String customerId = room.getCustomerId();
        if (StringUtils.isEmpty(customerId)) {
            return null;
        }
        return SystemContainer.getInstance().getBean(CustomerService.class).findById(customerId);
    }

    @Override
    public BuildingVo getBuilding(String roomId) {
        Assert.hasText(roomId, "查询失败!房屋ID不能为空!");
        Room room = roomDao.findRoomById(roomId);
        Assert.notNull(room, "查询失败!房屋不存在!");
        String buildingId = room.getBuildingId();
        if (StringUtils.isEmpty(buildingId)) {
            return null;
        }
        return SystemContainer.getInstance().getBean(BuildingService.class).findById(buildingId);
    }

    private void wrapParamName(RoomView roomView) {
        ParameterContainer container = ParameterContainer.getInstance();
        roomView.setStatusName(container.getSystemName(HouseParams.HOUSE_STATUS, roomView.getStatus()));
        roomView.setOrientName(container.getBusinessName(HouseParams.ORIENT, roomView.getOrient()));
        roomView.setHousePropertyName(container.getBusinessName(HouseParams.HOUSE_PROPERTY, roomView.getHouseProperty()));
        roomView.setHouseUseTypeName(container.getBusinessName(HouseParams.HOUSE_USE_TYPE, roomView.getHouseUseType()));
    }

    @Override
    public void doCallback(RoomView room, RoomVo vo) {
    }
}
