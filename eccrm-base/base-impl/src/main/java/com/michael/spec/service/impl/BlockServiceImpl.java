package com.michael.spec.service.impl;

import com.michael.spec.bo.BlockBo;
import com.michael.spec.bo.UnitBo;
import com.michael.spec.dao.BlockDao;
import com.michael.spec.dao.BuildingDao;
import com.michael.spec.dao.UnitDao;
import com.michael.spec.domain.Block;
import com.michael.spec.domain.Building;
import com.michael.spec.domain.Unit;
import com.michael.spec.service.BlockService;
import com.michael.spec.service.UnitService;
import com.michael.spec.vo.BlockVo;
import com.ycrl.core.SystemContainer;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.number.IntegerUtils;
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
    private BuildingDao buildingDao;

    @Resource
    private BlockDao blockDao;

    @Resource
    private UnitDao unitDao;

    @Override
    public String save(Block block) {
        ValidatorUtils.validate(block);
        // 更新楼盘的实际楼栋数量
        String buildingId = block.getBuildingId();
        Building building = buildingDao.findById(buildingId);
        building.setRealCounts(IntegerUtils.add(building.getRealCounts(), 1));

        // 保存
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
    public void createUnit(String id) {
        Assert.hasText(id, "操作失败!ID不能为空!");
        Block block = blockDao.findById(id);
        Assert.notNull(block, "操作失败!楼栋不存在，请刷新后重试!");
        // 查看该楼栋下是否已经具有单元信息
        UnitBo bo = new UnitBo();
        bo.setBlockId(id);
        Long total = unitDao.getTotal(bo);
        Assert.isTrue(total == null || total == 0, "操作失败!该楼栋下已经具有单元信息，无法再次生成!若需生成请先清理原有数据!");
        Integer unitCounts = block.getUnitCounts();
        UnitService unitService = SystemContainer.getInstance().getBean(UnitService.class);
        for (int i = 0; i < unitCounts; i++) {
            Unit unit = new Unit();
            unit.setBlockId(id);
            unit.setCode((i + 1) + "");
            unitService.save(unit);
        }
    }

    @Override
    public void clearUnit(String id) {
        Assert.hasText(id, "操作失败!ID不能为空!");
        Block block = blockDao.findById(id);
        Assert.notNull(block, "操作失败!楼栋不存在，请刷新后重试!");

        // 删除单元
        UnitBo bo = new UnitBo();
        bo.setBlockId(id);
        List<Unit> units = unitDao.query(bo);
        if (units != null && !units.isEmpty()) {
            UnitService unitService = SystemContainer.getInstance().getBean(UnitService.class);
            for (Unit unit : units) {
                unitService.deleteByIds(new String[]{unit.getId()});
            }
        }
        // 更新实际单元数量
        block.setRealCounts(0);
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
            Block block = blockDao.findById(id);
            if (block == null) {
                continue;
            }
            blockDao.delete(block);

            // 更新楼盘的实际楼栋数量
            Building building = buildingDao.findById(block.getBuildingId());
            Assert.notNull(building, "数据异常!楼栋关联的楼盘不存在!");
            building.setRealCounts(IntegerUtils.add(building.getRealCounts(), -1));

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
