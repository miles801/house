package com.michael.spec.service.impl;

import com.michael.spec.bo.UnitBo;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.dao.UnitDao;
import com.michael.spec.domain.Unit;
import com.michael.spec.service.HouseParams;
import com.michael.spec.service.UnitService;
import com.michael.spec.vo.UnitVo;
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
@Service("unitService")
public class UnitServiceImpl implements UnitService, BeanWrapCallback<Unit, UnitVo> {
    @Resource
    private UnitDao unitDao;

    @Resource
    private RoomDao roomDao;

    @Override
    public String save(Unit unit) {
        ValidatorUtils.validate(unit);
        String id = unitDao.save(unit);
        return id;
    }

    @Override
    public void update(Unit unit) {
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
            unitDao.deleteById(id);
        }
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