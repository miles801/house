package com.michael.spec.bo;

import com.michael.docs.annotations.ApiField;
import com.ycrl.core.hibernate.criteria.BO;
import com.ycrl.core.hibernate.criteria.Condition;
import com.ycrl.core.hibernate.criteria.LikeModel;
import com.ycrl.core.hibernate.criteria.MatchModel;

import java.util.Date;
import java.util.List;

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

    @ApiField("门牌号")
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

    @ApiField("户型-室")
    @Condition
    private String type1;
    @ApiField("户型-厅")
    @Condition
    private String type2;
    @ApiField("户型-厨")
    @Condition
    private String type3;
    @ApiField("户型-卫")
    @Condition
    private String type4;

    @Condition(matchMode = MatchModel.GE, target = "square")
    private Double square1;
    @Condition(matchMode = MatchModel.LT, target = "square")
    private Double square2;

    @Condition
    private String orient;

    @Condition(matchMode = MatchModel.IN, target = "orient")
    private String orients;

    @Condition(matchMode = MatchModel.IN, target = "status")
    private List<String> statusInclude;
    @Condition(matchMode = MatchModel.NOT_IN, target = "status")
    private List<String> statusExclude;

    // 创建时间
    @Condition(matchMode = MatchModel.GE, target = "createdDatetime")
    private Date createdDatetime1;
    @Condition(matchMode = MatchModel.LT, target = "createdDatetime")
    private Date createdDatetime2;

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String creatorName;

    // 如果值为true，则表示查询所有数据
    private Boolean manager;

    // 如果值为true，则表示只查询自己负责的楼盘的数据
    private Boolean master;

    @Condition
    private String customerId;

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public List<String> getStatusInclude() {
        return statusInclude;
    }

    public void setStatusInclude(List<String> statusInclude) {
        this.statusInclude = statusInclude;
    }

    public List<String> getStatusExclude() {
        return statusExclude;
    }

    public void setStatusExclude(List<String> statusExclude) {
        this.statusExclude = statusExclude;
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

    public Date getCreatedDatetime1() {
        return createdDatetime1;
    }

    public void setCreatedDatetime1(Date createdDatetime1) {
        this.createdDatetime1 = createdDatetime1;
    }

    public Date getCreatedDatetime2() {
        return createdDatetime2;
    }

    public void setCreatedDatetime2(Date createdDatetime2) {
        this.createdDatetime2 = createdDatetime2;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
}
