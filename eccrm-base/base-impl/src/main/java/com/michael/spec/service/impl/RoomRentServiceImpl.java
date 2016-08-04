package com.michael.spec.service.impl;

import com.michael.spec.bo.RoomRentBo;
import com.michael.spec.dao.RoomRentDao;
import com.michael.spec.domain.RoomRent;
import com.michael.spec.service.RoomRentService;
import com.michael.spec.vo.RoomRentVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("roomRentService")
public class RoomRentServiceImpl implements RoomRentService, BeanWrapCallback<RoomRent, RoomRentVo> {
    @Resource
    private RoomRentDao roomRentDao;

    @Override
    public String save(RoomRent roomRent) {
        roomRent.setFinish(false);
        ValidatorUtils.validate(roomRent);
        String id = roomRentDao.save(roomRent);
        return id;
    }

    @Override
    public void update(RoomRent roomRent) {
        roomRent.setFinish(false);
        ValidatorUtils.validate(roomRent);
        roomRentDao.update(roomRent);
    }

    @Override
    public PageVo pageQuery(RoomRentBo bo) {
        PageVo vo = new PageVo();
        Long total = roomRentDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<RoomRent> roomRentList = roomRentDao.query(bo);
        List<RoomRentVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(roomRentList, RoomRentVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public RoomRentVo findById(String id) {
        RoomRent roomRent = roomRentDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(roomRent, RoomRentVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        Assert.isTrue(false, "该方法不允许调用!");
    }

    @Override
    public void finish(String roomId) {
        Assert.hasText(roomId, "操作失败!没有获取到房屋ID!");
        RoomRentBo bo = new RoomRentBo();
        bo.setRoomId(roomId);
        bo.setFinish(false);
        List<RoomRent> roomRents = roomRentDao.query(bo);
        Assert.isTrue(!roomRents.isEmpty(), "操作失败!该房屋并没有在出租状态，请刷新后重试!");
        Assert.isTrue(roomRents.size() == 1, "数据错误!该房屋的“在租”记录超过一条，请与管理员联系!");
    }

    @Override
    public void doCallback(RoomRent roomRent, RoomRentVo vo) {
    }
}
