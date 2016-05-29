package com.michael.base.position.dao;

import com.michael.base.position.bo.PositionRelationBo;
import com.michael.base.position.domain.PositionRelation;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionRelationDao {

    String save(PositionRelation positionRelation);

    void update(PositionRelation positionRelation);

    /**
     * 高级查询接口
     */
    List<PositionRelation> query(PositionRelationBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(PositionRelationBo bo);

    PositionRelation findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(PositionRelation positionRelation);

    /**
     * 查询所有父级ID
     *
     * @param childId 子节点ID
     */
    List<String> findParents(String childId);

    /**
     * 查询所有的孩子节点ID
     *
     * @param positionId 节点ID
     */
    List<String> findChildren(String positionId);

    /**
     * 根据孩子节点删除关联关系
     *
     * @param childId 孩子节点
     */
    void deleteByChild(String childId);
}
