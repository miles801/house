package com.michael.spec.dao;

import com.michael.spec.bo.RoomNewsBo;
import com.michael.spec.domain.RoomNews;

import java.util.List;

/**
 * @author Michael
 */
public interface RoomNewsDao {

    String save(RoomNews roomNews);

    void update(RoomNews roomNews);

    /**
     * 高级查询接口
     */
    List<RoomNews> query(RoomNewsBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(RoomNewsBo bo);

    RoomNews findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(RoomNews roomNews);
}
