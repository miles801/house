package com.michael.spec.dao;

import com.michael.spec.bo.UnitBo;
import com.michael.spec.domain.Unit;

import java.util.List;

/**
 * @author Michael
 */
public interface UnitDao {

    String save(Unit unit);

    void update(Unit unit);

    /**
     * 高级查询接口
     */
    List<Unit> query(UnitBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(UnitBo bo);

    Unit findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Unit unit);
}
