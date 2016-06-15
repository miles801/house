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

    List<Unit> query(UnitBo bo);
}
