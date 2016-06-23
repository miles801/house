package com.michael.spec.vo;

import com.michael.spec.domain.RoomBusiness;

/**
 * @author Michael
 */
public class RoomBusinessVo extends RoomBusiness {

    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
