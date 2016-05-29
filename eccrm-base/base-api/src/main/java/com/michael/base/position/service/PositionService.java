package com.michael.base.position.service;

import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.domain.Position;
import com.michael.base.position.vo.PositionVo;
import com.ycrl.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionService {

    /**
     * 保存
     */
    String save(Position position);

    /**
     * 更新
     */
    void update(Position position);

    /**
     * 分页查询
     */
    PageVo pageQuery(PositionBo bo);

    /**
     * 根据ID查询对象的信息
     */
    PositionVo findById(String id);

    /**
     * 批量删除
     */
    void disable(String[] ids);

    /**
     * 启用
     */
    void enable(String[] ids);

    /**
     * 加载整颗树，只返回属性必要的几个字段
     */
    List<PositionVo> tree();

    /**
     * 加载整颗有效的树，只返回属性必要的几个字段
     */
    List<PositionVo> validTree();

    /**
     * 给指定的岗位添加指定数量的员工
     *
     * @param positionId 岗位ID
     * @param counts     员工数量
     * @return 最新的员工数量
     */
    Integer addEmp(String positionId, int counts);
}
