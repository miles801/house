package com.michael.spec.service.impl;

import com.michael.base.common.BaseParameter;
import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.spec.bo.BlockBo;
import com.michael.spec.bo.BuildingBo;
import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.BlockDao;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.Block;
import com.michael.spec.domain.Building;
import com.michael.spec.service.BuildingService;
import com.michael.spec.service.HouseParams;
import com.michael.spec.vo.BuildingVo;
import com.ycrl.base.common.CommonStatus;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.string.StringUtils;
import eccrm.base.parameter.service.ParameterContainer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael
 */
@Service("buildingService")
public class BuildingServiceImpl implements BuildingService, BeanWrapCallback<Building, BuildingVo> {
    @Resource
    private BuildingDao buildingDao;

    @Resource
    private BlockDao blockDao;

    @Resource
    private RoomDao roomDao;

    @Resource
    private EmpDao empDao;

    @Override
    public String save(Building building) {
        if (StringUtils.isEmpty(building.getMasterId())) {
            building.setMasterId(SecurityContext.getEmpId());
            building.setMasterName(SecurityContext.getEmpName());
        }
        validate(building);
        // 如果状态为空，则默认为未激活状态
        if (StringUtils.isEmpty(building.getStatus())) {
            building.setStatus(CommonStatus.INACTIVE.getValue());
        }
        String id = buildingDao.save(building);
        return id;
    }

    @Override
    public String commit(Building building) {
        building.setStatus(CommonStatus.ACTIVE.getValue());
        String id = building.getId();
        if (StringUtils.isNotEmpty(id)) {
            // 产生楼栋
            id = save(building);
        } else {
            update(building);
        }

        // 自动创建楼栋
        Integer counts = building.getBuildingCounts();
        if (counts != null && counts > 0) {
            for (int i = 0; i < counts; i++) {
                Block block = new Block();
                block.setBuildingId(id);
                block.setCode((i + 1) + "");
                blockDao.save(block);
            }
        }
        return id;
    }

    @Override
    public void enable(String id) {

    }

    private void validate(Building building) {
        ValidatorUtils.validate(building);
        boolean exists = buildingDao.hasName(building.getName(), building.getId());
        Assert.isTrue(!exists, "操作失败!名称重复!");
    }

    @Override
    public void update(Building building) {
        validate(building);
        buildingDao.update(building);
    }

    @Override
    public PageVo pageQuery(BuildingBo bo) {
        PageVo vo = new PageVo();
        Long total = buildingDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Building> buildingList = buildingDao.query(bo);
        List<BuildingVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(buildingList, BuildingVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public BuildingVo findById(String id) {
        Building building = buildingDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrap(building, BuildingVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            //
            Building building = buildingDao.findById(id);
            if (building == null) {
                continue;
            }
            // 校验关联
            BlockBo blockBo = new BlockBo();
            blockBo.setBuildingId(id);
            Long blockTotal = blockDao.getTotal(blockBo);
            Assert.isTrue(blockTotal == null || blockTotal == 0, "楼盘删除失败!楼盘下已经具有楼栋信息!");

            RoomBo bo = new RoomBo();
            bo.setBuildingId(id);
            Long total = roomDao.getTotal(bo);
            Assert.isTrue(total == null || total == 0, "楼盘删除失败!楼盘下已经具有房屋信息!");
            buildingDao.deleteById(id);
        }
    }

    @Override
    public void addMaintain(String id, String... empIds) {
        Assert.hasText(id, "操作失败!楼盘ID不能为空!");
        Assert.notEmpty(empIds, "添加维护人失败!维护人ID不能为空!");
        Building building = buildingDao.findById(id);
        Assert.notNull(building, "操作失败!楼盘不存在！请刷新后重试!");
        for (String empId : empIds) {
            String maintainId = building.getMaintainId();
            if (StringUtils.isEmpty(maintainId)) {
                Emp emp = empDao.findById(empId);
                Assert.notNull(emp, "添加维护人失败!员工不存在!");
                building.setMaintainId(empId + ";");
                building.setMaintainName(emp.getName() + ";");
            } else if (!maintainId.contains(id + ";")) {
                Emp emp = empDao.findById(empId);
                Assert.notNull(emp, "添加维护人失败!员工不存在!");
                building.setMaintainId(maintainId + empId + ";");
                building.setMaintainName(building.getMaintainName() + emp.getName() + ";");
            }
        }
    }

    @Override
    public void removeMaintain(String id, String empId) {
        Assert.hasText(id, "操作失败!楼盘ID不能为空!");
        Assert.hasText(empId, "删除维护人失败!维护人ID不能为空!");
        Building building = buildingDao.findById(id);
        Assert.notNull(building, "操作失败!楼盘不存在！请刷新后重试!");
        String maintainId = building.getMaintainId();
        if (StringUtils.isNotEmpty(maintainId)) {
            String[] idArr = maintainId.split(";");
            String[] nameArr = building.getMaintainName().split(";");
            StringBuilder idBuilder = new StringBuilder();
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 0; i < idArr.length; i++) {
                if (idArr[i].equals(empId)) {
                    continue;
                }
                idBuilder.append(idArr[i] + ";");
                nameBuilder.append(nameArr[i] + ";");
            }
            String newMaintainId = idBuilder.toString();
            String newMaintainName = nameBuilder.toString();
            building.setMaintainName(newMaintainName);
            building.setMaintainId(newMaintainId);
        }
    }

    @Override
    public void updateMaster(String id, String empId) {
        Assert.hasText(id, "操作失败!楼盘ID不能为空!");
        Building building = buildingDao.findById(id);
        Assert.notNull(building, "操作失败!楼盘不存在！请刷新后重试!");
        if (StringUtils.isEmpty(empId)) {
            building.setMasterId(null);
            building.setMaintainName(null);
        } else {
            Emp emp = empDao.findById(empId);
            Assert.notNull(emp, "更新楼盘负责人失败!员工不存在!");
            building.setMasterId(empId);
            building.setMasterName(emp.getName());
        }
    }

    @Override
    public void doCallback(Building building, BuildingVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        // 建筑类型
        vo.setTypeName(container.getBusinessName(HouseParams.BUILDING_TYPE, building.getType()));
        // 房屋权属
        vo.setHouseTypeName(container.getBusinessName(HouseParams.HOUSE_TYPE, building.getHouseType()));
        // 房屋用途
        vo.setUsagesName(container.getBusinessName(HouseParams.USAGE, building.getUsages()));
        // 供暖方式
        vo.setWarmTypeName(container.getBusinessName(HouseParams.WARM_TYPE, building.getWarmType()));
        // 距离地铁
        vo.setSubwayName(container.getBusinessName(HouseParams.SUBWAY, building.getSubway()));

        // 状态
        vo.setStatusName(container.getSystemName(BaseParameter.STATUS, building.getStatus()));
        // 录入户数
        RoomBo bo = new RoomBo();
        bo.setBuildingId(building.getId());
        vo.setAllRooms(roomDao.getTotal(bo));
        // 有效户数
        List<String> status = new ArrayList<String>();
        Collections.addAll(status, "ACTIVE", "APPLY_MODIFY", "APPLY_INVALID");
        bo.setStatusInclude(status);
        vo.setValidRooms(roomDao.getTotal(bo));
    }
}
