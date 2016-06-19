package com.michael.spec.bo;

import com.michael.docs.annotations.ApiField;
import com.ycrl.core.hibernate.criteria.BO;
import com.ycrl.core.hibernate.criteria.Condition;
import com.ycrl.core.hibernate.criteria.LikeModel;
import com.ycrl.core.hibernate.criteria.MatchModel;

/**
 * @author Michael
 */
public class RoomBo implements BO {

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String roomKey;

    @Condition
    private String buildingId;
    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)

    private String buildingName;
    @Condition
    private String blockId;
    @Condition
    private String blockCode;

    @Condition
    private String unitId;
    @Condition
    private String unitCode;

    @Condition
    private Integer floor;

    @Condition
    private String code;

    @ApiField("房屋现状")
    @Condition
    private String houseUseType;

    @ApiField("房屋状态")
    @Condition
    private String status;

    @ApiField("产权性质")
    @Condition
    private String houseProperty;

    @Condition
    private String type;

    @Condition(matchMode = MatchModel.GE, target = "square")
    private Double square1;
    @Condition(matchMode = MatchModel.LT, target = "square")
    private Double square2;

    @Condition
    private String orient;

    @Condition(matchMode = MatchModel.IN, target = "orient")
    private String orients;

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

    public String getHouseUseType() {
        return houseUseType;
    }

    public void setHouseUseType(String houseUseType) {
        this.houseUseType = houseUseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHouseProperty() {
        return houseProperty;
    }

    public void setHouseProperty(String houseProperty) {
        this.houseProperty = houseProperty;
    }

    public Double getSquare1() {
        return square1;
    }

    public void setSquare1(Double square1) {
        this.square1 = square1;
    }

    public Double getSquare2() {
        return square2;
    }

    public void setSquare2(Double square2) {
        this.square2 = square2;
    }

    public String getOrients() {
        return orients;
    }

    public void setOrients(String orients) {
        this.orients = orients;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }
}
