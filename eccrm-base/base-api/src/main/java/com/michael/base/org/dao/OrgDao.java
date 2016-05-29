package com.michael.base.org.dao;

import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.domain.Org;

import java.util.List;
import java.util.Set;

/**
 * @author Michael
 */
public interface OrgDao {

    String save(Org org);

    void update(Org org);

    /**
     * 高级查询接口
     */
    List<Org> query(OrgBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(OrgBo bo);

    Org findById(String id);

    void deleteById(String id);

    /**
     * 指定编号是否存在
     *
     * @param code 编号
     * @param id   ID
     * @return true存在 false不存在
     */
    Boolean hasCode(String code, String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Org org);

    /**
     * 获得指定机构的上级机构
     *
     * @param orgId 机构ID
     * @return 上级机构
     */
    Org findParent(String orgId);

    /**
     * 查询指定机构的所有子结构（包括隔代），结果集中不包含自身
     * 利用的是path属性
     *
     * @param orgId 机构ID
     * @return 所有的子组织机构
     */
    List<Org> allChildren(String orgId);

    /**
     * 重置指定机构的isParent状态
     *
     * @param orgId 机构ID
     */
    void resetIsParent(String orgId);

    /**
     * 禁用指定节点及该节点下的所有子节点
     *
     * @param orgId 节点ID
     */
    void disableAll(String orgId);

    /**
     * 启用指定节点
     *
     * @param orgIds 节点ID列表
     */
    void enableAll(Set<String> orgIds);

    /**
     * 通过全名找到对应的组织对象
     *
     * @param longName
     * @return
     */
    Org findOrgByOrgFullName(String longName);


    /**
     * 根据ID查询名称
     *
     * @param id
     * @return
     */
    String findName(String id);
}
