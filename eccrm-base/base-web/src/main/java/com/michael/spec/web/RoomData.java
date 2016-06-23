package com.michael.spec.web;

import com.michael.spec.domain.Customer;
import com.michael.spec.domain.RoomBusiness;

/**
 * @author Michael
 */
public class RoomData {
    private String roomId;
    private Customer customer;
    private RoomBusiness roomBusiness;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RoomBusiness getRoomBusiness() {
        return roomBusiness;
    }

    public void setRoomBusiness(RoomBusiness roomBusiness) {
        this.roomBusiness = roomBusiness;
    }
}
