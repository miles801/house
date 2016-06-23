package com.michael.spec.dao;

import com.michael.spec.bo.RoomBusinessBo;
import com.michael.spec.domain.RoomBusiness;

import java.util.List;

/**
 * @author Michael
 */
public interface RoomBusinessDao {

    String save(RoomBusiness roomBusiness);

    void update(RoomBusiness roomBusiness);

    /**
     * 高级查询接口
     */
    List<RoomBusiness> query(RoomBusinessBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(RoomBusinessBo bo);

    RoomBusiness findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(RoomBusiness roomBusiness);
}
