package com.michael.spec.service;

import com.michael.spec.bo.UnitBo;
import com.michael.spec.domain.Unit;
import com.michael.spec.vo.UnitVo;
import com.ycrl.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface UnitService {

    /**
     * 保存
     */
    String save(Unit unit);

    /**
     * 更新
     */
    void update(Unit unit);

    /**
     * 分页查询
     */
    PageVo pageQuery(UnitBo bo);

    /**
     * 根据ID查询对象的信息
     */
    UnitVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 强制删除！
     * 删除单元的同时，会删除该单元下的所有房屋，并同时跟新楼栋的单元数数量
     *
     * @param ids 单元ID
     */
    void forceDelete(String[] ids);

    List<Unit> query(UnitBo bo);

    /**
     * 根据楼栋自动创建房屋信息
     *
     * @param blockId 楼栋ID
     * @return 创建的总房屋数量
     */
    Integer generate(String blockId);
}
