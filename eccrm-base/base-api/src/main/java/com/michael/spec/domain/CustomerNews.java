package com.michael.spec.domain;

import com.michael.docs.annotations.ApiField;
import com.ycrl.base.common.CommonDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Michael
 */

@Entity
@Table(name = "spec_customer_news")
public class CustomerNews extends CommonDomain {

    /**
     * 跟进方式、最新动态类型
     */
    public static final String TYPE = "CUSTOMER_NEWS_TYPE";

    @NotNull
    @Column(length = 40, nullable = false)
    private String customerId;

    @NotNull
    @Column(length = 40, nullable = false)
    private String empId;
    @Column(length = 40)
    private String empName;
    @Column(length = 20)
    private String phone;

    @ApiField("跟进方式")
    @Column(length = 40)
    private String type;
    @ApiField("内容")
    @Column(length = 1000)
    private String content;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
