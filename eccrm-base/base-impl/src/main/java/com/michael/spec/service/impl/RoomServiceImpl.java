package com.michael.spec.service.impl;

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
import eccrm.base.attachment.AttachmentProvider;
import eccrm.base.attachment.utils.AttachmentHolder;
import eccrm.base.attachment.vo.AttachmentVo;
import eccrm.base.parameter.dao.BusinessParamItemDao;
import eccrm.base.parameter.dao.SysParamItemDao;
import eccrm.base.parameter.service.ParameterContainer;
import eccrm.utils.BeanCopyUtils;
import org.apache.commons.io.FileUtils;
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
        RoomNews news = new RoomNews();
        if (StringUtils.isEmpty(customerId)) {
            customerId = beanContainer.getBean(CustomerService.class).save(customer);
        } else {
            Customer originCus = customerDao.findById(customerId);
            Assert.notNull(originCus, "数据错误!客户不存在!" + customerId);
            // 比较两个对象并返回不一样的内容
            String content = compare(customer, originCus);
            news.setContent(content);
            BeanUtils.copyProperties(customer, originCus);
            customer.setId(null);
        }
        Room room = roomDao.findRoomById(id);
        Assert.notNull(room, "操作失败!房屋已经不存在，请刷新后重试!");

        // 设置变更信息（添加到最新动态）
        String originCustomerId = room.getCustomerId();
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

        }

        roomNewsService.save(news);

        room.setCustomerId(customerId);
    }

    private String compare(final Customer customer, final Customer originCus) {
        final StringBuilder builder = new StringBuilder("变更业主信息：");
        ReflectionUtils.doWithFields(Customer.class, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                field.setAccessible(true);
                Object newValue = field.get(customer);
                Object oldValue = field.get(originCus);
                field.setAccessible(false);
                if (newValue == oldValue) {
                    return;
                }
                ApiField nameAnno = field.getAnnotation(ApiField.class);
                if (nameAnno == null) {
                    return;
                }
                String name = nameAnno.value();
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
    public void importData(String[] attachmentIds) {
        Logger logger = Logger.getLogger(CustomerServiceImpl.class);
        Assert.notEmpty(attachmentIds, "数据文件不能为空，请重试!");

        for (String id : attachmentIds) {
            AttachmentVo vo = AttachmentProvider.getInfo(id);
            Assert.notNull(vo, "附件已经不存在，请刷新后重试!");
            File file = AttachmentHolder.newInstance().getTempFile(id);
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
            configuration.setPath(newFilePath);
            configuration.setHandler(new Handler<RoomDTO>() {
                @Override
                public void execute(RoomDTO dto) {
                    Context context = RuntimeContext.get();
                    Room room = new Room();
                    BeanUtils.copyProperties(dto, room);

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
                    String unitName = dto.getUnitName();
                    Assert.hasText(unitName, String.format("数据错误,单元信息不能为空!发生在第%d行!", RuntimeContext.get().getRowIndex()));
                    String unitId = (String) session.createQuery("select b.id from " + Unit.class.getName() + " b where b.code=? and b.blockId=?")
                            .setParameter(0, unitName)
                            .setParameter(1, blockId)
                            .setMaxResults(1)
                            .uniqueResult();
                    if (StringUtils.isEmpty(unitId)) {
                        Unit unit = new Unit();
                        unit.setBlockId(blockId);
                        unit.setCode(unitName);
                        unitId = beanContainer.getBean(UnitService.class).save(unit);
                    }
                    room.setUnitId(unitId);

                    // 设置参数-现状
                    String useType = dto.getHouseUseType();
                    if (StringUtils.isNotEmpty(useType)) {
                        String userTypeValue = params.get(useType);
                        if (StringUtils.isEmpty(userTypeValue)) {
                            userTypeValue = bpiDao.queryName(HouseParams.HOUSE_USE_TYPE, useType);
                            params.put(useType, userTypeValue);
                        }
                        room.setHouseUseType(userTypeValue);
                    }
                    // 设置参数-状态
                    room.setStatus(Room.STATUS_ACTIVE);
                    String status = dto.getStatus();
                    if (StringUtils.isNotEmpty(status)) {
                        String statusValue = params.get(status);
                        if (StringUtils.isEmpty(statusValue)) {
                            statusValue = beanContainer.getBean(SysParamItemDao.class).queryName(HouseParams.HOUSE_STATUS, status);
                            params.put(status, statusValue);
                        }
                        room.setStatus(statusValue);
                    }

                    // 设置业主
                    String cusName = dto.getCusName();
                    String cusPhone = dto.getCusPhone();
                    if (StringUtils.isNotEmpty(cusName)) {
                        String cusId = (String) session.createQuery("select c.id from " + Customer.class.getName() + " c where c.name=? and c.phone1=?")
                                .setParameter(0, cusName)
                                .setParameter(1, cusPhone)
                                .setMaxResults(1)
                                .uniqueResult();
                        if (StringUtils.isEmpty(cusId)) {
                            Customer customer = new Customer();
                            customer.setName(cusName);
                            customer.setPhone1(cusPhone);
                            cusId = beanContainer.getBean(CustomerService.class).save(customer);
                        }
                        room.setCustomerId(cusId);
                    }

                    // 真的保存房屋信息
                    try {
                        String roomId = save(room);

                        // 给客户添加一套房源
                        if (StringUtils.isNotEmpty(room.getCustomerId())) {
                            beanContainer.getBean(CustomerService.class).addRoom(room.getCustomerId(), roomId);
                        }
                    } catch (Exception e) {
                        Assert.isTrue(false, String.format("数据异常!发生在第%d行!原因:%s", context.getRowIndex(), e.getMessage()));
                    }
                }
            });
            logger.info("开始导入数据....");
            ImportEngine engine = new ImportEngine(configuration);
            engine.execute();
            logger.info(String.format("导入数据成功,用时(%d)s....", (System.currentTimeMillis() - start) / 1000));
            new File(newFilePath).delete();
        }
    }

    @Override
    public void doCallback(RoomView room, RoomVo vo) {
    }
}
