package com.michael.base.org.service;

import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.domain.Org;
import com.michael.base.org.vo.OrgDTO;
import com.michael.base.org.vo.OrgVo;
import com.ycrl.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface OrgService {

    /**
     * 保存
     */
    String save(Org org);

    /**
     * 更新
     */
    void update(Org org);

    /**
     * 分页查询
     */
    PageVo pageQuery(OrgBo bo);

    /**
     * 查询直接子机构
     *
     * @param parentId 上级机构的ID（如果为空，则表示查询根）
     */
    List<OrgVo> children(String parentId);

    /**
     * 查询指定机构下的人和机构
     *
     * @param parentId 机构ID
     */
    OrgDTO childrenAndEmp(String parentId);

    /**
     * 根据ID查询对象的信息
     */
    OrgVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 启用指定的组织机构
     *
     * @param ids ID集合
     */
    void enableAll(String[] ids);

    List<OrgVo> tree(OrgBo bo);

    /**
     * 给指定机构添加X个员工（会同时更新父级所有机构的员工数量）
     *
     * @param orgId     机构
     * @param empCounts 员工数量
     * @return 当前机构最新的员工数量
     */
    Integer addEmp(String orgId, Integer empCounts);
}
