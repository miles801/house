package com.michael.spec.domain;

import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * 房屋关注
 *
 * @author Michael
 */

@Entity
@Table(name = "spec_room_star")
public class RoomStar extends CommonDomain {
    @NotNull
    @Column(length = 40, nullable = false)
    private String roomId;
    @NotNull
    @Column(length = 40, nullable = false)
    private String empId;

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
}
