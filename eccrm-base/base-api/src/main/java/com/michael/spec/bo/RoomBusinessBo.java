package com.michael.spec.bo;

import com.ycrl.core.hibernate.criteria.BO;
import com.ycrl.core.hibernate.criteria.Condition;
import com.ycrl.core.hibernate.criteria.LikeModel;
import com.ycrl.core.hibernate.criteria.MatchModel;

import java.util.Date;

/**
 * @author Michael
 */
public class RoomBusinessBo implements BO {

    @Condition
    private String roomId;
    @Condition
    private String originCustomerId;
    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String originCustomerName;
    @Condition
    private String newCustomerId;

    @Condition(matchMode = MatchModel.LIKE, likeMode = LikeModel.ANYWHERE)
    private String newCustomerName;
    @Condition
    private String type;

    @Condition(matchMode = MatchModel.GE, target = "price")
    private Double price1;
    @Condition(matchMode = MatchModel.LT, target = "price")
    private Double price12;

    @Condition(matchMode = MatchModel.GE, target = "occurDate")
    private Date occurDate1;
    @Condition(matchMode = MatchModel.LT, target = "occurDate")
    private Date occurDate2;

    public Date getOccurDate1() {
        return occurDate1;
    }

    public void setOccurDate1(Date occurDate1) {
        this.occurDate1 = occurDate1;
    }

    public Date getOccurDate2() {
        return occurDate2;
    }

    public void setOccurDate2(Date occurDate2) {
        this.occurDate2 = occurDate2;
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

    public String getNewCustomerId() {
        return newCustomerId;
    }

    public void setNewCustomerId(String newCustomerId) {
        this.newCustomerId = newCustomerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice1() {
        return price1;
    }

    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

    public Double getPrice12() {
        return price12;
    }

    public void setPrice12(Double price12) {
        this.price12 = price12;
    }
}
