package com.michael.spec.service.impl;

import com.michael.spec.bo.BuildingBo;
import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.Building;
import com.michael.spec.service.BuildingService;
import com.michael.spec.service.HouseParams;
import com.michael.spec.vo.BuildingVo;
import com.ycrl.core.SystemContainer;
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

    @Override
    public String save(Building building) {
        if (StringUtils.isEmpty(building.getMasterId())) {
            building.setMasterId(SecurityContext.getEmpId());
            building.setMasterName(SecurityContext.getEmpName());
        }
        validate(building);
        String id = buildingDao.save(building);
        return id;
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
            buildingDao.deleteById(id);
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

        RoomDao roomDao = SystemContainer.getInstance().getBean(RoomDao.class);
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
