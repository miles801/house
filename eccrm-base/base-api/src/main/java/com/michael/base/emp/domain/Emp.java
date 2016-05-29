package com.michael.base.emp.domain;

import com.michael.docs.annotations.ApiField;
import eccrm.base.attachment.AttachmentSymbol;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 员工
 *
 * @author Michael
 */
@Entity
@Table(name = "s_user")
public class Emp implements AttachmentSymbol {

    public static String SALARY_CONF = "SALARY_CONF"; //岗位等级

    /**
     * 正常
     */
    public static final Integer STATUS_NORMAL = 0;
    /**
     * 锁定
     */
    public static final Integer STATUS_LOCKED = 1;
    /**
     * 离职
     */
    public static final Integer STATUS_LEAVE = 2;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "identity")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;


    @ApiField(value = "姓名", required = true)
    @Column(nullable = false, length = 40)
    private String name;

    @ApiField(value = "工号", desc = "唯一")
    @Column(length = 40, unique = true, name = "seat_number")
    private String code;

    @ApiField(value = "登录名", required = true, desc = "唯一")
    @Column(unique = true, length = 40, name = "login_name", updatable = false)
    private String loginName;

    // 密码
    @ApiField(value = "登录密码", desc = "使用MD5加密")
    @Column(nullable = false, length = 40, name = "login_pwd", updatable = false)
    private String password;

    @ApiField(value = "姓名的拼音（全拼）")
    @Column(length = 40, name = "pinyin")
    private String pinyin;

    @ApiField(value = "性别，业务参数，来自BP_SEX")
    @Column(length = 40)
    private String sex;

    // 开始工作时间
    @ApiField(value = "开始工作时间（工龄）")
//    @Column
    @Transient
    private Date workDate;

    @ApiField(value = "入职时间（司龄）")
//    @Column
    @Transient
    private Date joinDate;

    @ApiField(value = "离职时间")
//    @Column
    @Transient
    private Date leaveDate;

    @ApiField(value = "电话号码")
    @Column(length = 20)
    private String phone;

    @ApiField(value = "手机号码")
//    @Column(length = 20)
    @Transient
    private String mobile;

    @ApiField(value = "邮箱")
//    @Column(length = 100)
    @Transient
    private String email;

    @ApiField(value = "头像地址")
    @Column(name = "avator", length = 200)
    private String icon;

    @ApiField(value = "账号状态", desc = "0、正常；1、锁定；2、离职")
    @Column(name = "locked")
    private Integer locked;

    @ApiField(value = "所属组织机构ID")
    @Column(length = 40, name = "org_id")
    private String orgId;
    @ApiField(value = "所属组织机构名称")
//    @Column(length = 40)
    @Transient
    private String orgName;

    @ApiField(value = "岗位", desc = "来自业务参数:BP_DUTY")
    @Column(length = 40, name = "position_level")
    private String duty;

    @ApiField(value = "考勤编号", desc = "用于与考勤机中的用户ID对接")
    @Column(length = 40, name = "att_number")
    private String attendanceNo;

    @ApiField(value = "商务级别", desc = "用于与CRM对接使用")
    @Column(length = 32)
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAttendanceNo() {
        return attendanceNo;
    }

    public void setAttendanceNo(String attendanceNo) {
        this.attendanceNo = attendanceNo;
    }

    public String businessId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
