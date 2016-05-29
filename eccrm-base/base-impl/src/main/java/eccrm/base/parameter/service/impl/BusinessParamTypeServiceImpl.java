package eccrm.base.parameter.service.impl;

import com.ycrl.core.pager.PageVo;
import eccrm.base.common.enums.CommonStatus;
import eccrm.base.constant.ParameterConstant;
import eccrm.base.parameter.bo.BusinessParamTypeBo;
import eccrm.base.parameter.dao.BusinessParamItemDao;
import eccrm.base.parameter.dao.BusinessParamTypeDao;
import eccrm.base.parameter.domain.BusinessParamType;
import eccrm.base.parameter.service.BusinessParamTypeService;
import eccrm.base.parameter.service.ParameterContainer;
import eccrm.base.parameter.vo.BusinessParamTypeVo;
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
 * @datetime: 2014-07-02
 */
@Service("businessParamTypeService")
public class BusinessParamTypeServiceImpl implements BusinessParamTypeService, VoWrapper<BusinessParamType, BusinessParamTypeVo> {
    @Resource
    private BusinessParamTypeDao dao;

    @Resource
    private BusinessParamItemDao businessParamItemDao;

    @Override
    public String save(BusinessParamType sysParamType) {
        sysParamType.setStatus(CommonStatus.ACTIVE.getValue());
        String id = dao.save(sysParamType);
        return id;
    }


    @Override
    public void update(BusinessParamType sysParamType) {
        dao.update(sysParamType);

        // 更新缓存
        ParameterContainer.getInstance().reloadBusiness(sysParamType.getCode());
    }

    @Override
    public PageVo query(BusinessParamTypeBo bo) {
        PageVo vo = new PageVo();
        Long total = dao.getTotal(bo);
        vo.setTotal(total);
        if (total == 0) return vo;
        List<BusinessParamType> sysParamTypes = dao.query(bo);
        vo.setData(VoHelper.wrapVos(sysParamTypes, this));
        return vo;
    }

    @Override
    public BusinessParamTypeVo findById(String id) {
        return wrap(dao.findById(id));
    }


    @Override
    public void deleteByIds(String... ids) {
        Assert.notEmpty(ids, "删除失败!ID列表不能为空!");
        BusinessParamTypeBo bo = new BusinessParamTypeBo();
        for (String id : ids) {
            BusinessParamType type = dao.findById(id);
            if (type == null) {
                continue;
            }

            // 判断是否被关联
            bo.setParentId(id);
            Long total = dao.getTotal(bo);
            Assert.isTrue(total == 0, "删除失败!存在子类型，无法直接删除!请删除了所有的子类型后再执行该操作!");

            // 删除关联的参数
            businessParamItemDao.deleteByType(type.getCode());

            // 删除类型
            dao.delete(type);

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }

    @Override
    public void disable(String... ids) {
        Assert.notEmpty(ids, "禁用失败!ID列表不能为空!");
        for (String id : ids) {
            BusinessParamType type = dao.findById(id);
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
            BusinessParamType type = dao.findById(id);
            Assert.notNull(type, "启用失败!参数类型不存在!请刷新后重试!");
            type.setStatus(CommonStatus.ACTIVE.getValue());

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }

    public BusinessParamTypeVo wrap(BusinessParamType businessParamType) {
        if (businessParamType == null) return null;
        BusinessParamTypeVo vo = new BusinessParamTypeVo();
        BeanUtils.copyProperties(businessParamType, vo);
        List<BusinessParamType> children = businessParamType.getChildren();
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
    public List<BusinessParamTypeVo> queryOther(String id) {
        List<BusinessParamType> params = dao.queryOther(id);
        return VoHelper.wrapVos(params, this);
    }

    @Override
    public List<BusinessParamTypeVo> allForTree() {
        List<BusinessParamType> params = dao.query(null);
        return VoHelper.wrapVos(params, this);
    }

    @Override
    public List<BusinessParamTypeVo> queryChildren(String id, boolean containSelf) {
        List<BusinessParamType> types = dao.queryChildren(id);
        if (containSelf) {
            types.add(0, dao.findById(id));
        }
        return VoHelper.wrapVos(types, this);
    }

    @Override
    public List<BusinessParamTypeVo> queryUsingTree() {
        List<BusinessParamType> params = dao.queryUsing();
        return VoHelper.wrapVos(params, this);
    }

    @Override
    public List<BusinessParamTypeVo> queryValidTree() {
        BusinessParamTypeBo bo = new BusinessParamTypeBo();
        bo.setStatus(CommonStatus.ACTIVE.getValue());
        List<BusinessParamType> params = dao.query(bo);
        return VoHelper.wrapVos(params, this);
    }

    @Override
    public String getName(String code) {
        return dao.getName(code);
    }

}
