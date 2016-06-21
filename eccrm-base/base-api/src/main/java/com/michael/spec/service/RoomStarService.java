package com.michael.spec.service;

import com.michael.spec.bo.RoomStarBo;
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
     * 分页查询
     */
    PageVo pageQuery(RoomStarBo bo);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

}
