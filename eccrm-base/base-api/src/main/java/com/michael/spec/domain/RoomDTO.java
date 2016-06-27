package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.michael.poi.annotation.Col;
import com.michael.poi.annotation.ImportConfig;
import com.michael.poi.core.DTO;

import javax.persistence.Column;

/**
 * @author Michael
 */
@ImportConfig(file = "", startRow = 1)
public class RoomDTO implements DTO {


    @Col(index = 0)
    private String buildingName;

    @Col(index = 1)
    private String blockName;

    @Col(index = 2)
    private String unitName;

    @Col(index = 3)
    private Integer floor;

    @Col(index = 4)
    private String code;

    @ApiField("面积")
    @Col(index = 5)
    private Double square;

    @ApiField("户型-室")
    @Col(index = 6)
    private String type1;
    @ApiField("户型-厅")
    @Col(index = 7)
    private String type2;
    @ApiField("户型-厨")
    @Col(index = 8)
    private String type3;
    @ApiField("户型-卫")
    @Col(index = 9)
    private String type4;

    @Col(index = 10)
    @ApiField("业务参数：现状")
    private String houseUseType;

    @Col(index = 11)
    private String cusName;
    @Col(index = 12)
    private String cusPhone;
    @Col(index = 13)
    private String status;
    @Col(index = 14)
    private String description;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getType4() {
        return type4;
    }

    public void setType4(String type4) {
        this.type4 = type4;
    }

    public String getHouseUseType() {
        return houseUseType;
    }

    public void setHouseUseType(String houseUseType) {
        this.houseUseType = houseUseType;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @ApiField("业主ID")
    @Column
    private String customerId;

}
