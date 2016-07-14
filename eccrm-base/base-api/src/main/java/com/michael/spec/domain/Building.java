package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 楼盘信息
 *
 * @author Michael
 */

@Entity
@Table(name = "spec_building")
public class Building extends CommonDomain {

    // 城市
    @Column(length = 40)
    private String city;
    @Column(length = 40)
    private String cityName;
    // 区域
    @Column(length = 40)
    private String area;
    @Column(length = 40)
    private String areaName;
    // 楼盘名称
    @NotNull(message = "楼盘名称不能为空!")
    @Column(length = 40, nullable = false)
    private String name;
    // 楼盘地址
    @Column(length = 100)
    private String address;
    // 建筑年份
    @Column
    private String year;
    // 建筑类型
    @Column(length = 40)
    private String type;
    // 房屋权属
    @Column(length = 40)
    private String houseType;
    // 用途
    @Column(length = 40)
    private String usages;
    // 供暖类型
    @Column(length = 40)
    private String warmType;

    // 物业费
    @Column
    private Float propertyPrice;
    // 物业公司
    @Column(length = 40)
    private String propertyName;
    // 物业电话
    @Column(length = 20)
    private String propertyPhone;
    // 开发商
    @Column(length = 40)
    private String developerName;
    // 开发商电话
    @Column(length = 20)
    private String developerPhone;

    // 楼栋总数
    @Column
    private Integer buildingCounts;

    // 实际楼栋总数
    @Column
    private Integer realCounts;

    // 总户数
    @Column
    private Integer houseCounts;
    // 车位数
    @Column
    private Integer carCounts;
    // 地铁距离
    @Column(length = 40)
    private String subway;
    // 过户指导价格
    @Column
    private Float price;

    // 均价
    @Column
    private Float avgPrice;
    // 容积率
    @Column
    private Float containPercent;
    // 绿化率
    @Column
    private Float greenPercent;

    @NotNull(message = "负责人不能为空")
    @Column(nullable = false, length = 40)
    private String masterId;
    @Column(nullable = false, length = 40)
    private String masterName;

    @ApiField("维护人")
    @Column(length = 1000)
    private String maintainId;
    @Column(length = 500)
    private String maintainName;

    // 学区
    @Column(length = 100)
    private String school;
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


    // 状态：未启用、启用、注销
    @Column(length = 40)
    private String status;

    public Integer getRealCounts() {
        return realCounts;
    }

    public void setRealCounts(Integer realCounts) {
        this.realCounts = realCounts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Float avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getUsages() {
        return usages;
    }

    public void setUsages(String usages) {
        this.usages = usages;
    }

    public String getWarmType() {
        return warmType;
    }

    public void setWarmType(String warmType) {
        this.warmType = warmType;
    }

    public Float getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(Float propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyPhone() {
        return propertyPhone;
    }

    public void setPropertyPhone(String propertyPhone) {
        this.propertyPhone = propertyPhone;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getDeveloperPhone() {
        return developerPhone;
    }

    public void setDeveloperPhone(String developerPhone) {
        this.developerPhone = developerPhone;
    }

    public Integer getBuildingCounts() {
        return buildingCounts;
    }

    public void setBuildingCounts(Integer buildingCounts) {
        this.buildingCounts = buildingCounts;
    }

    public Integer getHouseCounts() {
        return houseCounts;
    }

    public void setHouseCounts(Integer houseCounts) {
        this.houseCounts = houseCounts;
    }

    public Integer getCarCounts() {
        return carCounts;
    }

    public void setCarCounts(Integer carCounts) {
        this.carCounts = carCounts;
    }

    public String getSubway() {
        return subway;
    }

    public void setSubway(String subway) {
        this.subway = subway;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getContainPercent() {
        return containPercent;
    }

    public void setContainPercent(Float containPercent) {
        this.containPercent = containPercent;
    }

    public Float getGreenPercent() {
        return greenPercent;
    }

    public void setGreenPercent(Float greenPercent) {
        this.greenPercent = greenPercent;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

    public String getMaintainId() {
        return maintainId;
    }

    public void setMaintainId(String maintainId) {
        this.maintainId = maintainId;
    }

    public String getMaintainName() {
        return maintainName;
    }

    public void setMaintainName(String maintainName) {
        this.maintainName = maintainName;
    }
}
