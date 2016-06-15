package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Michael
 */

@Entity
@Table(name = "spec_room")
public class Room extends CommonDomain {

    @NotNull
    @Column(nullable = false, length = 40)
    private String blockId;
    @Column(nullable = false, length = 40)
    private String blockCode;

    @Column(nullable = false, length = 40)
    private String unitId;
    @Column(nullable = false, length = 40)
    private String unitCode;

    @NotNull(message = "楼层信息不能为空!")
    @Column(nullable = false)
    private Integer floor;

    @NotNull(message = "房间号不能为空")
    @Column(nullable = false, length = 20)
    private String code;

    @ApiField("面积")
    @Column
    private Double square;

    @ApiField("户型")
    @Column(length = 40)
    private String type;
    @ApiField("朝向")
    @Column(length = 40)
    private String orient;

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
