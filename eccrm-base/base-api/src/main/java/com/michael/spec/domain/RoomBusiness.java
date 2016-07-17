package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 房屋交易记录
 *
 * @author Michael
 */
@Entity
@Table(name = "spec_room_business")
public class RoomBusiness extends CommonDomain {

    @NotNull(message = "交易的房屋ID不能为空!")
    @Column(length = 40, nullable = false)
    private String roomId;
    /**
     * 业务参数：交易类型
     */
    public static final String TYPE = "ROOM_BUSINESS_TYPE";

    @NotNull(message = "原客户不能为空！")
    @Column(nullable = false, length = 40)
    private String originCustomerId;
    @Column(length = 40)
    private String originCustomerName;
    @NotNull(message = "新客户不能为空！")
    @Column(nullable = false, length = 40)
    private String newCustomerId;
    @Column(length = 40)
    private String newCustomerName;

    @ApiField("交易时间")
    @Column
    private Date occurDate;
    @ApiField("交易类型")
    @Column(length = 40, nullable = false)
    private String type;

    @ApiField("成交价格")
    @Column
    private Double price;

    @ApiField("描述")
    @Column(length = 1000)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOriginCustomerName() {
        return originCustomerName;
    }

    public void setOriginCustomerName(String originCustomerName) {
        this.originCustomerName = originCustomerName;
    }

    public String getNewCustomerName() {
        return newCustomerName;
    }

    public void setNewCustomerName(String newCustomerName) {
        this.newCustomerName = newCustomerName;
    }

    public void setOriginCustomerId(String originCustomerId) {
        this.originCustomerId = originCustomerId;
    }

    public String getNewCustomerId() {
        return newCustomerId;
    }

    public void setNewCustomerId(String newCustomerId) {
        this.newCustomerId = newCustomerId;
    }

    public Date getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
