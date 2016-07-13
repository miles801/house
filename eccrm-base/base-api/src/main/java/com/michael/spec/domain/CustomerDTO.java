package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.michael.poi.annotation.Col;
import com.michael.poi.annotation.ImportConfig;
import com.michael.poi.core.DTO;

/**
 * 客户数据导入
 *
 * @author Michael
 */
@ImportConfig(file = "", startRow = 2)
public class CustomerDTO implements DTO {
    @Col(index = 0)
    private String name;

    @Col(index = 1)
    private String idCard;
    @Col(index = 2)
    private String sex;
    @ApiField("年龄段")
    @Col(index = 3)
    private String age;
    @ApiField("婚姻状况")
    @Col(index = 4)
    private String marriage;
    @Col(index = 5)
    private Integer familyCounts;
    @Col(index = 6)
    private String phone1;
    @Col(index = 7)
    private String phone2;
    @Col(index = 8)
    private String phone3;
    @Col(index = 9)
    private String email;
    @Col(index = 10)
    private String wechat;

    @ApiField("职业")
    @Col(index = 11)
    private String duty;
    @ApiField("工作单位")
    @Col(index = 12)
    private String company;
    @ApiField("学历")
    @Col(index = 13)
    private String education;
    @ApiField("资产规模")
    @Col(index = 14)
    private String money;
    @ApiField("车位1")
    @Col(index = 15)
    private String carSite1;
    @ApiField("车位2")
    @Col(index = 16)
    private String carSite2;
    @ApiField("车牌号")
    @Col(index = 17)
    private String carNo;
    @ApiField("车型")
    @Col(index = 18)
    private String carType;
    @Col(index = 19)
    private String c1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public Integer getFamilyCounts() {
        return familyCounts;
    }

    public void setFamilyCounts(Integer familyCounts) {
        this.familyCounts = familyCounts;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
}
