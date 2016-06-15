package com.michael.spec.service;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.Room;
import com.michael.spec.vo.RoomVo;
import com.ycrl.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface RoomService {

    /**
     * 保存
     */
    String save(Room room);

    /**
     * 更新
     */
    void update(Room room);

    /**
     * 分页查询
     */
    PageVo pageQuery(RoomBo bo);

    /**
     * 根据ID查询对象的信息
     */
    RoomVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    List<Room> query(RoomBo bo);
}
