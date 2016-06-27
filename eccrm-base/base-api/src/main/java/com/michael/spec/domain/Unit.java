package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 单元
 *
 * @author Michael
 */
@Entity
@Table(name = "spec_unit")
public class Unit extends CommonDomain {
    @NotNull
    @Column(nullable = false, length = 40)
    private String blockId;

    @NotNull
    @Column(nullable = false, length = 40)
    @ApiField("单元号")
    private String code;

    @Column(length = 20)
    @ApiField("门牌号")
    private String doorCode;

    @Column
    @ApiField("面积")
    private Double square;

    @Column(length = 20)
    @ApiField("户型")
    private String type;

    @Column(length = 40)
    @ApiField("朝向")
    private String orient;

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDoorCode() {
        return doorCode;
    }

    public void setDoorCode(String doorCode) {
        this.doorCode = doorCode;
    }

    public Double getSquare() {
        return square;
    }

    public void setSquare(Double square) {
        this.square = square;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrient() {
        return orient;
    }

    public void setOrient(String orient) {
        this.orient = orient;
    }
}
