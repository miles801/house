package com.michael.spec.domain;

import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 楼栋
 *
 * @author Michael
 */
@Entity
@Table(name = "spec_block")
public class Block extends CommonDomain {

    @Column(length = 40, nullable = false)
    @NotNull(message = "楼栋必须与楼盘关联!")
    private String buildingId;

    /**
     * 编号
     */
    @Column(nullable = false, length = 40)
    @NotNull(message = "楼栋编号不能为空!")
    private String code;


    /**
     * 单元数
     */
    @Column
    private Integer unitCounts;

    // 实际单元数量
    @Column
    private Integer realCounts;
    /**
     * 物理层高
     */
    @Column
    private Integer physicalLevels;
    /**
     * 标号层高
     */
    @Column
    private Integer levels;

    public Integer getRealCounts() {
        return realCounts;
    }

    public void setRealCounts(Integer realCounts) {
        this.realCounts = realCounts;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUnitCounts() {
        return unitCounts;
    }

    public void setUnitCounts(Integer unitCounts) {
        this.unitCounts = unitCounts;
    }

    public Integer getPhysicalLevels() {
        return physicalLevels;
    }

    public void setPhysicalLevels(Integer physicalLevels) {
        this.physicalLevels = physicalLevels;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }
}
