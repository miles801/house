package com.michael.spec.service;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.*;
import com.michael.spec.vo.BuildingVo;
import com.michael.spec.vo.CustomerVo;
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
     * 添加客户
     *
     * @param id           房屋ID
     * @param customer     客户信息
     * @param roomBusiness 成交记录
     */
    void addCustomer(String id, Customer customer, RoomBusiness roomBusiness);

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

    /**
     * 批量新增
     *
     * @param ids ID列表
     */
    void batchAdd(String[] ids);

    /**
     * 批量修改
     *
     * @param ids ID列表
     */
    void batchModify(String[] ids);

    /**
     * 申请为无效
     *
     * @param ids ID列表
     */
    void applyInvalid(String[] ids);

    /**
     * 批量通过房屋（即将房屋的状态设置为“正常”）
     *
     * @param ids 房屋ID列表
     */
    void batchPass(String[] ids);

    /**
     * 批量不通过（即将房屋的状态设置为“未录入”）
     *
     * @param ids 房屋ID列表
     */
    void batchDeny(String[] ids);

    /**
     * 数据导入
     *
     * @param attachmentIds 附件ID列表
     */
    void importData(String[] attachmentIds);

    /**
     * 根据房屋编号查询房屋信息
     *
     * @param code 房屋编号
     * @return 房屋
     */
    RoomVo findByCode(String code);

    /**
     * 添加租户
     *
     * @param roomId   房屋ID
     * @param customer 租户
     * @param roomRent 租赁信息
     */
    void addRent(String roomId, Customer customer, RoomRent roomRent);

    /**
     * 变更租户
     *
     * @param roomId   房屋ID
     * @param customer 新租户
     * @param roomRent 新租赁信息
     */
    void changeRent(String roomId, Customer customer, RoomRent roomRent);

    /**
     * 修改租户
     *
     * @param roomId   房屋ID
     * @param customer 租户
     * @param roomRent 租赁信息
     */
    void updateRent(String roomId, Customer customer, RoomRent roomRent);

    /**
     * 查询指定房屋当前的租赁信息
     *
     * @param roomId 房屋ID
     * @return 租赁信息
     */
    RoomRent findCurrent(String roomId);

    /**
     * 删除指定的租赁信息（实际是完成）
     *
     * @param rentId 租赁ID
     */
    void deleteRent(String rentId);
}
