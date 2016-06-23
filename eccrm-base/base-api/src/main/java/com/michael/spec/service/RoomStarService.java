package com.michael.spec.service;

import com.michael.spec.bo.RoomBo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface RoomStarService {


    /**
     * 添加关注，会排除重复的
     *
     * @param roomId 房屋ID
     * @param empId  员工ID
     */
    String star(String roomId, String empId);

    /**
     * 取消关注
     *
     * @param roomId 房屋ID
     * @param empId  员工ID
     */
    void cancel(String roomId, String empId);

    /**
     * 分页查询我的关注
     */
    PageVo myStarRoom(RoomBo bo);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 判断指定的房屋是否已经关注
     *
     * @param roomId 房屋ID
     * @param empId  员工ID
     * @return true 已经关注
     */
    boolean isStar(String roomId, String empId);
}
