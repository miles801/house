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

    void batchSetStatus(String[] customerIds, String status);

    /**
     * 判断指定的电话号码是否已经被其他客户注册
     *
     * @param phone1 电话号码
     * @param id     客户ID
     * @return TRUE已经被注册
     */
    boolean hasPhone(String phone1, String id);

    /**
     * 判断一个客户是否重复
     *
     * @param buildingId 楼盘ID
     * @param name       姓名
     * @param phone1     电话
     * @param id         排除的ID
     * @return true重复
     */
    boolean hasSame(String buildingId, String name, String phone1, String id);
}
