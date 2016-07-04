package com.michael.spec.service;

import java.util.List;

/**
 * 用于存放楼盘的维护人信息
 *
 * @author Michael
 */
public class BuildingEmp {
    private String id;
    private List<String> empIds;
    private List<String> empNames;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getEmpIds() {
        return empIds;
    }

    public void setEmpIds(List<String> empIds) {
        this.empIds = empIds;
    }

    public List<String> getEmpNames() {
        return empNames;
    }

    public void setEmpNames(List<String> empNames) {
        this.empNames = empNames;
    }
}
