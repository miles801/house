package com.michael.spec.service;

import com.michael.spec.bo.CustomerBo;
import com.michael.spec.domain.Customer;
import com.michael.spec.vo.CustomerVo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface CustomerService {

    /**
     * 保存
     */
    String save(Customer customer);

    /**
     * 更新
     */
    void update(Customer customer);

    /**
     * 分页查询
     */
    PageVo pageQuery(CustomerBo bo);

    /**
     * 根据ID查询对象的信息
     */
    CustomerVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 申请为无效
     *
     * @param id 客户ID
     */
    void applyInvalid(String id);
}
