package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 房屋视图
 *
 * @author Michael
 */
@Entity
@Table(name = "view_spec_room")
public class RoomView {

    // 实体类属性
    @Id
    @Column
    private String id;

    @Column
    private String roomKey;

    @NotNull
    @Column(length = 40, nullable = false)
    private String buildingId;

    @NotNull
    @Column(nullable = false, length = 40)
    private String blockId;

    @Column(nullable = false, length = 40)
    private String unitId;

    @NotNull(message = "楼层信息不能为空!")
    @Column(nullable = false)
    private Integer floor;

    @NotNull(message = "房间号不能为空")
    @Column(nullable = false, length = 20)
    private String code;

    @ApiField("面积")
    @Column
    private Double square;

    @ApiField("户型-室")
    @Column(length = 40)
    private String type1;
    @ApiField("户型-厅")
    @Column(length = 40)
    private String type2;
    @ApiField("户型-厨")
    @Column(length = 40)
    private String type3;
    @ApiField("户型-卫")
    @Column(length = 40)
    private String type4;
    @ApiField("朝向")
    @Column(length = 40)
    private String orient;

    @Column(length = 40)
    @ApiField("产权性质")
    private String houseProperty;

    @Column(length = 40)
    @ApiField("业务参数：房屋使用类型")
    private String houseUseType;

    @Column(length = 1000)
    private String description;

    @Column(length = 100)
    private String col1;
    @Column(length = 100)
    private String col2;
    @Column(length = 100)
    private String col3;
    @Column(length = 100)
    private String col4;
    @Column(length = 100)
    private String col5;

    @NotNull(message = "房屋状态不能为空!")
    @Column(length = 40, nullable = false)
    private String status;

    @ApiField("客户ID")
    @Column
    private String customerId;

    @ApiField("待租")
    @Column
    private Boolean onRent;
    @ApiField("待售")
    @Column
    private Boolean onSale;
    // ===============  关联表属性  =================
    @Column
    private String buildingName;
    @Column
    private String blockCode;
    @Column
    private String unitCode;

    @Column
    private String creatorId;
    @Column
    private String creatorName;
    @Column
    private Date createdDatetime;

    // ===============  客户信息   ===================
    @Column
    private String cusName;
    @Column
    private String cusPhone;
    @Column
    private String cusWechat;
    @ApiField("性别")
    @Column
    private String sex;
    @Transient
    private String sexName;
    @ApiField("身份证号码")
    @Column
    private String idCard;

    @ApiField("电话2")
    @Column
    private String phone2;
    @ApiField("电话3")
    @Column
    private String phone3;
    @ApiField("邮箱")
    @Column
    private String email;
    @ApiField("年龄段")
    @Column
    private String age;
    @Transient
    private String ageName;
    @ApiField("职位")
    @Column
    private String duty;
    @ApiField("工作单位")
    @Column
    private String company;

    @ApiField("教育程度")
    @Column
    private String education;
    @Transient
    private String educationName;

    @ApiField("财产规模")
    @Column
    private String money;
    @Transient
    private String moneyName;
    @ApiField("婚姻状况")
    @Column
    private String marriage;
    @Transient
    private String marriageName;
    @ApiField("车位1")
    @Column
    private String carSite1;
    @ApiField("车位2")
    @Column
    private String carSite2;
    @ApiField("车牌号")
    @Column
    private String carNo;
    @ApiField("车型")
    @Column
    private String carType;
    @ApiField("家庭人口")
    @Column
    private Integer familyCounts;
    @ApiField("备注")
    @Column
    private String cusDescription;

    // ===============  数据字典   ===================

    @Transient
    @ApiField("状态名称")
    private String statusName;
    @Transient
    @ApiField("房屋使用类型名称")
    private String houseUseTypeName;
    @Transient
    @ApiField("产权性质名称")
    private String housePropertyName;
    @Transient
    @ApiField("朝向名称")
    private String orientName;
    @Column
    private String modifierId;

    public Boolean getOnRent() {
        return onRent;
    }

    public void setOnRent(Boolean onRent) {
        this.onRent = onRent;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getMarriageName() {
        return marriageName;
    }

    public void setMarriageName(String marriageName) {
        this.marriageName = marriageName;
    }

    public String getCarSite1() {
        return carSite1;
    }

    public void setCarSite1(String carSite1) {
        this.carSite1 = carSite1;
    }

    public String getCarSite2() {
        return carSite2;
    }

    public void setCarSite2(String carSite2) {
        this.carSite2 = carSite2;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public Integer getFamilyCounts() {
        return familyCounts;
    }

    public void setFamilyCounts(Integer familyCounts) {
        this.familyCounts = familyCounts;
    }

    public String getCusDescription() {
        return cusDescription;
    }

    public void setCusDescription(String cusDescription) {
        this.cusDescription = cusDescription;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public String getCusWechat() {
        return cusWechat;
    }

    public void setCusWechat(String cusWechat) {
        this.cusWechat = cusWechat;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getHouseUseTypeName() {
        return houseUseTypeName;
    }

    public void setHouseUseTypeName(String houseUseTypeName) {
        this.houseUseTypeName = houseUseTypeName;
    }

    public String getHousePropertyName() {
        return housePropertyName;
    }

    public void setHousePropertyName(String housePropertyName) {
        this.housePropertyName = housePropertyName;
    }

    public String getOrientName() {
        return orientName;
    }

    public void setOrientName(String orientName) {
        this.orientName = orientName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
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

    public String getHouseUseType() {
        return houseUseType;
    }

    public void setHouseUseType(String houseUseType) {
        this.houseUseType = houseUseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
