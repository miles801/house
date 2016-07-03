package com.michael.base.position.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 岗位（角色）
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_position")
public class Position extends CommonDomain {

    @ApiField(value = "岗位名称")
    @NotNull
    @Column(nullable = false, length = 40)
    private String name;

    @Column(unique = true, length = 40)
    @ApiField(value = "岗位编号，唯一")
    private String code;

    @ApiField(value = "上级岗位")
    @Column(length = 40)
    private String parentId;

    @ApiField(value = "上级岗位名称")
    @Column(length = 40)
    private String parentName;

    @ApiField(value = "该岗位允许的最小员工数")
    @Column
    private Integer minEmp;

//    @ApiField(value = "该岗位允许的最大员工数")
//    @Column
//    private Integer maxEmp;

//    @ApiField(value = "该岗位当前员工数")
//    @Column
//    private Integer empCounts;

    @ApiField(value = "是否为父节点/是否有子节点")
    @Column
    private Boolean isParent;

    @ApiField(value = "排序号")
    @Column
    private Integer sequenceNo;


    @ApiField(value = "是否禁用")
    @NotNull
    @Column
    private Boolean deleted;

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getMinEmp() {
        return minEmp;
    }

    public void setMinEmp(Integer minEmp) {
        this.minEmp = minEmp;
    }

}
