package com.michael.spec.service;

import com.michael.spec.bo.RoomRentBo;
import com.michael.spec.domain.RoomRent;
import com.michael.spec.vo.RoomRentVo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface RoomRentService {

    /**
     * 保存
     * 同时更新房屋的状态为“在租”
     */
    String save(RoomRent roomRent);

    /**
     * 更新
     */
    void update(RoomRent roomRent);

    /**
     * 分页查询
     */
    PageVo pageQuery(RoomRentBo bo);

    /**
     * 根据ID查询对象的信息
     */
    RoomRentVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 完成交易
     * 设置完成状态，并同时更新房屋对应的状态
     *
     * @param roomId 房屋ID
     */
    void finish(String roomId);
}
