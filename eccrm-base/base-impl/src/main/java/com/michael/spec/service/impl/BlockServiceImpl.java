package com.michael.spec.service.impl;

import com.michael.spec.bo.BlockBo;
import com.michael.spec.bo.UnitBo;
import com.michael.spec.dao.BlockDao;
import com.michael.spec.dao.UnitDao;
import com.michael.spec.domain.Block;
import com.michael.spec.service.BlockService;
import com.michael.spec.vo.BlockVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("blockService")
public class BlockServiceImpl implements BlockService, BeanWrapCallback<Block, BlockVo> {
    @Resource
    private BlockDao blockDao;

    @Resource
    private UnitDao unitDao;

    @Override
    public String save(Block block) {
        ValidatorUtils.validate(block);
        String id = blockDao.save(block);
        return id;
    }

    @Override
    public void update(Block block) {
        // 判断是否具有单元信息
        UnitBo bo = new UnitBo();
        bo.setBlockId(block.getId());
        Long total = unitDao.getTotal(bo);
        Assert.isTrue(total == null || total == 0, "更新失败!该楼栋下已经存在单元信息，无法进行更新操作!");
        ValidatorUtils.validate(block);
        blockDao.update(block);
    }

    @Override
    public PageVo pageQuery(BlockBo bo) {
        PageVo vo = new PageVo();
        Long total = blockDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Block> blockList = blockDao.query(bo);
        List<BlockVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(blockList, BlockVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public BlockVo findById(String id) {
        Block block = blockDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(block, BlockVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            // 判断是否具有单元信息
            UnitBo bo = new UnitBo();
            bo.setBlockId(id);
            Long total = unitDao.getTotal(bo);
            Assert.isTrue(total == null || total == 0, "删除失败!该楼栋下已经存在单元信息，无法删除!");
            blockDao.deleteById(id);
        }
    }

    @Override
    public List<Block> query(BlockBo bo) {
        return blockDao.query(bo);
    }

    @Override
    public void doCallback(Block block, BlockVo vo) {
    }
}
