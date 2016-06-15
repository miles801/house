package com.michael.spec.bo;

import com.ycrl.core.hibernate.criteria.BO;
import com.ycrl.core.hibernate.criteria.Condition;

/**
 * @author Michael
 */
public class UnitBo implements BO {

    @Condition
    private String blockId;

    @Condition
    private String code;

    @Condition
    private String type;

    @Condition
    private String orient;

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrient() {
        return orient;
    }

    public void setOrient(String orient) {
        this.orient = orient;
    }
}
