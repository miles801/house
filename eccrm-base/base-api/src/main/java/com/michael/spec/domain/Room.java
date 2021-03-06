package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 房间，实际就是房屋信息
 *
 * @author Michael
 */
@Entity
@Table(name = "spec_room")
public class Room extends CommonDomain {

    /**
     * 未录入
     */
    public static final String STATUS_INACTIVE = "INACTIVE";
    /**
     * 新增申请
     */
    public static final String STATUS_APPLY_ADD = "APPLY_ADD";
    /**
     * 修改申请
     */
    public static final String STATUS_APPLY_MODIFY = "APPLY_MODIFY";
    /**
     * 申请无效
     */
    public static final String STATUS_APPLY_INVALID = "APPLY_INVALID";
    /**
     * 无效电话
     */
    public static final String STATUS_INVALID = "INVALID";
    /**
     * 新增无效
     */
    public static final String STATUS_INVALID_ADD = "INVALID_ADD";
    /**
     * 正常
     */
    public static final String STATUS_ACTIVE = "ACTIVE";

    @ApiField("房屋编号")
    @Column(nullable = false, length = 40)
    private String roomKey;

    @NotNull
    @Column(length = 40, nullable = false)
    private String buildingId;

    @NotNull
    @Column(nullable = false, length = 40)
    private String blockId;

    @Column(nullable = false, length = 40)
    private String unitId;

    @ApiField("楼层")
    @NotNull(message = "楼层信息不能为空!")
    @Column(nullable = false)
    private Integer floor;

    @ApiField("门牌号")
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

    @ApiField("待租")
    @Column
    private Boolean onRent;
    @ApiField("待售")
    @Column
    private Boolean onSale;

    @Column(length = 40)
    @ApiField(value = "房屋现状", desc = "业务参数")
    private String houseUseType;

    @ApiField("描述")
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

    @ApiField("状态")
    @NotNull(message = "房屋状态不能为空!")
    @Column(length = 40, nullable = false)
    private String status;

    @ApiField("客户ID")
    @Column
    private String customerId;

    @ApiField("业主电话")
    @Column
    private String cusPhone;

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
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

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }
}
