package com.michael.spec.dao;

import com.michael.spec.bo.RoomRentBo;
import com.michael.spec.domain.RoomRent;

import java.util.List;

/**
 * @author Michael
 */
public interface RoomRentDao {

    String save(RoomRent roomRent);

    void update(RoomRent roomRent);

    /**
     * 高级查询接口
     */
    List<RoomRent> query(RoomRentBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(RoomRentBo bo);

    RoomRent findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(RoomRent roomRent);

    /**
     * 查询指定房屋当前的租赁信息
     *
     * @param roomId 房屋ID
     * @return 租赁信息
     */
    RoomRent findCurrent(String roomId);

    /**
     * 删除指定房屋的所有租赁信息
     *
     * @param roomId 房屋ID
     */
    void deleteByRoom(String roomId);

    /**
     * 判断一个客户是否是租户
     *
     * @param customerId 客户id
     * @return true是租户
     */
    boolean isRent(String customerId);
}
