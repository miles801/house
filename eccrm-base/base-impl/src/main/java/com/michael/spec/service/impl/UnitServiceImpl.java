package com.michael.spec.service.impl;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.bo.UnitBo;
import com.michael.spec.dao.BlockDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.dao.UnitDao;
import com.michael.spec.domain.Block;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.Unit;
import com.michael.spec.service.HouseParams;
import com.michael.spec.service.RoomService;
import com.michael.spec.service.UnitService;
import com.michael.spec.vo.UnitVo;
import com.ycrl.core.SystemContainer;
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
import java.util.List;

/**
 * @author Michael
 */
@Service("unitService")
public class UnitServiceImpl implements UnitService, BeanWrapCallback<Unit, UnitVo> {
    @Resource
    private UnitDao unitDao;

    @Resource
    private RoomDao roomDao;

    @Resource
    private BlockDao blockDao;

    @Override
    public String save(Unit unit) {
        ValidatorUtils.validate(unit);
        String blockId = unit.getBlockId();
        // 验证重复
        UnitBo bo = new UnitBo();
        bo.setBlockId(blockId);
        bo.setCode(unit.getCode());
        bo.setDoorCode(unit.getDoorCode());
        Long total = unitDao.getTotal(bo);
        Assert.isTrue(total == null || total == 0, "保存失败!该楼栋下已经存在相同的单元编号和房屋编号!");

        // 校验关联
        Block block = blockDao.findById(blockId);
        Assert.notNull(block, "保存失败!该单元关联的楼栋不存在!请刷新后重试!");

        // 校验数量是否超越
//        bo.setDoorCode(null);
//        Integer counts = IntegerUtils.add(block.getUnitCounts(), 0);
//        Integer realCounts = IntegerUtils.add(block.getRealCounts(), 0);
//        if (realCounts < 0) {
//            realCounts = 0;
//        }
//        Assert.isTrue(counts >= realCounts + 1, "保存失败!该单元所关联的楼栋下已经具有足够的单元!请核对数据后再行尝试!");
        block.setRealCounts(IntegerUtils.add(block.getRealCounts(), 1));

        // 保存
        String id = unitDao.save(unit);
        return id;
    }

    @Override
    public void update(Unit unit) {
        long counts = roomDao.getUnitRoomCounts(unit.getId());
        Assert.isTrue(counts == 0, "更新失败!该单元下已经存在了房间信息，无法直接更新单元信息!");
        ValidatorUtils.validate(unit);
        unitDao.update(unit);
    }

    @Override
    public PageVo pageQuery(UnitBo bo) {
        PageVo vo = new PageVo();
        Long total = unitDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Unit> unitList = unitDao.query(bo);
        List<UnitVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(unitList, UnitVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public UnitVo findById(String id) {
        Unit unit = unitDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(unit, UnitVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            long counts = roomDao.getUnitRoomCounts(id);
            Assert.isTrue(counts == 0, "删除失败!该单元下已经存在了房间信息，无法直接删除!");

            Unit unit = unitDao.findById(id);
            if (unit == null) {
                continue;
            }
            // 楼栋的已注册单元数减1
            String blockId = unit.getBlockId();
            Block block = blockDao.findById(blockId);
            if (block != null) {
                Assert.isTrue(IntegerUtils.add(block.getRealCounts(), -1) >= 0, "操作失败!楼栋的单元数量不能小于0!");
                block.setRealCounts(IntegerUtils.add(block.getRealCounts(), -1));
            }
            // 删除单元
            unitDao.delete(unit);

        }
    }

    @Override
    public void forceDelete(String[] ids) {
        if (ids == null || ids.length == 0) return;
        RoomBo bo = new RoomBo();
        for (String id : ids) {
            bo.setUnitId(id);
            List<Room> rooms = roomDao.queryRoom(bo);
            RoomService roomService = SystemContainer.getInstance().getBean(RoomService.class);
            if (rooms != null) {
                String[] roomIds = new String[rooms.size()];
                for (int i = 0; i < roomIds.length; i++) {
                    roomIds[i] = rooms.get(i).getId();
                }
                roomService.deleteByIds(roomIds);
            }

            Unit unit = unitDao.findById(id);
            if (unit == null) {
                continue;
            }
            // 楼栋的已注册单元数减1
            String blockId = unit.getBlockId();
            Block block = blockDao.findById(blockId);
            if (block != null) {
                if (IntegerUtils.add(block.getRealCounts(), -1) < 0) {
                    block.setRealCounts(0);
                } else {
                    block.setRealCounts(IntegerUtils.add(block.getRealCounts(), -1));
                }
            }
            // 删除单元
            unitDao.delete(unit);

        }
    }

    @Override
    public Integer generate(String blockId) {
        Assert.hasText(blockId, "操作失败!楼栋ID不能为空!");
        Block block = blockDao.findById(blockId);
        Assert.notNull(block, "操作失败!楼栋不存在!请刷新后重试!");

        Integer levels = block.getLevels();
        Assert.isTrue(levels != null && levels > 0, "操作失败!楼栋的楼高未正确设置，无法创建房屋!");

        UnitBo bo = new UnitBo();
        bo.setBlockId(blockId);
        List<Unit> units = unitDao.query(bo);
        Assert.notEmpty(units, "操作失败!该楼栋并未设置单元信息，请设置单元信息后重试!");
        RoomService roomService = SystemContainer.getInstance().getBean(RoomService.class);
        int total = 0;
        String roomKey = null;
        for (int i = 1; i < levels + 1; i++) {
            for (Unit unit : units) {
                // 如果门牌号为空，则跳过！
                String doorCode = unit.getDoorCode();
                if (StringUtils.isEmpty(doorCode)) {
                    continue;
                }
                // 查找已经生成的房屋,如果找到则跳过
                RoomBo rb = new RoomBo();
                rb.setUnitId(unit.getId());
                rb.setFloor(i);
                Long t = roomDao.getTotal(rb);
                if (t != null && t > 0) {
                    continue;
                }
                // 如果没有找到类似的，则执行保存操作
                Room room = new Room();
                room.setStatus(Room.STATUS_INACTIVE);
                room.setRoomKey(roomKey);
                room.setBlockId(blockId);
                room.setBuildingId(block.getBuildingId());
                room.setFloor(i);           // 楼层
                room.setCode(i + doorCode);
                room.setSquare(unit.getSquare());           // 默认面积
                room.setOrient(unit.getOrient());           // 默认朝向
                room.setUnitId(unit.getId());               // 单元信息
                String types[] = unit.getType().split("-"); // 户型
                room.setType1(types[0]);
                room.setType2(types[1]);
                room.setType3(types[2]);
                room.setType4(types[3]);
                roomService.save(room);
                roomKey = room.getRoomKey();
                total++;
            }
        }
        return total;
    }

    @Override
    public List<Unit> query(UnitBo bo) {
        return unitDao.query(bo);
    }

    @Override
    public void doCallback(Unit unit, UnitVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        // 朝向
        vo.setOrientName(container.getBusinessName(HouseParams.ORIENT, unit.getOrient()));
    }
}
