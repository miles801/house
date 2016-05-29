package com.michael.base.position.domain;

import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 岗位关联关系，用于保存岗位的所有上级
 *
 * @author Michael
 */
@Entity
@Table(name = "sys_position_rel")
public class PositionRelation extends CommonDomain {

    @NotNull
    @Column(length = 40, nullable = false)
    private String positionId;

    @NotNull
    @Column(length = 40, nullable = false)
    private String childId;


    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
