package com.michael.base.emp.service.impl;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.base.emp.service.EmpService;
import com.michael.base.emp.vo.EmpVo;
import com.michael.base.org.dao.OrgDao;
import com.michael.base.position.dao.PositionEmpDao;
import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.ycrl.core.SystemContainer;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import com.ycrl.utils.md5.MD5Utils;
import com.ycrl.utils.string.StringUtils;
import com.ycrl.utils.uuid.UUIDGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("empService")
public class EmpServiceImpl implements EmpService, BeanWrapCallback<Emp, EmpVo> {
    @Resource
    private EmpDao empDao;

    @Resource
    private PositionEmpDao positionEmpDao;

    @Override
    public String save(Emp emp) {
        emp.setId(UUIDGenerator.generate());
        // 设置初始化数据
        emp.setLocked(0);
        emp.setPassword(MD5Utils.encode("111111"));
        emp.setPinyin(new SimplePinYin().toPinYin(emp.getName(), new StandardStrategy()));

        // 验证合法性：员工编号和登录用户名
        validate(emp);

        // 保存
        String id = empDao.save(emp);
        return id;
    }

    private void validate(Emp emp) {
        ValidatorUtils.validate(emp);
        // 检查用户编号
        if (StringUtils.isNotEmpty(emp.getCode())) {
            boolean hasCode = empDao.hasCode(emp.getCode(), emp.getId());
            Assert.isTrue(!hasCode, "操作失败!工号重复!");
        }

        // 检查登录用户名
        boolean hasLoginName = empDao.hasLoginName(emp.getLoginName(), emp.getId());
        Assert.isTrue(!hasLoginName, "操作失败!登录用户名已经存在!");

    }

    @Override
    public void update(Emp emp) {

        // 验证合法性
        validate(emp);

        emp.setPinyin(new SimplePinYin().toPinYin(emp.getName(), new StandardStrategy()));

        // 机构变更的问题 获取原来的机构，改变新的
        empDao.update(emp);
    }

    @Override
    public PageVo pageQuery(EmpBo bo) {
        PageVo vo = new PageVo();
        Long total = empDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Emp> empList = empDao.query(bo);
        List<EmpVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(empList, EmpVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public EmpVo findById(String id) {
        Emp emp = empDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrap(emp, EmpVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Emp emp = empDao.findById(id);
            Assert.notNull(emp, "禁用失败!员工不存在，请刷新后重试!");
            emp.setLocked(Emp.STATUS_LOCKED);
        }
    }

    @Override
    public EmpVo login(String loginName, String password) {
        Assert.hasText(loginName, "登录失败!未获取到登录名!");
        Assert.hasText(password, "登录失败!未获取到密码!");
        Emp emp = empDao.findByLoginName(loginName);
        Assert.notNull(emp, "登录失败!用户名不正确!");
        Assert.isTrue(emp.getLocked().equals(Emp.STATUS_NORMAL), "登录失败!用户已经被锁或已经离职!");
        Assert.isTrue(password.equals(emp.getPassword()), "登录失败!密码不正确!");

        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"password"})
                .exclude()
//                .setCallback(this)
                .wrap(emp, EmpVo.class);
    }

    @Override
    public void start(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Emp emp = empDao.findById(id);
            Assert.notNull(emp, "启用失败!员工不存在，请刷新后重试!");
            emp.setLocked(Emp.STATUS_NORMAL);
        }
    }

    @Override
    public void updatePwd(String oldPwd, String newPwd) {
        Assert.hasText(oldPwd, "更新密码失败!原密码不能为空!");
        Assert.hasText(newPwd, "更新密码失败!新密码不能为空!");
        Emp emp = empDao.findById(SecurityContext.getEmpId());
        Assert.notNull(emp, "更新密码失败!当前用户不存在!");
        Assert.isTrue(emp.getPassword().equals(oldPwd), "更新密码失败!原密码不正确!");
        empDao.updatePwd(SecurityContext.getEmpId(), newPwd);
    }

    @Override
    public PageVo queryByOrg(String orgId, EmpBo bo) {
        Assert.hasText(orgId, "查询失败!组织机构ID不能为空!");
        PageVo vo = new PageVo();
        if (bo != null) {
            bo.setOrgId(null);
        }
        Long total = empDao.getTotalByOrg(orgId, bo);
        if (total == null || total == 0) {
            return vo;
        }
        vo.setTotal(total);
        List<Emp> data = empDao.queryByOrg(orgId, bo);
        vo.setData(BeanWrapBuilder.newInstance().setCallback(this)
                .wrapList(data, EmpVo.class));
        return vo;
    }

    @Override
    public PageVo queryByPosition(String positionId, EmpBo bo) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空!");
        PageVo vo = new PageVo();
        List<String> empIds = positionEmpDao.queryEmp(positionId);
        if (empIds == null || empIds.isEmpty()) {
            vo.setTotal(0L);
            return vo;
        }
        if (bo == null) {
            bo = new EmpBo();
        }
        bo.setIds(empIds);
        Long total = empDao.getTotal(bo);
        if (total == null || total == 0) {
            vo.setTotal(0L);
            return vo;
        }
        vo.setTotal(total);
        List<Emp> data = empDao.query(bo);
        vo.setData(BeanWrapBuilder.newInstance().setCallback(this)
                .wrapList(data, EmpVo.class));
        return vo;
    }

    @Override
    public void doCallback(Emp emp, EmpVo vo) {
        Integer locked = emp.getLocked();
        if (locked == null) {

        } else if (locked == 0) {
            vo.setLockedName("正常");
        } else if (locked == 1) {
            vo.setLockedName("锁定");
        } else if (locked == 2) {
            vo.setLockedName("离职");
        } else {
            vo.setLockedName("异常");
        }

        // 设置机构的名称
        OrgDao orgDao = SystemContainer.getInstance().getBean(OrgDao.class);
        if (StringUtils.isNotEmpty(emp.getOrgId())) {
            vo.setOrgName(orgDao.findName(emp.getOrgId()));
        }
    }
}
