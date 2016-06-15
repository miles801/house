package com.michael.spec.dao;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.Room;

import java.util.List;

/**
 * @author Michael
 */
public interface RoomDao {

    String save(Room room);

    void update(Room room);

    /**
     * 高级查询接口
     */
    List<Room> query(RoomBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(RoomBo bo);

    Room findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Room room);

    /**
     * 获取指定单元的房间数
     *
     * @param unitId 单元ID
     * @return 房间数量
     */
    long getUnitRoomCounts(String unitId);
}
