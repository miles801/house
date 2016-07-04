package com.michael.spec.dao;

import com.michael.spec.bo.BuildingBo;
import com.michael.spec.domain.Building;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

/**
 * @author Michael
 */
public interface BuildingDao {

    String save(Building building);

    void update(Building building);

    /**
     * 获得个人楼盘ID列表的离线查询对象
     *
     * @param empId 员工ID
     * @return 离线查询对象（只包含楼盘ID）
     */
    DetachedCriteria getPersonalBuilding(String empId);

    /**
     * 高级查询接口
     */
    List<Building> query(BuildingBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(BuildingBo bo);

    Building findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Building building);

    /**
     * 判断指定名称的楼盘是否存在
     *
     * @param name 楼盘名称
     * @param id   楼盘ID
     * @return true：存在
     */
    boolean hasName(String name, String id);
}
