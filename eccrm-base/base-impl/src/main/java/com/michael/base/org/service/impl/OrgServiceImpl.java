package com.michael.base.org.service.impl;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.base.emp.vo.EmpVo;
import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.dao.OrgDao;
import com.michael.base.org.dao.OrgEmpDao;
import com.michael.base.org.domain.Org;
import com.michael.base.org.service.OrgService;
import com.michael.base.org.vo.OrgDTO;
import com.michael.base.org.vo.OrgVo;
import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.ycrl.core.SystemContainer;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.number.IntegerUtils;
import com.ycrl.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Michael
 */
@Service("orgService")
public class OrgServiceImpl implements OrgService, BeanWrapCallback<Org, OrgVo> {
    @Resource
    private OrgDao orgDao;

    @Resource
    private OrgEmpDao orgEmpDao;

    @Override
    public String save(Org org) {
        org.setHide(false);

        // 新增时默认不是父级
        org.setParent(false);

        ValidatorUtils.validate(org);

        String id = orgDao.save(org);
        // 初始化数据
        init(org);

        return id;
    }


    @Override
    public void update(Org org) {
        ValidatorUtils.validate(org);

        init(org);

        // 更新
        orgDao.update(org);

        // 重置所有子组织机构的path
        List<Org> children = orgDao.allChildren(org.getId());
        if (children != null) {
            for (Org child : children) {
                child.setPath(org.getPath() + child.getId() + "/");
            }
        }

        // 重置父级机构的状态

    }

    private void init(Org org) {

        org.setEmpCounts(0);
        org.setTotalEmpCounts(0);
        // 设置层级
        setLevel(org);

        // 设置拼音
        setPinYin(org);

        // 检查编号
        if (StringUtils.isNotEmpty(org.getCode())) {
            Boolean exists = orgDao.hasCode(org.getCode(), org.getId());
            Assert.isTrue(!exists, "保存失败!编号重复!");
        }
    }

    private void setLevel(Org org) {
        if (StringUtils.isEmpty(org.getParentId())) {
            org.setLevel(0);
            // 设置path
            org.setPath("/" + org.getId() + "/");

        } else {
            Org parent = orgDao.findById(org.getParentId());
            Assert.notNull(parent, "操作失败!上级机构不存在!请刷新后重试!");
            Assert.isTrue(parent.getLevel() != null && parent.getLevel() > -1, "数据错误!组织机构[" + parent.getName() + "]的层级不在正常范围内!");
            org.setLevel(parent.getLevel() + 1);

            // 设置path
            org.setPath(parent.getPath() + org.getId() + "/");
            // 设置父机构
            parent.setParent(true);
        }
    }


    private void setPinYin(Org org) {
        String name = org.getName();
        SimplePinYin pinYin = new SimplePinYin();
        StandardStrategy strategy = new StandardStrategy();
        StringBuilder py = new StringBuilder(name.length());
        for (String n : name.split(".")) {
            py.append(pinYin.toPinYin(n, strategy).substring(0, 1));
        }
        org.setPinyin(py.toString());
    }


    @Override
    public List<OrgVo> children(String parentId) {
        OrgBo bo = new OrgBo();
        bo.setParentId(parentId);
        bo.setHide(false);
        List<Org> data = orgDao.query(bo);
        return BeanWrapBuilder.newInstance().wrapList(data, OrgVo.class);
    }

    @Override
    public OrgDTO childrenAndEmp(String parentId) {
        OrgBo bo = new OrgBo();
        if (StringUtils.isEmpty(parentId)) {
            bo.setParentId("0");
        } else {
            bo.setParentId(parentId);
        }
        bo.setHide(true);
        List<Org> data = orgDao.query(bo);
        OrgDTO dto = new OrgDTO();
        dto.setOrgs(data);
        if (StringUtils.isNotEmpty(parentId)) {
            EmpBo empBo = new EmpBo();
            empBo.setLocked(0);
            List<Emp> emps = SystemContainer.getInstance().getBean(EmpDao.class).queryByOrg(parentId, empBo);
            dto.setEmps(BeanWrapBuilder.newInstance()
                    .wrapList(emps, EmpVo.class));
        }
        return dto;
    }

    @Override
    public PageVo pageQuery(OrgBo bo) {
        PageVo vo = new PageVo();
        Long total = orgDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Org> orgList = orgDao.query(bo);
        List<OrgVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(orgList, OrgVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public OrgVo findById(String id) {
        Org org = orgDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(org, OrgVo.class);
    }

    @Override
    public List<OrgVo> tree(OrgBo bo) {
        if (bo == null) {
            bo = new OrgBo();
        }
        List<Org> data = orgDao.query(bo);

        return BeanWrapBuilder.newInstance()
                .wrapList(data, OrgVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            orgDao.disableAll(id);
        }
    }

    @Override
    public Integer addEmp(String orgId, Integer empCounts) {
        Assert.hasText(orgId, "添加员工失败!机构ID不能为空!");
        Assert.isTrue(empCounts != null, "添加员工失败!员工数量不能为空");
        Org org = orgDao.findById(orgId);
        Assert.notNull(org, "添加员工失败!组织机构不存在，请刷新后重试!");
        Assert.isTrue(IntegerUtils.add(org.getEmpCounts(), empCounts) <= IntegerUtils.add(org.getMaxEmp(), 0), "添加员工失败!超出该组织机构允许的最大员工数量!");
        org.setEmpCounts(IntegerUtils.add(org.getEmpCounts(), empCounts));
        org.setTotalEmpCounts(IntegerUtils.add(org.getTotalEmpCounts(), empCounts));

        // 更新所有上级的员工数量
        String[] parentIds = org.getPath().split("/");
        for (String parentId : parentIds) {
            // 排除空的和自己
            if (StringUtils.isEmpty(parentId) || parentId.equals(orgId)) {
                continue;
            }
            Org parent = orgDao.findById(parentId);
            Assert.notNull(parent, "数据错误![" + org.getName() + "]的父级不存在!请与管理员联系!~");
            parent.setTotalEmpCounts(IntegerUtils.add(parent.getTotalEmpCounts(), empCounts));
        }

        return org.getEmpCounts();
    }

    @Override
    public void enableAll(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Org org = orgDao.findById(id);
            Assert.notNull(org, "启用失败!组织机构不存在!请刷新后重试!");

            // 获取当前机构的所有上级机构
            String path[] = org.getPath().split("/");
            Set<String> parentIds = new HashSet<String>();
            for (String p : path) {
                if (StringUtils.isEmpty(p)) {
                    continue;
                }
                parentIds.add(p);
            }
            // 启用包括自身在内的所有机构
            orgDao.enableAll(parentIds);
        }
    }

    @Override
    public void doCallback(Org org, OrgVo vo) {
    }
}
