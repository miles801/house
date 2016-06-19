package com.michael.spec.dao;

import com.michael.spec.bo.CustomerBo;
import com.michael.spec.domain.Customer;

import java.util.List;

/**
 * @author Michael
 */
public interface CustomerDao {

    String save(Customer customer);

    void update(Customer customer);

    /**
     * 高级查询接口
     */
    List<Customer> query(CustomerBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(CustomerBo bo);

    Customer findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Customer customer);

    /**
     * 查找最大的客户编号
     */
    String maxCode();

    /**
     * 根据电话号码查找客户
     *
     * @param phone 主电话号
     * @return 客户
     */
    Customer findByPhone(String phone);
}
