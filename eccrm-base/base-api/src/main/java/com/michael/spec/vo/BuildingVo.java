package com.michael.spec.vo;

import com.michael.spec.domain.Building;

/**
 * @author Michael
 */
public class BuildingVo extends Building {

    private String typeName;
    private String houseTypeName;
    private String warmTypeName;
    private String usagesName;
    private String subwayName;

    // 所有房屋
    private Long allRooms;

    // 有效房屋
    private Long validRooms;

    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Long getAllRooms() {
        return allRooms;
    }

    public void setAllRooms(Long allRooms) {
        this.allRooms = allRooms;
    }

    public Long getValidRooms() {
        return validRooms;
    }

    public void setValidRooms(Long validRooms) {
        this.validRooms = validRooms;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getHouseTypeName() {
        return houseTypeName;
    }

    public void setHouseTypeName(String houseTypeName) {
        this.houseTypeName = houseTypeName;
    }

    public String getWarmTypeName() {
        return warmTypeName;
    }

    public void setWarmTypeName(String warmTypeName) {
        this.warmTypeName = warmTypeName;
    }

    public String getUsagesName() {
        return usagesName;
    }

    public void setUsagesName(String usagesName) {
        this.usagesName = usagesName;
    }

    public String getSubwayName() {
        return subwayName;
    }

    public void setSubwayName(String subwayName) {
        this.subwayName = subwayName;
    }
}
