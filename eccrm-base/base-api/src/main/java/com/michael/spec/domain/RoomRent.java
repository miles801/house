package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Michael
 */

@Entity
@Table(name = "spec_room_rent")
public class RoomRent extends CommonDomain {

    @NotNull(message = "租赁的房屋ID不能为空!")
    @Column(length = 40, nullable = false)
    private String roomId;
    @ApiField("房屋编号")
    @Column(length = 40)
    private String roomKey;
    @ApiField("原租户，用于转租的情况")
    @Column(length = 40)
    private String originCustomerId;
    @Column(length = 40)
    private String originCustomerName;

    @NotNull(message = "新租户不能为空！")
    @Column(nullable = false, length = 40)
    private String newCustomerId;
    @Column(length = 40)
    private String newCustomerName;

    @ApiField("交易时间")
    @Column
    private Date occurDate;

    @ApiField("租金")
    @Column
    private Double price;
    @ApiField("到期日")
    @Column
    private Date endDate;

    @ApiField("租赁用途")
    @Column(length = 40)
    private String rentUsage;

    @ApiField("公司名称")
    @Column(length = 100)
    private String company;

    @ApiField("是否完成交易")
    @Column
    private Boolean finish;

    @ApiField("描述")
    @Column(length = 1000)
    private String description;

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOriginCustomerId() {
        return originCustomerId;
    }

    public void setOriginCustomerId(String originCustomerId) {
        this.originCustomerId = originCustomerId;
    }

    public String getOriginCustomerName() {
        return originCustomerName;
    }

    public void setOriginCustomerName(String originCustomerName) {
        this.originCustomerName = originCustomerName;
    }

    public String getNewCustomerId() {
        return newCustomerId;
    }

    public void setNewCustomerId(String newCustomerId) {
        this.newCustomerId = newCustomerId;
    }

    public String getNewCustomerName() {
        return newCustomerName;
    }

    public void setNewCustomerName(String newCustomerName) {
        this.newCustomerName = newCustomerName;
    }

    public Date getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRentUsage() {
        return rentUsage;
    }

    public void setRentUsage(String rentUsage) {
        this.rentUsage = rentUsage;
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
