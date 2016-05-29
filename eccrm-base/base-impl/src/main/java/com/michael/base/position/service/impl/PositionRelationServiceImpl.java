package com.michael.base.position.service.impl;

import com.michael.base.position.bo.PositionRelationBo;
import com.michael.base.position.dao.PositionRelationDao;
import com.michael.base.position.domain.PositionRelation;
import com.michael.base.position.service.PositionRelationService;
import com.michael.base.position.vo.PositionRelationVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("positionRelationService")
public class PositionRelationServiceImpl implements PositionRelationService, BeanWrapCallback<PositionRelation, PositionRelationVo> {
    @Resource
    private PositionRelationDao positionRelationDao;

    @Override
    public String save(PositionRelation positionRelation) {
        ValidatorUtils.validate(positionRelation);
        String id = positionRelationDao.save(positionRelation);
        return id;
    }

    @Override
    public void update(PositionRelation positionRelation) {
        ValidatorUtils.validate(positionRelation);
        positionRelationDao.update(positionRelation);
    }

    @Override
    public PageVo pageQuery(PositionRelationBo bo) {
        PageVo vo = new PageVo();
        Long total = positionRelationDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<PositionRelation> positionRelationList = positionRelationDao.query(bo);
        List<PositionRelationVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(positionRelationList, PositionRelationVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public PositionRelationVo findById(String id) {
        PositionRelation positionRelation = positionRelationDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(positionRelation, PositionRelationVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            positionRelationDao.deleteById(id);
        }
    }

    @Override
    public void doCallback(PositionRelation positionRelation, PositionRelationVo vo) {
    }
}
