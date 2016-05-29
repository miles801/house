package com.michael.base.position.dao;

import com.michael.base.position.bo.PositionEmpBo;
import com.michael.base.position.domain.PositionEmp;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionEmpDao {

    String save(PositionEmp positionEmp);

    void update(PositionEmp positionEmp);

    /**
     * 高级查询接口
     */
    List<PositionEmp> query(PositionEmpBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(PositionEmpBo bo);

    PositionEmp findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(PositionEmp positionEmp);

    /**
     * 根据岗位ID和员工ID进行唯一定位
     *
     * @param positionId 岗位ID
     * @param empId      员工ID
     */
    PositionEmp find(String positionId, String empId);

    /**
     * 查询指定岗位下的所有员工的ID集合
     *
     * @param positionId 岗位ID
     * @return 员工ID的集合
     */
    List<String> queryEmp(String positionId);

    /**
     * 查询指定岗位下员工的数量
     *
     * @param positionId 岗位ID
     * @return 数量
     */
    Long queryEmpTotal(String positionId);
}
