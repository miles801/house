package com.michael.spec.service.impl;

import com.michael.base.common.BaseParameter;
import com.michael.docs.annotations.ApiField;
import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.michael.poi.adapter.AnnotationCfgAdapter;
import com.michael.poi.core.Context;
import com.michael.poi.core.Handler;
import com.michael.poi.core.ImportEngine;
import com.michael.poi.core.RuntimeContext;
import com.michael.poi.imp.cfg.Configuration;
import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.CustomerDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.dao.RoomRentDao;
import com.michael.spec.domain.*;
import com.michael.spec.service.*;
import com.michael.spec.vo.BuildingVo;
import com.michael.spec.vo.CustomerVo;
import com.michael.spec.vo.RoomVo;
import com.ycrl.core.SystemContainer;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.HibernateUtils;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.string.StringUtils;
import eccrm.base.attachment.AttachmentProvider;
import eccrm.base.attachment.utils.AttachmentHolder;
import eccrm.base.attachment.vo.AttachmentVo;
import eccrm.base.parameter.dao.BusinessParamItemDao;
import eccrm.base.parameter.service.ParameterContainer;
import eccrm.utils.BeanCopyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private RoomRentDao roomRentDao;

    @Override
    public String save(Room room) {
        if (StringUtils.isEmpty(room.getStatus())) {
            room.setStatus(Room.STATUS_INACTIVE);
        }
        // 生成房屋编号
        String key = room.getRoomKey();
        if (StringUtils.isEmpty(key)) {
            key = roomDao.maxKey(room.getBuildingId());
        }
        if (StringUtils.isEmpty(key)) {
            String buildingId = room.getBuildingId();
            Building building = buildingDao.findById(buildingId);
            Assert.notNull(building, "操作失败!楼盘不存在!");
            String code = building.getCode();
            // 默认按照楼盘的编号来对房屋进行编号
            // 如果楼盘的编号为空，则使用楼盘的名称的首写字母
            String keyCode = "";
            if (StringUtils.isNotEmpty(code)) {
                keyCode = code;
            } else {
                String name = building.getName();
                for (char c : name.toCharArray()) {
                    keyCode += new SimplePinYin().toPinYin(c + "", new StandardStrategy()).substring(0, 1);
                }
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
        String content = compare(room, origin, new String[]{"id", "creatorId", "creatorName", "modifierId", "modifierName", "createdDatetime", "modifiedDatetime"});
        news.setContent(content);
        if (StringUtils.isNotEmpty(news.getContent())) {
            SystemContainer.getInstance().getBean(RoomNewsService.class).save(news);
        }
        BeanCopyUtils.copyPropertiesExclude(room, origin, new String[]{"id"});
    }

    @Override
    public void addCustomer(String id, Customer customer, RoomBusiness roomBusiness) {
        Assert.hasText(id, "操作失败!ID不能为空!");
        Assert.notNull(customer, "操作失败!客户信息不能为空!");
        Room room = roomDao.findRoomById(id);
        Assert.notNull(room, "操作失败!房屋已经不存在，请刷新后重试!");

        SystemContainer beanContainer = SystemContainer.getInstance();
        RoomNewsService roomNewsService = beanContainer.getBean(RoomNewsService.class);
        String customerId = customer.getId();
        // 给客户设置所属楼栋
        String buildingId = room.getBuildingId();
        customer.setBuildingId(buildingId);


        RoomNews news = new RoomNews();
        if (StringUtils.isEmpty(customerId)) {
            customer.setStatus(Room.STATUS_APPLY_ADD);
            customerId = beanContainer.getBean(CustomerService.class).save(customer);
        } else {
            beanContainer.getBean(CustomerService.class).update(customer);
        }


        // 设置变更信息（添加到最新动态）
        String originCustomerId = room.getCustomerId();
        news.setRoomId(id);
        if (StringUtils.isEmpty(originCustomerId)) {
            news.setContent(String.format("添加客户：<span style=\"color:#ff0000\">%s</span>", customer.getName()));
        } else if (!StringUtils.equals(originCustomerId, customerId)) {
            Customer originCustomer = customerDao.findById(originCustomerId);
            news.setContent(String.format("变更客户：<span>%s</span><span style=\"margin:0 15px;\">--></span><span style=\"color:#ff0000;font-weight:700;\">%s</span>", originCustomer != null ? originCustomer.getName() : "", customer.getName()));

            // 保存成交记录
            Assert.notNull(roomBusiness, "变更客户时成交记录不能为空!");
            Assert.notNull(roomBusiness.getPrice(), "变更客户时，成交价格不能为空!");
            roomBusiness.setRoomId(id);
            roomBusiness.setOriginCustomerId(originCustomerId);
            roomBusiness.setNewCustomerId(customerId);
            beanContainer.getBean(RoomBusinessService.class).save(roomBusiness);
        }

        // 保存最新动态
        if (StringUtils.isNotEmpty(news.getContent())) {
            roomNewsService.save(news);
        }

        // 给房屋设置客户ID
        room.setCustomerId(customerId);
    }

    @Override
    public void addRent(String roomId, Customer customer, RoomRent roomRent) {
        Assert.hasText(roomId, "操作失败!ID不能为空!");
        Assert.notNull(customer, "操作失败!租户信息不能为空!");
        customer.setRent(true);
        Assert.notNull(roomRent, "操作失败!添加/变更租户时，租赁信息不能为空!");
        Room room = roomDao.findRoomById(roomId);
        room.setOnRent(false);
        room.setHouseUseType("4");  // 租赁
        Assert.notNull(room, "操作失败!房屋已经不存在，请刷新后重试!");

        SystemContainer beanContainer = SystemContainer.getInstance();
        RoomNewsService roomNewsService = beanContainer.getBean(RoomNewsService.class);
        // 给客户设置所属楼栋
        String buildingId = room.getBuildingId();
        customer.setBuildingId(buildingId);


        RoomNews news = new RoomNews();
        news.setRoomId(roomId);
        String customerId = customer.getId();
        if (StringUtils.isEmpty(customerId)) {
            customerId = beanContainer.getBean(CustomerService.class).save(customer);
        } else {
            beanContainer.getBean(CustomerService.class).update(customer);
        }
        news.setContent(String.format("添加租户：<span style=\"color:#ff0000\">%s</span>", customer.getName()));


        // 保存最新动态
        roomNewsService.save(news);

        // 保存租赁信息
        roomRent.setRoomId(roomId);
        roomRent.setRoomKey(room.getRoomKey());
        roomRent.setNewCustomerId(customerId);
        roomRent.setNewCustomerName(customer.getName());
        RoomRentService roomRentService = SystemContainer.getInstance().getBean(RoomRentService.class);
        roomRentService.save(roomRent);
    }

    @Override
    public void changeRent(String roomId, Customer customer, RoomRent roomRent) {
        Assert.hasText(roomId, "操作失败!ID不能为空!");
        Assert.notNull(customer, "操作失败!租户信息不能为空!");
        customer.setRent(true);
        customer.setType("2");// 租客
        Assert.notNull(roomRent, "操作失败!添加/变更租户时，租赁信息不能为空!");
        Room room = roomDao.findRoomById(roomId);
        room.setHouseUseType("4");  // 租赁
        room.setOnRent(false);
        Assert.notNull(room, "操作失败!房屋已经不存在，请刷新后重试!");

        SystemContainer beanContainer = SystemContainer.getInstance();
        RoomNewsService roomNewsService = beanContainer.getBean(RoomNewsService.class);
        // 给客户设置所属楼栋
        String buildingId = room.getBuildingId();
        customer.setBuildingId(buildingId);


        // 获取之前的租户信息
        RoomRent rr = roomRentDao.findCurrent(roomId);
        Assert.notNull(rr, "变更租户失败!没有查询到该房屋的租赁信息!请刷新后重试");
        Assert.isTrue(!rr.getNewCustomerId().equals(customer), "操作失败!租户变更时不能是同一个租户!");
        // 将状态置为完成
        rr.setFinish(true);
        RoomNews news = new RoomNews();
        news.setRoomId(roomId);
        String customerId = beanContainer.getBean(CustomerService.class).save(customer);
        news.setContent(String.format("变更租户：<span>%s</span><span style=\"margin:0 15px;\">--></span><span style=\"color:#ff0000;font-weight:700;\">%s</span>", rr.getNewCustomerName(), customer.getName()));


        // 保存最新动态
        roomNewsService.save(news);

        // 保存租赁信息
        roomRent.setRoomId(roomId);
        roomRent.setRoomKey(room.getRoomKey());
        roomRent.setNewCustomerId(customerId);
        roomRent.setNewCustomerName(customer.getName());
        roomRent.setOriginCustomerId(rr.getNewCustomerId());
        roomRent.setOriginCustomerName(rr.getNewCustomerName());
        RoomRentService roomRentService = SystemContainer.getInstance().getBean(RoomRentService.class);
        roomRentService.save(roomRent);
    }

    @Override
    public void updateRent(String roomId, Customer customer, RoomRent roomRent) {
        // 更新客户信息
        SystemContainer.getInstance().getBean(CustomerService.class).update(customer);

        // 更新租赁信息（并记录变更记录，如果发生了变更）
        String roomRentId = roomRent.getId();
        Assert.notNull(roomRentId, "更新租户信息失败!原租赁信息不能为空!");
        RoomRent origin = roomRentDao.findById(roomRentId);
        String content = compare(roomRent, origin, new String[]{"creatorId", "creatorName", "modifierId", "modifierName", "createdDatetime", "modifiedDatetime"});
        if (StringUtils.isNotEmpty(content)) {
            // 租赁信息也发生了变更，需要产生一个记录
            CustomerNews news = new CustomerNews();
            news.setContent(content);
            news.setCustomerId(customer.getId());
            SystemContainer.getInstance().getBean(CustomerNewsService.class).save(news);
        }
        HibernateUtils.evict(origin);
        roomRentDao.update(roomRent);
    }

    @Override
    public RoomRent findCurrent(String roomId) {
        return roomRentDao.findCurrent(roomId);
    }

    @Override
    public void deleteRent(String rentId) {
        Assert.hasText(rentId, "操作失败!租赁ID不能为空!");
        RoomRent roomRent = roomRentDao.findById(rentId);
        Assert.notNull(roomRent, "操作失败!租赁信息不存在，请刷新后重试!");
        roomRent.setFinish(true);

        // 生成动态信息
        String roomId = roomRent.getRoomId();
        RoomNews news = new RoomNews();
        news.setRoomId(roomId);
        news.setContent(String.format("删除租户：<span>%s</span><span style=\"margin:0 15px;\">--></span><span style=\"color:#ff0000;font-weight:700;\"></span>", roomRent.getNewCustomerName()));
        SystemContainer.getInstance().getBean(RoomNewsService.class).save(news);
    }

    /**
     * @param newO 新对象
     * @param oriO 原始对象
     * @param <T>  类型
     * @return 不同的类容
     */
    private <T> String compare(final T newO, final T oriO, final String[] exclude) {
        final StringBuilder builder = new StringBuilder();
        ReflectionUtils.doWithFields(newO.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                String fieldName = field.getName();
                // 排除指定名称的属性
                if (exclude != null && ArrayUtils.contains(exclude, fieldName)) {
                    return;
                }
                field.setAccessible(true);
                Object newValue = field.get(newO);
                Object oldValue = field.get(oriO);
                field.setAccessible(false);
                if (newValue == oldValue) {
                    return;
                }
                ApiField nameAnno = field.getAnnotation(ApiField.class);
                if (nameAnno == null) {
                    return;
                }

                String name = nameAnno.value();
                ParameterContainer container = ParameterContainer.getInstance();
                if (fieldName.equals("sex")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(BaseParameter.SEX, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(BaseParameter.SEX, oldValue.toString());
                    }
                } else if (fieldName.equals("age")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(Customer.AGE_STAGE, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(Customer.AGE_STAGE, oldValue.toString());
                    }
                } else if (fieldName.equals("education")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(BaseParameter.EDU, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(BaseParameter.EDU, oldValue.toString());
                    }
                } else if (fieldName.equals("money")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(Customer.MONEY_STAGE, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(Customer.MONEY_STAGE, oldValue.toString());
                    }
                } else if (fieldName.equals("marriage")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(BaseParameter.MARRIAGE, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(BaseParameter.MARRIAGE, oldValue.toString());
                    }
                } else if (fieldName.equals("status")) {
                    if (newValue != null) {
                        newValue = container.getSystemName(HouseParams.HOUSE_STATUS, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getSystemName(HouseParams.HOUSE_STATUS, oldValue.toString());
                    }
                } else if (fieldName.equals("orient")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(HouseParams.ORIENT, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(HouseParams.ORIENT, oldValue.toString());
                    }
                } else if (fieldName.equals("houseProperty")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(HouseParams.HOUSE_PROPERTY, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(HouseParams.HOUSE_PROPERTY, oldValue.toString());
                    }
                } else if (fieldName.equals("houseUseType")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(HouseParams.HOUSE_USE_TYPE, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(HouseParams.HOUSE_USE_TYPE, oldValue.toString());
                    }
                } else if (fieldName.equals("rentUsage")) {
                    if (newValue != null) {
                        newValue = container.getBusinessName(HouseParams.RENT_USAGE, newValue.toString());
                    }
                    if (oldValue != null) {
                        oldValue = container.getBusinessName(HouseParams.RENT_USAGE, oldValue.toString());
                    }
                } else if (fieldName.equals("rent") || fieldName.equals("onSale") || fieldName.equals("onRent")) {
                    oldValue = (oldValue == null || !(boolean) oldValue) ? "否" : "是";
                    newValue = (newValue == null || !(boolean) newValue) ? "否" : "是";
                }
                if (newValue != null && newValue instanceof Date) {
                    newValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newValue);
                }
                if (oldValue != null && oldValue instanceof Date) {
                    oldValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(oldValue);
                }
                if (newValue == null) {
                    builder.append("<p>" + name + "：" + oldValue.toString() + " --> </p>");
                }
                if (oldValue == null) {
                    builder.append("<p>" + name + "：" + " --> " + newValue.toString() + "</p>");
                }
                if (newValue != null && oldValue != null && !newValue.equals(oldValue)) {
                    builder.append("<p>" + name + "：" + oldValue.toString() + " --> " + newValue.toString() + "</p>");
                }
            }
        });
        return builder.toString();
    }


    @Override
    public void batchAdd(String[] ids) {
        Assert.notEmpty(ids, "操作失败!ID不能为空!");
        for (String id : ids) {
            Room room = roomDao.findRoomById(id);
            // 只有“未录入”可以申请为新增
            if (room != null && StringUtils.include(room.getStatus(), Room.STATUS_INACTIVE, Room.STATUS_INVALID_ADD, Room.STATUS_INVALID)) {
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
        for (String id : ids) {
            Room room = roomDao.findRoomById(id);
            if (room != null && Room.STATUS_ACTIVE.equals(room.getStatus())) {
                room.setStatus(Room.STATUS_APPLY_INVALID);
            }
        }
    }

    @Override
    public void batchPass(String[] ids) {
        ParameterContainer parameterContainer = ParameterContainer.getInstance();
        for (String id : ids) {
            Room room = roomDao.findRoomById(id);
            if (room == null) {
                continue;
            }
            String originStatus = room.getStatus();
            String newStatus = null;
            if (Room.STATUS_APPLY_INVALID.equals(originStatus)) {     // 无效申请  -->  电话无效
                newStatus = Room.STATUS_INVALID;
            } else if (Room.STATUS_APPLY_ADD.equals(originStatus)) {    // 新增申请 --> 正常
                newStatus = Room.STATUS_ACTIVE;
            }
            room.setStatus(Room.STATUS_ACTIVE);

            // 同时将该房屋对应的客户的状态也变为正常
            String customerId = room.getCustomerId();
            if (StringUtils.isNotEmpty(customerId)) {
                Customer customer = customerDao.findById(customerId);
                Assert.notNull(customer, "数据错误!房屋对应的客户已经不存在，请与管理员联系!房屋编号[" + room.getRoomKey() + "]");
                String customerStatus = customer.getStatus();
                if (originStatus.equals(customerStatus)) { // 只有新增申请的客户状态才需要同时变更
                    customer.setStatus(newStatus);

                    // 写入客户最新状态
                    CustomerNews customerNews = new CustomerNews();
                    customerNews.setCustomerId(customerId);
                    String template = "状态：<span style=\"margin:0 15px;\">%s</span>--><span style=\"font-weight:700;color:#ff0000;margin:0 15px;\">%s</spa>";
                    customerNews.setContent(String.format(template, parameterContainer.getSystemName(HouseParams.HOUSE_STATUS, originStatus), parameterContainer.getSystemName(HouseParams.HOUSE_STATUS, newStatus)));
                    SystemContainer.getInstance().getBean(CustomerNewsService.class).save(customerNews);

                }
            }
        }
    }

    @Override
    public void batchDeny(String[] ids) {
        ParameterContainer parameterContainer = ParameterContainer.getInstance();
        for (String id : ids) {
            Room room = roomDao.findRoomById(id);
            if (room == null) {
                continue;
            }
            String originStatus = room.getStatus();
            String newStatus = null;
            if (Room.STATUS_APPLY_INVALID.equals(originStatus)) {         // 无效电话申请 --> 正常
                newStatus = Room.STATUS_ACTIVE;
            } else if (Room.STATUS_APPLY_ADD.equals(originStatus)) {    // 申请新增 --> 新增无效
                newStatus = Room.STATUS_INVALID_ADD;
            }

            room.setStatus(newStatus);

            // 对应的客户信息也要变更为“新增无效”
            String customerId = room.getCustomerId();
            if (StringUtils.isNotEmpty(customerId)) {
                Customer customer = customerDao.findById(customerId);
                Assert.notNull(customer, "数据错误!房屋对应的客户已经不存在，请与管理员联系!房屋编号[" + room.getRoomKey() + "]");
                String customerStatus = customer.getStatus();
                if (originStatus.equals(customerStatus)) { // 只有新增申请的客户状态才需要同时变更
                    customer.setStatus(newStatus);

                    // 写入客户最新状态
                    CustomerNews customerNews = new CustomerNews();
                    customerNews.setCustomerId(customerId);
                    String template = "状态：<span style=\"margin:0 15px;\">%s</span>--><span style=\"font-weight:700;color:#ff0000;margin:0 15px;\">%s</spa>";
                    customerNews.setContent(String.format(template, parameterContainer.getSystemName(HouseParams.HOUSE_STATUS, originStatus), parameterContainer.getSystemName(HouseParams.HOUSE_STATUS, newStatus)));
                    SystemContainer.getInstance().getBean(CustomerNewsService.class).save(customerNews);

                }
            }
        }
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
        String statusName = container.getSystemName(HouseParams.HOUSE_STATUS, roomView.getStatus());
        roomView.setStatusName(statusName);
        roomView.setOrientName(container.getBusinessName(HouseParams.ORIENT, roomView.getOrient()));
        roomView.setHousePropertyName(container.getBusinessName(HouseParams.HOUSE_PROPERTY, roomView.getHouseProperty()));
        String houseUseTypeName = container.getBusinessName(HouseParams.HOUSE_USE_TYPE, roomView.getHouseUseType());
        if (StringUtils.isEmpty(houseUseTypeName)) {
            houseUseTypeName = "";
        }
        if (roomView.getOnRent() != null && roomView.getOnRent()) {
            houseUseTypeName += "; 待租";
        }
        if (roomView.getOnSale() != null && roomView.getOnSale()) {
            houseUseTypeName += "; 待售";
        }
        roomView.setHouseUseTypeName(houseUseTypeName);
        // 设置客户信息
        roomView.setAgeName(container.getBusinessName(Customer.AGE_STAGE, roomView.getAge()));
        roomView.setMoneyName(container.getBusinessName(Customer.MONEY_STAGE, roomView.getMoney()));
        roomView.setSexName(container.getBusinessName(BaseParameter.SEX, roomView.getSex()));
        roomView.setEducationName(container.getBusinessName(BaseParameter.EDU, roomView.getEducation()));
        roomView.setMarriageName(container.getBusinessName(BaseParameter.MARRIAGE, roomView.getMarriage()));

    }

    @Override
    public RoomVo findByCode(String code) {
        return BeanWrapBuilder.newInstance()
                .wrap(roomDao.findByCode(code), RoomVo.class);
    }

    @Override
    public void importData(String[] attachmentIds) {
        Logger logger = Logger.getLogger(CustomerServiceImpl.class);
        Assert.notEmpty(attachmentIds, "数据文件不能为空，请重试!");

        for (String id : attachmentIds) {
            AttachmentVo vo = AttachmentProvider.getInfo(id);
            Assert.notNull(vo, "附件已经不存在，请刷新后重试!");
            final File file = AttachmentHolder.newInstance().getTempFile(id);
            logger.info("准备导入数据：" + file.getAbsolutePath());
            logger.info("初始化导入引擎....");
            long start = System.currentTimeMillis();

            // 初始化引擎
            Configuration configuration = new AnnotationCfgAdapter(RoomDTO.class).parse();
            configuration.setStartRow(2);
            String newFilePath = file.getAbsolutePath() + vo.getFileName().substring(vo.getFileName().lastIndexOf(".")); //获取路径
            try {
                FileUtils.copyFile(file, new File(newFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            final Map<String, String> params = new HashMap<String, String>();
            final SystemContainer beanContainer = SystemContainer.getInstance();
            final BusinessParamItemDao bpiDao = beanContainer.getBean(BusinessParamItemDao.class);
            // 获取session
            SessionFactory sessionFactory = (SessionFactory) beanContainer.getBean("sessionFactory");
            final Session session = sessionFactory.getCurrentSession();
            final ParameterContainer parameterContainer = ParameterContainer.getInstance();
            configuration.setPath(newFilePath);
            configuration.setHandler(new Handler<RoomDTO>() {
                @Override
                public void execute(RoomDTO dto) {
                    Context context = RuntimeContext.get();
                    Room room = new Room();
                    BeanUtils.copyProperties(dto, room);
                    if (BeanCopyUtils.isEmpty(room)) {
                        return;
                    }
                    // 设置楼盘
                    String buildingName = dto.getBuildingName();
                    Assert.hasText(buildingName, String.format("数据错误,楼盘/小区名称不能为空!发生在第%d行!", RuntimeContext.get().getRowIndex()));
                    String buildingId = (String) session.createQuery("select b.id from " + Building.class.getName() + " b where b.name=?")
                            .setParameter(0, buildingName)
                            .setMaxResults(1)
                            .uniqueResult();
                    if (StringUtils.isEmpty(buildingId)) {
                        Building building = new Building();
                        building.setName(buildingName);
                        buildingId = beanContainer.getBean(BuildingService.class).save(building);
                    }
                    room.setBuildingId(buildingId);

                    // 设置楼栋
                    String blockName = dto.getBlockName();
                    Assert.hasText(blockName, String.format("数据错误,楼栋信息不能为空!发生在第%d行!", RuntimeContext.get().getRowIndex()));
                    String blockId = (String) session.createQuery("select b.id from " + Block.class.getName() + " b where b.code=? and b.buildingId=?")
                            .setParameter(0, blockName)
                            .setParameter(1, buildingId)
                            .setMaxResults(1)
                            .uniqueResult();
                    if (StringUtils.isEmpty(blockId)) {
                        Block block = new Block();
                        block.setBuildingId(buildingId);
                        block.setCode(blockName);
                        blockId = beanContainer.getBean(BlockService.class).save(block);
                    }
                    room.setBlockId(blockId);

                    // 设置单元
                    Integer floor = dto.getFloor();
                    Assert.isTrue(floor != null && floor > 0, String.format("数据错误,楼层必须大于0!发生在第%d行!", RuntimeContext.get().getRowIndex()));
                    String doorCode = dto.getCode();
                    Assert.hasText(doorCode, String.format("数据错误,门牌号不能为空!发生在第%d行!", RuntimeContext.get().getRowIndex()));
                    Assert.isTrue(doorCode.startsWith(floor + ""), String.format("数据错误!门牌号与楼层不匹配!发生在第%d行!", RuntimeContext.get().getRowIndex()));
                    String dc = doorCode.substring((floor + "").length());
                    String unitName = dto.getUnitName();
                    Assert.hasText(unitName, String.format("数据错误,单元信息不能为空!发生在第%d行!", RuntimeContext.get().getRowIndex()));
                    String unitId = (String) session.createQuery("select b.id from " + Unit.class.getName() + " b where b.code=? and b.blockId=? and b.doorCode=?")
                            .setParameter(0, unitName)
                            .setParameter(1, blockId)
                            .setParameter(2, dc)
                            .setMaxResults(1)
                            .uniqueResult();
                    if (StringUtils.isEmpty(unitId)) {
                        Unit unit = new Unit();
                        unit.setBlockId(blockId);
                        unit.setCode(unitName);
                        unitId = beanContainer.getBean(UnitService.class).save(unit);
                    }
                    room.setUnitId(unitId);

                    // 设置参数
                    // 朝向
                    room.setOrient(parameterContainer.getBusinessValue(HouseParams.ORIENT, dto.getOrient()));
                    // 产权性质
                    room.setHouseProperty(parameterContainer.getBusinessValue(HouseParams.HOUSE_PROPERTY, dto.getHouseProperty()));
                    room.setCol1(dto.getC1());
                    // 房屋现状
                    room.setHouseUseType(parameterContainer.getBusinessValue(HouseParams.HOUSE_USE_TYPE, dto.getHouseUseType()));
                    // 设置参数-状态
                    room.setStatus(Room.STATUS_APPLY_ADD);


                    // 设置客户
                    String cusName = dto.getCusName();
                    String cusPhone = dto.getCusPhone();
                    if (StringUtils.isNotEmpty(cusName)) {
                        String cusId = (String) session.createQuery("select c.id from " + Customer.class.getName() + " c where c.name=? and c.phone1=? and c.buildingId=?")
                                .setParameter(0, cusName)
                                .setParameter(1, cusPhone)
                                .setParameter(2, buildingId)
                                .setMaxResults(1)
                                .uniqueResult();
                        if (StringUtils.isEmpty(cusId)) {
                            Customer customer = new Customer();
                            customer.setName(cusName);
                            customer.setIdCard(dto.getCusIDCard());
                            customer.setSex(parameterContainer.getBusinessValue(BaseParameter.SEX, dto.getCusSex()));
                            customer.setAge(parameterContainer.getBusinessValue(Customer.AGE_STAGE, dto.getCusAge()));
                            customer.setMarriage(parameterContainer.getBusinessValue(BaseParameter.MARRIAGE, dto.getCusMarriage()));
                            customer.setFamilyCounts(dto.getCusFamilyCount());
                            customer.setPhone1(cusPhone);
                            customer.setPhone2(dto.getCusPhone2());
                            customer.setPhone3(dto.getCusPhone3());
                            customer.setEmail(dto.getCusEmail());
                            customer.setWechat(dto.getCusWechat());
                            customer.setDuty(dto.getCusDuty());
                            customer.setCompany(dto.getCusCompany());
                            String eduction = dto.getCusEducation();
                            if (StringUtils.isNotEmpty(eduction)) {
                                customer.setEducation(parameterContainer.getBusinessValue(BaseParameter.EDU, eduction));
                            }
                            String money = dto.getCusMoney();
                            if (StringUtils.isNotEmpty(money)) {
                                customer.setMoney(parameterContainer.getBusinessValue(Customer.MONEY_STAGE, money));
                            }
                            customer.setCarSite1(dto.getCusCarSite1());
                            customer.setCarSite2(dto.getCusCarSite2());
                            customer.setCarNo(dto.getCusCarNo());
                            customer.setCarType(dto.getCusCarType());
                            customer.setC1(dto.getCusDescription());
                            customer.setBuildingId(buildingId);
                            customer.setBuildingName(buildingName);
                            customer.setStatus(Room.STATUS_APPLY_ADD);
                            cusId = beanContainer.getBean(CustomerService.class).save(customer);
                        }
                        room.setCustomerId(cusId);
                    }

                    // 真的保存房屋信息
                    try {
                        // 去重
                        Room originRoom = roomDao.findSame(unitId, room.getCode(), room.getFloor());
                        String roomId = null;
                        if (originRoom == null) {
                            roomId = save(room);
                            // 给客户添加一套房源
                            if (StringUtils.isNotEmpty(room.getCustomerId())) {
                                beanContainer.getBean(CustomerService.class).addRoom(room.getCustomerId(), roomId);
                            }
                        } else {
                            String status = originRoom.getStatus();
                            // 无效数据，进行覆盖
                            if (StringUtils.include(status, Room.STATUS_INVALID, Room.STATUS_INACTIVE, Room.STATUS_INACTIVE, Room.STATUS_INVALID_ADD)) {

                                // 保存日志
                                RoomNews news = new RoomNews();
                                String content = compare(room, originRoom, new String[]{"creatorId", "creatorName", "modifierId", "modifierName", "createdDatetime", "modifiedDatetime"});
                                news.setContent(content);
                                news.setRoomId(originRoom.getId());
                                news.setEmpId(SecurityContext.getEmpId());
                                news.setEmpName(SecurityContext.getEmpName());
                                SystemContainer.getInstance().getBean(RoomNewsService.class).save(news);
                                // 覆盖信息
                                roomId = originRoom.getId();
                                originRoom.setOrient(room.getOrient());
                                originRoom.setType1(room.getType1());
                                originRoom.setType2(room.getType2());
                                originRoom.setType3(room.getType3());
                                originRoom.setType4(room.getType4());
                                originRoom.setSquare(room.getSquare());
                                originRoom.setHouseProperty(room.getHouseProperty());
                                originRoom.setHouseUseType(room.getHouseUseType());
                                // 如果客户ID并不一致，则使用新的
                                if (!StringUtils.equals(originRoom.getCustomerId(), room.getCustomerId())) {
                                    originRoom.setCustomerId(room.getCustomerId());
                                }
                                if (StringUtils.isEmpty(originRoom.getCustomerId()) && StringUtils.isNotEmpty(room.getCustomerId())) {
                                    beanContainer.getBean(CustomerService.class).addRoom(room.getCustomerId(), roomId);
                                }
                            }
                        }

                    } catch (Exception e) {
                        Assert.isTrue(false, String.format("数据异常!发生在第%d行!原因:%s", context.getRowIndex(), e.getMessage()));
                    }
                }
            });
            logger.info("开始导入数据....");
            ImportEngine engine = new ImportEngine(configuration);
            try {
                engine.execute();
            } catch (Exception e) {
                Assert.isTrue(false, String.format("数据异常!发生在第%d行,%d列!原因:%s", RuntimeContext.get().getRowIndex() + 1, RuntimeContext.get().getCellIndex() + 1, e.getMessage()));
            }
            logger.info(String.format("导入数据成功,用时(%d)s....", (System.currentTimeMillis() - start) / 1000));
            new File(newFilePath).delete();
        }
    }

    @Override
    public void doCallback(RoomView room, RoomVo vo) {
    }
}
