package com.michael.base.org.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 * @author Michael
 */

/**
 * 组织机构
 *
 * @author Michael
 */
@Entity
@Table(name = "s_organization")
public class Org {

    // 名称
    @NotNull
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    // 编号，用于在新建用户时默认指定某个机构，可以为空，如果有值则必须唯一
    @Column(name = "org_code", length = 20, unique = true)
    private String code;
    // 全名（如果为空，则等于名称）
    @Column(length = 40)
    private String longName;
    // 拼音的首字母（便于搜索）
    @Column(length = 20)
    private String pinyin;
    // 上级
    @Column(length = 40, name = "pid")
    private String parentId;
    @Column(length = 40, name = "pname")
    private String parentName;
    // 排序号
    @Column(name = "sort")
    private Integer sequenceNo;
    // 路径（用于访问所有的子节点）
    @Column(name = "org_path")
    private String path;

    // 是否为父结构
    @Column(name = "is_parent")
    private Boolean isParent;

    // 层级（从0开始）
    @Column(name = "org_level")
    private Integer level;

    // 是否隐藏(是否禁用）
    @Column(name = "is_hide")
    private Boolean isHide;

    // 最小用户数
    @Column(name = "min_emp")
    private Integer minEmp;
    // 最大用户数
    @Column(name = "max_emp")
    private Integer maxEmp;

    // 职位
    @Column
    private String duty;

    // 当前机构下的员工数
    @Column
    private Integer empCounts;

    // 当前机构及子机构下的员工数量
    @Column
    private Integer totalEmpCounts;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "id", unique = true, nullable = false)
    private String id;


    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public Integer getEmpCounts() {
        return empCounts;
    }

    public void setEmpCounts(Integer empCounts) {
        this.empCounts = empCounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getTotalEmpCounts() {
        return totalEmpCounts;
    }

    public void setTotalEmpCounts(Integer totalEmpCounts) {
        this.totalEmpCounts = totalEmpCounts;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }


    public Boolean getHide() {
        return isHide;
    }

    public void setHide(Boolean hide) {
        isHide = hide;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getMinEmp() {
        return minEmp;
    }

    public void setMinEmp(Integer minEmp) {
        this.minEmp = minEmp;
    }

    public Integer getMaxEmp() {
        return maxEmp;
    }

    public void setMaxEmp(Integer maxEmp) {
        this.maxEmp = maxEmp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
