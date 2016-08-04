package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 客户/业主
 *
 * @author Michael
 */
@Entity
@Table(name = "spec_customer")
public class Customer extends CommonDomain {

    /**
     * 业务参数：年龄段
     */
    public static final String AGE_STAGE = "AGE_STAGE";
    /**
     * 业务参数：财产规模
     */
    public static final String MONEY_STAGE = "MONEY_STAGE";
    /**
     * 客户类型
     */
    public static final String TYPE = "CUSTOMER_TYPE";

    @NotNull(message = "客户编号不能为空!")
    @Column(length = 40, nullable = false)
    private String code;

    @ApiField("客户类型")
    @Column(length = 40)
    private String type;

    @ApiField("姓名")
    @Column(length = 40)
    private String name;

    @ApiField("性别")
    @Column(length = 40)
    private String sex;
    @ApiField("身份证号码")
    @Column(length = 20)
    private String idCard;
    @NotNull(message = "客户主电话号码不能为空!")
    @ApiField("电话1")
    @Column(length = 20, nullable = false)
    private String phone1;
    @ApiField("电话2")
    @Column(length = 20)
    private String phone2;
    @ApiField("电话3")
    @Column(length = 20)
    private String phone3;
    @ApiField("邮箱")
    @Column(length = 100)
    private String email;
    @ApiField("微信")
    @Column(length = 40)
    private String wechat;
    @ApiField("年龄段")
    @Column(length = 40)
    private String age;
    @ApiField("职位")
    @Column(length = 40)
    private String duty;
    @ApiField("工作单位")
    @Column(length = 100)
    private String company;

    @ApiField("教育程度")
    @Column(length = 40)
    private String education;

    @ApiField("财产规模")
    @Column(length = 40)
    private String money;
    @ApiField("婚姻状况")
    @Column(length = 40)
    private String marriage;
    @ApiField("车位1")
    @Column(length = 40)
    private String carSite1;
    @ApiField("车位2")
    @Column(length = 40)
    private String carSite2;
    @ApiField("车牌号")
    @Column(length = 40)
    private String carNo;
    @ApiField("车型")
    @Column(length = 40)
    private String carType;
    @ApiField("家庭人口")
    @Column
    private Integer familyCounts;
    @ApiField("备注")
    @Column(length = 1000)
    private String description;
    @Column(length = 100)
    private String c1;
    @Column(length = 100)
    private String c2;
    @Column(length = 100)
    private String c3;
    @Column(length = 100)
    private String c4;
    @Column(length = 100)
    private String c5;
    @Column(length = 100)
    private String c6;
    @Column(length = 100)
    private String c7;
    @Column(length = 100)
    private String c8;
    @Column(length = 100)
    private String c9;
    @Column(length = 100)
    private String c10;

    @ApiField("房屋套数")
    @Column
    private Integer roomCounts;

    @ApiField("楼盘ID")
    @Column(length = 40)
    private String buildingId;
    @ApiField("楼盘名称")
    @Column(length = 40)
    private String buildingName;

    /**
     * @see com.michael.spec.service.HouseParams#HOUSE_STATUS
     * @see Room
     */
    @ApiField("状态，同房屋状态")
    @Column(length = 40, nullable = false)
    private String status;

    @ApiField("是否为租户")
    @Column
    private Boolean rent;

    public Boolean getRent() {
        return rent;
    }

    public void setRent(Boolean rent) {
        this.rent = rent;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Integer getRoomCounts() {
        return roomCounts;
    }

    public void setRoomCounts(Integer roomCounts) {
        this.roomCounts = roomCounts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
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

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }

    public String getC5() {
        return c5;
    }

    public void setC5(String c5) {
        this.c5 = c5;
    }

    public String getC6() {
        return c6;
    }

    public void setC6(String c6) {
        this.c6 = c6;
    }

    public String getC7() {
        return c7;
    }

    public void setC7(String c7) {
        this.c7 = c7;
    }

    public String getC8() {
        return c8;
    }

    public void setC8(String c8) {
        this.c8 = c8;
    }

    public String getC9() {
        return c9;
    }

    public void setC9(String c9) {
        this.c9 = c9;
    }

    public String getC10() {
        return c10;
    }

    public void setC10(String c10) {
        this.c10 = c10;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
