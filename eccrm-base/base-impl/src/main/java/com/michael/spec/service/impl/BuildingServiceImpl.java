package com.michael.spec.service.impl;

import com.michael.spec.bo.BuildingBo;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.domain.Building;
import com.michael.spec.service.BuildingService;
import com.michael.spec.service.HouseParams;
import com.michael.spec.vo.BuildingVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import eccrm.base.parameter.service.ParameterContainer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        ValidatorUtils.validate(building);
        String id = buildingDao.save(building);
        return id;
    }

    @Override
    public void update(Building building) {
        ValidatorUtils.validate(building);
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
    }
}
