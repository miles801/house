package com.michael.spec.dao;

import com.michael.spec.bo.BlockBo;
import com.michael.spec.domain.Block;

import java.util.List;

/**
 * @author Michael
 */
public interface BlockDao {

    String save(Block block);

    void update(Block block);

    /**
     * 高级查询接口
     */
    List<Block> query(BlockBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(BlockBo bo);

    Block findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Block block);
}
