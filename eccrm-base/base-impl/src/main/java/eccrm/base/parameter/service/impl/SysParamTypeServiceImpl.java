package eccrm.base.parameter.service.impl;

import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.pager.PageVo;
import eccrm.base.common.enums.CommonStatus;
import eccrm.base.constant.ParameterConstant;
import eccrm.base.parameter.bo.SysParamTypeBo;
import eccrm.base.parameter.dao.SysParamItemDao;
import eccrm.base.parameter.dao.SysParamTypeDao;
import eccrm.base.parameter.domain.SysParamType;
import eccrm.base.parameter.service.ParameterContainer;
import eccrm.base.parameter.service.SysParamTypeService;
import eccrm.base.parameter.vo.SysParamTypeVo;
import eccrm.core.VoHelper;
import eccrm.core.VoWrapper;
import eccrm.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: miles
 * @datetime: 2014-06-20
 */
@Service("sysParamTypeService")
public class SysParamTypeServiceImpl implements SysParamTypeService, VoWrapper<SysParamType, SysParamTypeVo> {
    @Resource
    private SysParamTypeDao dao;

    @Resource
    private SysParamItemDao sysParamItemDao;

    @Override
    public String save(SysParamType sysParamType) {
        sysParamType.setPath(null);
        String id = dao.save(sysParamType);
        return id;
    }

    @Override
    public void update(SysParamType sysParamType) {
        dao.update(sysParamType);

        // 更新缓存信息
        ParameterContainer.getInstance().reloadSystem(sysParamType.getCode());
    }

    @Override
    public PageVo query(SysParamTypeBo bo) {
        PageVo vo = new PageVo();
        Long total = dao.getTotal(bo);
        vo.setTotal(total);
        if (total == 0) return vo;
        List<SysParamType> sysParamTypes = dao.query(bo);
        vo.setData(VoHelper.wrapVos(sysParamTypes, this));
        return vo;
    }

    @Override
    public SysParamTypeVo findById(String id) {
        return wrap(dao.findById(id));
    }


    @Override
    public void deleteByIds(String... ids) {
        Assert.notEmpty(ids, "删除失败!ID列表不能为空!");
        SysParamTypeBo bo = new SysParamTypeBo();
        for (String id : ids) {
            SysParamType type = dao.findById(id);
            if (type == null) {
                continue;
            }
            // 判断是否被关联
            bo.setParentId(id);
            Long total = dao.getTotal(bo);
            Assert.isTrue(total == 0, "删除失败!存在子类型，无法直接删除!请删除了所有的子类型后再执行该操作!");

            // 删除所有的参数项
            sysParamItemDao.deleteByType(type.getCode());

            // 删除
            dao.delete(type);

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }

    @Override
    public void disable(String... ids) {
        Assert.notEmpty(ids, "禁用失败!ID列表不能为空!");
        for (String id : ids) {
            SysParamType type = dao.findById(id);
            Assert.notNull(type, "禁用失败!参数类型不存在!请刷新后重试!");
            type.setStatus(CommonStatus.CANCELED.getValue());

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }

    @Override
    public void enable(String... ids) {
        Assert.notEmpty(ids, "启用失败!ID列表不能为空!");
        for (String id : ids) {
            SysParamType type = dao.findById(id);
            Assert.notNull(type, "启用失败!参数类型不存在!请刷新后重试!");
            type.setStatus(CommonStatus.ACTIVE.getValue());

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }


    public SysParamTypeVo wrap(SysParamType sysParamType) {
        if (sysParamType == null) return null;
        SysParamTypeVo vo = new SysParamTypeVo();
        BeanUtils.copyProperties(sysParamType, vo);
        List<SysParamType> children = sysParamType.getChildren();
        if (children != null && children.size() > 0) {
            vo.setChildren(VoHelper.wrapVos(children, this));
        }
        ParameterContainer container = ParameterContainer.getInstance();
        vo.setStatusName(container.getSystemName(ParameterConstant.COMMON_STATE, vo.getStatus()));
        return vo;
    }

    @Override
    public boolean hasName(String parentId, String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("名称不能为空!");
        }
        return dao.hasName(parentId, name);
    }

    @Override
    public boolean hasCode(String code) {
        return dao.hasCode(code);
    }

    @Override
    public List<SysParamTypeVo> queryOther(String id) {
        List<SysParamType> params = dao.queryOther(id);
        return BeanWrapBuilder.newInstance().wrapList(params, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> allForTree() {
        List<SysParamType> types = dao.query(null);
        return BeanWrapBuilder.newInstance().wrapList(types, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> queryChildren(String id, boolean containSelf) {
        List<SysParamType> types = dao.queryChildren(id);
        if (containSelf) {
            types.add(0, dao.findById(id));
        }
        return BeanWrapBuilder.newInstance().wrapList(types, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> queryUsingTree() {
        List<SysParamType> params = dao.queryUsing();
        return BeanWrapBuilder.newInstance().wrapList(params, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> queryValidTree() {
        SysParamTypeBo bo = new SysParamTypeBo();
        bo.setStatus(CommonStatus.ACTIVE.getValue());
        List<SysParamType> params = dao.query(bo);
        return BeanWrapBuilder.newInstance().wrapList(params, SysParamTypeVo.class);
    }

    @Override
    public String getName(String code) {
        return dao.getName(code);
    }

}
