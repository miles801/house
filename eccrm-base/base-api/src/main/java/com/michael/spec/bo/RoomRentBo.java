package com.michael.spec.bo;

import com.ycrl.core.hibernate.criteria.BO;
import com.ycrl.core.hibernate.criteria.Condition;
import com.ycrl.core.hibernate.criteria.MatchModel;

import java.util.Date;

/**
 * @author Michael
 */
public class RoomRentBo implements BO {

    @Condition
    private String roomId;

    @Condition
    private String originCustomerId;
    @Condition
    private String newCustomerId;

    // 交易时间范围
    @Condition(matchMode = MatchModel.GE, target = "occurDate")
    private Date occurDate1;
    @Condition(matchMode = MatchModel.LT, target = "occurDate")
    private Date occurDate2;

    // 到期日期范围
    @Condition(matchMode = MatchModel.GE, target = "endDate")
    private Date endDate1;
    @Condition(matchMode = MatchModel.LT, target = "endDate")
    private Date endDate2;

    @Condition
    private Boolean finish;

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

    public Date getEndDate1() {
        return endDate1;
    }

    public void setEndDate1(Date endDate1) {
        this.endDate1 = endDate1;
    }

    public Date getEndDate2() {
        return endDate2;
    }

    public void setEndDate2(Date endDate2) {
        this.endDate2 = endDate2;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }
}
