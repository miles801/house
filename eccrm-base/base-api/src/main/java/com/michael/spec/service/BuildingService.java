package com.michael.spec.service;

import com.michael.spec.bo.BuildingBo;
import com.michael.spec.domain.Building;
import com.michael.spec.vo.BuildingVo;
import com.ycrl.core.pager.PageVo;

/**
 * @author Michael
 */
public interface BuildingService {

    /**
     * 保存
     */
    String save(Building building);


    /**
     * 添加维护人
     *
     * @param id     楼盘ID
     * @param empIds 负责人ID列表
     */
    void addMaintain(String id, String... empIds);

    /**
     * 删除维护人
     *
     * @param id    楼盘ID
     * @param empId 负责人ID
     */
    void removeMaintain(String id, String empId);

    /**
     * 变更负责人
     *
     * @param id    楼盘ID
     * @param empId 员工ID
     */
    void updateMaster(String id, String empId);

    /**
     * 更新
     */
    void update(Building building);

    /**
     * 分页查询
     */
    PageVo pageQuery(BuildingBo bo);

    /**
     * 根据ID查询对象的信息
     */
    BuildingVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

}
