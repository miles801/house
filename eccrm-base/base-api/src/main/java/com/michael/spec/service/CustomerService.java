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
     * 批量新增
     *
     * @param ids 客户ID
     */
    void batchAdd(String[] ids);

    /**
     * 批量修改
     *
     * @param ids 客户ID
     */
    void batchModify(String[] ids);

    /**
     * 申请为无效
     *
     * @param ids 客户ID
     */
    void applyInvalid(String[] ids);

    /**
     * 批量将客户设置为无效
     *
     * @param ids 客户ID集合
     */
    void batchPassInvalid(String[] ids);

    /**
     * 给指定客户添加一套房产
     *
     * @param customerId 客户ID
     * @param roomId     房屋ID
     * @return 客户所拥有的最新的房产数量
     */
    Integer addRoom(String customerId, String roomId);

    /**
     * 批量通过客户（即将客户的状态设置为“正常”）
     *
     * @param customerIds 客户ID列表
     */
    void batchPass(String[] customerIds);

    /**
     * 批量不通过（即将客户的状态设置为“未录入”）
     *
     * @param customerIds 客户ID列表
     */
    void batchDeny(String[] customerIds);

    /**
     * 导入客户数据
     *
     * @param attachmentIds 附件列表
     */
    void importData(String[] attachmentIds);

    /**
     * 将客户变为“有效”
     * 只针对“无效客户”
     *
     * @param ids 客户ID列表
     */
    void applyValid(String[] ids);
}
