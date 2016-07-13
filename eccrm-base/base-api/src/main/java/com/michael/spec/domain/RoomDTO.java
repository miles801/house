package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.michael.poi.annotation.Col;
import com.michael.poi.annotation.ImportConfig;
import com.michael.poi.core.DTO;

/**
 * @author Michael
 */
@ImportConfig(file = "", startRow = 1)
public class RoomDTO implements DTO {


    // 楼盘名称
    @Col(index = 0)
    private String buildingName;

    // 楼栋名称
    @Col(index = 1)
    private String blockName;

    // 单元
    @Col(index = 2)
    private String unitName;

    // 楼层
    @Col(index = 3)
    private Integer floor;

    // 门牌号
    @Col(index = 4)
    private String code;

    @ApiField("面积")
    @Col(index = 5)
    private Double square;

    @ApiField("朝向")
    @Col(index = 6)
    private String orient;

    @ApiField("户型-室")
    @Col(index = 7)
    private String type1;
    @ApiField("户型-厅")
    @Col(index = 8)
    private String type2;
    @ApiField("户型-厨")
    @Col(index = 9)
    private String type3;
    @ApiField("户型-卫")
    @Col(index = 10)
    private String type4;

    @ApiField("产权性质")
    @Col(index = 11)
    private String houseProperty;

    @Col(index = 12)
    @ApiField("业务参数：现状")
    private String houseUseType;

    @Col(index = 13)
    private String c1;

    @ApiField("客户姓名")
    @Col(index = 14)
    private String cusName;

    @ApiField("客户身份证号码")
    @Col(index = 15)
    private String cusIDCard;

    @ApiField("客户性别")
    @Col(index = 16)
    private String cusSex;
    @ApiField("客户年龄段")
    @Col(index = 17)
    private String cusAge;

    @ApiField("客户婚姻状况")
    @Col(index = 18)
    private String cusMarriage;
    @ApiField("家庭人口数")
    @Col(index = 19)
    private Integer cusFamilyCount;

    @Col(index = 20)
    private String cusPhone;
    @Col(index = 21)
    private String cusPhone2;
    @Col(index = 22)
    private String cusPhone3;
    @Col(index = 23)
    private String cusEmail;
    @Col(index = 24)
    private String cusWechat;
    @ApiField("职业")
    @Col(index = 25)
    private String cusDuty;
    @ApiField("工作单位")
    @Col(index = 26)
    private String cusCompany;

    @ApiField("学历")
    @Col(index = 27)
    private String cusEducation;
    @ApiField("资产规模")
    @Col(index = 28)
    private String cusMoney;

    @ApiField("车位1")
    @Col(index = 29)
    private String cusCarSite1;
    @ApiField("车位2")
    @Col(index = 30)
    private String cusCarSite2;
    @ApiField("车牌号")
    @Col(index = 31)
    private String cusCarNo;
    @ApiField("车型")
    @Col(index = 32)
    private String cusCarType;
    @ApiField("客户备注")
    @Col(index = 33)
    private String cusDescription;


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


    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getOrient() {
        return orient;
    }

    public void setOrient(String orient) {
        this.orient = orient;
    }

    public String getHouseProperty() {
        return houseProperty;
    }

    public void setHouseProperty(String houseProperty) {
        this.houseProperty = houseProperty;
    }

    public String getCusIDCard() {
        return cusIDCard;
    }

    public void setCusIDCard(String cusIDCard) {
        this.cusIDCard = cusIDCard;
    }

    public String getCusSex() {
        return cusSex;
    }

    public void setCusSex(String cusSex) {
        this.cusSex = cusSex;
    }

    public String getCusAge() {
        return cusAge;
    }

    public void setCusAge(String cusAge) {
        this.cusAge = cusAge;
    }

    public String getCusMarriage() {
        return cusMarriage;
    }

    public void setCusMarriage(String cusMarriage) {
        this.cusMarriage = cusMarriage;
    }

    public Integer getCusFamilyCount() {
        return cusFamilyCount;
    }

    public void setCusFamilyCount(Integer cusFamilyCount) {
        this.cusFamilyCount = cusFamilyCount;
    }

    public String getCusPhone2() {
        return cusPhone2;
    }

    public void setCusPhone2(String cusPhone2) {
        this.cusPhone2 = cusPhone2;
    }

    public String getCusPhone3() {
        return cusPhone3;
    }

    public void setCusPhone3(String cusPhone3) {
        this.cusPhone3 = cusPhone3;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public String getCusWechat() {
        return cusWechat;
    }

    public void setCusWechat(String cusWechat) {
        this.cusWechat = cusWechat;
    }

    public String getCusDuty() {
        return cusDuty;
    }

    public void setCusDuty(String cusDuty) {
        this.cusDuty = cusDuty;
    }

    public String getCusCompany() {
        return cusCompany;
    }

    public void setCusCompany(String cusCompany) {
        this.cusCompany = cusCompany;
    }

    public String getCusEducation() {
        return cusEducation;
    }

    public void setCusEducation(String cusEducation) {
        this.cusEducation = cusEducation;
    }

    public String getCusMoney() {
        return cusMoney;
    }

    public void setCusMoney(String cusMoney) {
        this.cusMoney = cusMoney;
    }

    public String getCusCarSite1() {
        return cusCarSite1;
    }

    public void setCusCarSite1(String cusCarSite1) {
        this.cusCarSite1 = cusCarSite1;
    }

    public String getCusCarSite2() {
        return cusCarSite2;
    }

    public void setCusCarSite2(String cusCarSite2) {
        this.cusCarSite2 = cusCarSite2;
    }

    public String getCusCarNo() {
        return cusCarNo;
    }

    public void setCusCarNo(String cusCarNo) {
        this.cusCarNo = cusCarNo;
    }

    public String getCusCarType() {
        return cusCarType;
    }

    public void setCusCarType(String cusCarType) {
        this.cusCarType = cusCarType;
    }

    public String getCusDescription() {
        return cusDescription;
    }

    public void setCusDescription(String cusDescription) {
        this.cusDescription = cusDescription;
    }
}
