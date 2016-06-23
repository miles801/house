package com.michael.spec.dao;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.RoomStar;
import com.michael.spec.domain.RoomView;

import java.util.List;

/**
 * @author Michael
 */
public interface RoomStarDao {

    String save(RoomStar roomStar);

    void update(RoomStar roomStar);

    /**
     * 高级查询接口
     */
    List<RoomView> query(RoomBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(RoomBo bo);

    RoomStar findById(String id);

    void deleteById(String id);

    RoomStar find(String roomId, String empId);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(RoomStar roomStar);
}
