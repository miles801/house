package com.michael.spec.service;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.Customer;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.RoomView;
import com.michael.spec.vo.BuildingVo;
import com.michael.spec.vo.CustomerVo;
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
     * 设置房屋相关信息
     */
    void setRoomInfo(Room room);

    /**
     * 添加客户
     *
     * @param id       房屋ID
     * @param customer 客户信息
     */
    void addCustomer(String id, Customer customer);

    /**
     * 分页查询,返回RoomView的集合
     */
    PageVo pageQuery(RoomBo bo);

    /**
     * 根据ID查询对象的信息
     */
    RoomView findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    List<RoomView> query(RoomBo bo);

    /**
     * 获取房屋所属的客户
     *
     * @param roomId 房屋ID
     * @return 客户信息
     */
    CustomerVo getCustomer(String roomId);

    /**
     * 获取房屋所属的小区信息
     *
     * @param roomId 房屋ID
     * @return 小区信息
     */
    BuildingVo getBuilding(String roomId);
}
