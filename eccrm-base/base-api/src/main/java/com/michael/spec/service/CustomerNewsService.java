package com.michael.spec.service;

import com.michael.spec.bo.CustomerNewsBo;
import com.michael.spec.domain.CustomerNews;
import com.michael.spec.vo.CustomerNewsVo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface CustomerNewsService {

    /**
     * 保存
     */
    String save(CustomerNews customerNews);

    /**
     * 更新
     */
    void update(CustomerNews customerNews);

    /**
     * 分页查询
     */
    PageVo pageQuery(CustomerNewsBo bo);

    /**
     * 根据ID查询对象的信息
     */
    CustomerNewsVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

}
