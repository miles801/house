package com.michael.spec.service.impl;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.dao.RoomDao;
import com.michael.spec.domain.Room;
import com.michael.spec.service.RoomService;
import com.michael.spec.vo.RoomVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("roomService")
public class RoomServiceImpl implements RoomService, BeanWrapCallback<Room, RoomVo> {
    @Resource
    private RoomDao roomDao;

    @Override
    public String save(Room room) {
        ValidatorUtils.validate(room);
        String id = roomDao.save(room);
        return id;
    }

    @Override
    public void update(Room room) {
        ValidatorUtils.validate(room);
        roomDao.update(room);
    }

    @Override
    public PageVo pageQuery(RoomBo bo) {
        PageVo vo = new PageVo();
        Long total = roomDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Room> roomList = roomDao.query(bo);
        List<RoomVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(roomList, RoomVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public RoomVo findById(String id) {
        Room room = roomDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(room, RoomVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            roomDao.deleteById(id);
        }
    }

    @Override
    public List<Room> query(RoomBo bo) {
        return roomDao.query(bo);
    }

    @Override
    public void doCallback(Room room, RoomVo vo) {
    }
}
