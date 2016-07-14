package com.michael.spec.dao;

import com.michael.spec.bo.CustomerNewsBo;
import com.michael.spec.domain.CustomerNews;

import java.util.List;

/**
 * @author Michael
 */
public interface CustomerNewsDao {

    String save(CustomerNews customerNews);

    void update(CustomerNews customerNews);

    /**
     * 高级查询接口
     */
    List<CustomerNews> query(CustomerNewsBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(CustomerNewsBo bo);

    CustomerNews findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(CustomerNews customerNews);
}
