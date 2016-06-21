package com.michael.spec.bo;

import com.michael.docs.annotations.ApiField;
import com.ycrl.core.hibernate.criteria.BO;
import com.ycrl.core.hibernate.criteria.Condition;

/**
 * @author Michael
 */
public class RoomNewsBo implements BO {

    @Condition
    private String roomId;

    @Condition
    private String empId;
    @Condition
    private String empName;

    @ApiField("跟进方式")
    @Condition
    private String type;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
