package com.michael.base.position.service;

import com.michael.base.position.bo.PositionRelationBo;
import com.michael.base.position.domain.PositionRelation;
import com.michael.base.position.vo.PositionRelationVo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface PositionRelationService {

    /**
     * 保存
     */
    String save(PositionRelation positionRelation);

    /**
     * 更新
     */
    void update(PositionRelation positionRelation);

    /**
     * 分页查询
     */
    PageVo pageQuery(PositionRelationBo bo);

    /**
     * 根据ID查询对象的信息
     */
    PositionRelationVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

}
