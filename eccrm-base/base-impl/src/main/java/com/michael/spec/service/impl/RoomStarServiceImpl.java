package com.michael.spec.service.impl;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.RoomStarDao;
import com.michael.spec.domain.RoomStar;
import com.michael.spec.domain.RoomView;
import com.michael.spec.service.HouseParams;
import com.michael.spec.service.RoomStarService;
import com.michael.spec.vo.RoomStarVo;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.pager.PageVo;
import eccrm.base.parameter.service.ParameterContainer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("roomStarService")
public class RoomStarServiceImpl implements RoomStarService, BeanWrapCallback<RoomStar, RoomStarVo> {
    @Resource
    private RoomStarDao roomStarDao;


    @Override
    public String star(String roomId, String empId) {
        Assert.hasText(roomId, "关注失败!房屋ID不能为空!");
        Assert.hasText(empId, "关注失败!关注人不能为空!");
        RoomStar roomStar = roomStarDao.find(roomId, empId);
        if (roomStar == null) {
            roomStar = new RoomStar();
            roomStar.setRoomId(roomId);
            roomStar.setEmpId(empId);
            return roomStarDao.save(roomStar);
        }
        return roomStar.getId();
    }

    @Override
    public void cancel(String roomId, String empId) {
        Assert.hasText(roomId, "取消关注失败!房屋ID不能为空!");
        Assert.hasText(empId, "取消关注失败!关注人不能为空!");
        RoomStar roomStar = roomStarDao.find(roomId, empId);
        if (roomStar != null) {
            roomStarDao.delete(roomStar);
        }
    }

    @Override
    public PageVo myStarRoom(RoomBo bo) {
        PageVo vo = new PageVo();
        Long total = roomStarDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<RoomView> data = roomStarDao.query(bo);
        for (RoomView roomView : data) {
            ParameterContainer container = ParameterContainer.getInstance();
            roomView.setStatusName(container.getSystemName(HouseParams.HOUSE_STATUS, roomView.getStatus()));
            roomView.setOrientName(container.getBusinessName(HouseParams.ORIENT, roomView.getOrient()));
            roomView.setHousePropertyName(container.getBusinessName(HouseParams.HOUSE_PROPERTY, roomView.getHouseProperty()));
            roomView.setHouseUseTypeName(container.getBusinessName(HouseParams.HOUSE_USE_TYPE, roomView.getHouseUseType()));
        }
        vo.setData(data);
        return vo;
    }


    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            roomStarDao.deleteById(id);
        }
    }

    @Override
    public boolean isStar(String roomId, String empId) {
        Assert.hasText(roomId, "查询失败!房屋ID不能为空!");
        Assert.hasText(empId, "查询失败!员工ID不能为空!");
        RoomStar roomStar = roomStarDao.find(roomId, empId);
        return roomStar != null;
    }

    @Override
    public void doCallback(RoomStar roomStar, RoomStarVo vo) {
    }
}
