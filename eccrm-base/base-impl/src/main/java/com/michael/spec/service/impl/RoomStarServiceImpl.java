package com.michael.spec.service.impl;

import com.michael.spec.bo.RoomStarBo;
import com.michael.spec.dao.RoomStarDao;
import com.michael.spec.domain.RoomStar;
import com.michael.spec.service.RoomStarService;
import com.michael.spec.vo.RoomStarVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.pager.PageVo;
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
    public PageVo pageQuery(RoomStarBo bo) {
        PageVo vo = new PageVo();
        Long total = roomStarDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<RoomStar> roomStarList = roomStarDao.query(bo);
        List<RoomStarVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(roomStarList, RoomStarVo.class);
        vo.setData(vos);
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
    public void doCallback(RoomStar roomStar, RoomStarVo vo) {
    }
}
