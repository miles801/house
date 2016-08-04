package com.michael.spec.vo;

import com.michael.spec.domain.Customer;

/**
 * @author Michael
 */
public class CustomerVo extends Customer {

    private String sexName;
    private String educationName;
    private String ageName;
    private String moneyName;
    private String marriageName;
    private String statusName;
    private String typeName;
    // 客户名下房产编号
    private String roomKeys;

    // 在租房屋编号
    private String rentKeys;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRentKeys() {
        return rentKeys;
    }

    public void setRentKeys(String rentKeys) {
        this.rentKeys = rentKeys;
    }

    public String getRoomKeys() {
        return roomKeys;
    }

    public void setRoomKeys(String roomKeys) {
        this.roomKeys = roomKeys;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getEducationName() {
        return educationName;
    }

    public void setEducationName(String educationName) {
        this.educationName = educationName;
    }

    public String getAgeName() {
        return ageName;
    }

    public void setAgeName(String ageName) {
        this.ageName = ageName;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getMarriageName() {
        return marriageName;
    }

    public void setMarriageName(String marriageName) {
        this.marriageName = marriageName;
    }
}
