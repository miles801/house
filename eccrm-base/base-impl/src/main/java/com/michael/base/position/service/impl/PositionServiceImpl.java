package com.michael.base.position.service.impl;

import com.michael.base.position.bo.PositionBo;
import com.michael.base.position.dao.PositionDao;
import com.michael.base.position.dao.PositionEmpDao;
import com.michael.base.position.dao.PositionRelationDao;
import com.michael.base.position.domain.Position;
import com.michael.base.position.domain.PositionRelation;
import com.michael.base.position.service.PositionService;
import com.michael.base.position.vo.PositionVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.number.IntegerUtils;
import com.ycrl.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("positionService")
public class PositionServiceImpl implements PositionService, BeanWrapCallback<Position, PositionVo> {
    @Resource
    private PositionDao positionDao;

    @Resource
    private PositionEmpDao positionEmpDao;

    @Resource
    private PositionRelationDao positionRelationDao;

    @Override
    public String save(Position position) {
        position.setDeleted(false);
        ValidatorUtils.validate(position);
        String id = positionDao.save(position);
        // 给所有的父节点添加孩子节点
        String parentId = position.getParentId();
        if (StringUtils.isNotEmpty(parentId)) {
            // 保存直接上级的关系
            Position parent = positionDao.findById(parentId);
            Assert.notNull(parent, "保存失败!上级岗位不存在!请刷新后重试!");
            parent.setParent(true);
            PositionRelation relation = new PositionRelation();
            relation.setPositionId(parentId);
            relation.setChildId(id);
            positionRelationDao.save(relation);

            // 通过直接上级保存关联上级的关系
            List<String> parentIds = positionRelationDao.findParents(position.getParentId());
            for (String pid : parentIds) {
                PositionRelation pr = new PositionRelation();
                pr.setChildId(id);
                pr.setPositionId(pid);
                positionRelationDao.save(pr);
            }

        }

        return id;
    }

    @Override
    public void update(Position position) {
        ValidatorUtils.validate(position);
        positionDao.update(position);
    }

    @Override
    public PageVo pageQuery(PositionBo bo) {
        PageVo vo = new PageVo();
        Long total = positionDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Position> positionList = positionDao.query(bo);
        List<PositionVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(positionList, PositionVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public PositionVo findById(String id) {
        Position position = positionDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(position, PositionVo.class);
    }

    @Override
    public void disable(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Position position = positionDao.findById(id);
            Assert.notNull(position, "禁用失败!数据不存在，请刷新后重试!");
            position.setDeleted(true);

            // 禁用所有下级
            List<String> childrenPositionIds = positionRelationDao.findChildren(id);
            for (String childId : childrenPositionIds) {
                Position child = positionDao.findById(childId);
                child.setDeleted(true);
            }
        }
    }

    @Override
    public void enable(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Position position = positionDao.findById(id);
            Assert.notNull(position, "启用失败!数据不存在，请刷新后重试!");
            position.setDeleted(false);

            // 启用所有上级
            List<String> childrenPositionIds = positionRelationDao.findParents(id);
            for (String childId : childrenPositionIds) {
                Position child = positionDao.findById(childId);
                child.setDeleted(false);
            }
        }
    }

    @Override
    public List<PositionVo> validTree() {
        PositionBo bo = new PositionBo();
        bo.setDeleted(false);
        List<Position> data = positionDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name", "code", "parentId", "parentName", "empCounts"})
                .wrapList(data, PositionVo.class);
    }

    @Override
    public List<PositionVo> tree() {
        List<Position> data = positionDao.query(null);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name", "code", "parentId", "parentName", "deleted"})
                .wrapList(data, PositionVo.class);
    }

    @Override
    public Integer addEmp(String positionId, int counts) {
        Assert.hasText(positionId, "添加员工失败!岗位ID不能为空!");
        Position position = positionDao.findById(positionId);
        Assert.notNull(position, "添加员工失败!组织机构不存在，请刷新后重试!");
        Assert.isTrue(IntegerUtils.add(position.getEmpCounts(), counts) <= IntegerUtils.add(position.getMaxEmp(), 0), "添加员工失败!超出该组织机构允许的最大员工数量!");
        position.setEmpCounts(IntegerUtils.add(position.getEmpCounts(), counts));
        return position.getEmpCounts();
    }

    @Override
    public void doCallback(Position position, PositionVo vo) {
    }
}
