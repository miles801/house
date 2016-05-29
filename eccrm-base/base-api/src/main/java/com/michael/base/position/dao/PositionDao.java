package com.michael.base.position.dao;

import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.domain.Position;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionDao {

    String save(Position position);

    void update(Position position);

    /**
     * 高级查询接口
     */
    List<Position> query(PositionBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(PositionBo bo);

    Position findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Position position);
}
