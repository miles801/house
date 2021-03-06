package com.michael.spec.service;

import com.michael.spec.bo.BlockBo;
import com.michael.spec.domain.Block;
import com.michael.spec.vo.BlockVo;
import com.ycrl.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface BlockService {

    /**
     * 保存
     */
    String save(Block block);

    /**
     * 更新
     */
    void update(Block block);

    /**
     * 分页查询
     */
    PageVo pageQuery(BlockBo bo);

    /**
     * 根据ID查询对象的信息
     */
    BlockVo findById(String id);

    /**
     * 产生单元信息
     */
    void createUnit(String id);

    /**
     * 移除单元信息
     */
    void clearUnit(String id);

    /**
     * 强制删除！
     * 删除楼栋的同时，会删除该楼栋下的所有房屋和单元
     *
     * @param ids 单元ID
     */
    void forceDelete(String[] ids);


    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    List<Block> query(BlockBo bo);
}
