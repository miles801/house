package com.michael.spec.dao;

import com.michael.spec.bo.RoomBo;
import com.michael.spec.domain.Room;
import com.michael.spec.domain.RoomView;

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
    List<RoomView> query(RoomBo bo);

    /**
     * 查询房屋担心
     */
    List<Room> queryRoom(RoomBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(RoomBo bo);

    RoomView findById(String id);

    Room findRoomById(String id);

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

    /**
     * 查找相同房屋
     *
     * @param unitId 单元ID
     * @param code   房号
     * @param floor  楼层
     */
    Room findSame(String unitId, String code, int floor);

    /**
     * 查询指定楼盘中你最大的房屋编号
     *
     * @param buildingId 楼盘ID
     * @return 编号
     */
    String maxKey(String buildingId);

    /**
     * 批量设置房屋状态
     *
     * @param ids    房屋ID集合
     * @param status 状态值
     */
    void batchSetStatus(String[] ids, String status);

    /**
     * 查询指定客户名下的所有房产的编号
     *
     * @param customerId 客户ID
     * @return 房屋标号
     */
    List<String> findCodeByCustomer(String customerId);

    /**
     * 根据客户ID查找对应的房屋信息
     *
     * @param customerId 客户ID
     * @return 房屋列表
     */
    List<Room> findByCustomer(String customerId);

    /**
     * 根据房屋编号查询房屋信息
     *
     * @param code 房屋编号
     * @return 房屋
     */
    Room findByCode(String code);
}
