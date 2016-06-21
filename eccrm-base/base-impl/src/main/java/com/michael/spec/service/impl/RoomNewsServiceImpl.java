package com.michael.spec.service.impl;

import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.spec.bo.RoomNewsBo;
import com.michael.spec.dao.RoomNewsDao;
import com.michael.spec.domain.RoomNews;
import com.michael.spec.service.RoomNewsService;
import com.michael.spec.vo.RoomNewsVo;
import com.ycrl.core.beans.BeanWrapBuilder;
import com.ycrl.core.beans.BeanWrapCallback;
import com.ycrl.core.context.SecurityContext;
import com.ycrl.core.hibernate.validator.ValidatorUtils;
import com.ycrl.core.pager.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("roomNewsService")
public class RoomNewsServiceImpl implements RoomNewsService, BeanWrapCallback<RoomNews, RoomNewsVo> {
    @Resource
    private RoomNewsDao roomNewsDao;

    @Resource
    private EmpDao empDao;

    @Override
    public String save(RoomNews roomNews) {
        // 绑定员工信息
        String empId = SecurityContext.getEmpId();
        roomNews.setEmpId(empId);
        Emp emp = empDao.findById(empId);
        Assert.notNull(emp, "操作失败!员工不存在!请刷新后重试!");
        roomNews.setEmpName(emp.getName());
        roomNews.setPhone(emp.getPhone());

        // 校验
        ValidatorUtils.validate(roomNews);

        // 保存
        String id = roomNewsDao.save(roomNews);
        return id;
    }

    @Override
    public void update(RoomNews roomNews) {
        ValidatorUtils.validate(roomNews);
        roomNewsDao.update(roomNews);
    }

    @Override
    public PageVo pageQuery(RoomNewsBo bo) {
        PageVo vo = new PageVo();
        Long total = roomNewsDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<RoomNews> roomNewsList = roomNewsDao.query(bo);
        List<RoomNewsVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(roomNewsList, RoomNewsVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public RoomNewsVo findById(String id) {
        RoomNews roomNews = roomNewsDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(roomNews, RoomNewsVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            roomNewsDao.deleteById(id);
        }
    }

    @Override
    public void doCallback(RoomNews roomNews, RoomNewsVo vo) {
    }
}
