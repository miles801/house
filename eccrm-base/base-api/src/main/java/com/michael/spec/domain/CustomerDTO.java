package com.michael.spec.domain;

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
    @Col(index = 3)
    private String marriage;
    @Col(index = 4)
    private Integer familyCounts;
    @Col(index = 5)
    private String phone1;
    @Col(index = 6)
    private String phone2;
    @Col(index = 7)
    private String phone3;
    @Col(index = 8)
    private String email;
    @Col(index = 9)
    private String wechat;
    @Col(index = 10)
    private String company;
    @Col(index = 11)
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
