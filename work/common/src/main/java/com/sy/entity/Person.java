package com.sy.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "person", schema = "qlzh2", catalog = "")
public class Person {
    private int id;

    private String name;

    private String sex;

    private Integer age;

    @Transient
    private Integer deptId;

    private Dept dept;

    @Transient
    private Integer roleId;

    private Role role;

    private String phone;

    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hiredate;

    private String email;

    private String workType;

    private Integer skillLevel;

    private Integer pileCounts;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    private String remark;

    public Person() {
    }

    public Person(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Dept.class)
    @JoinColumn(name = "dept_id")
    @Fetch(FetchMode.SELECT)
    public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	@ManyToOne(targetEntity = Role.class)
    @JoinColumn(name = "role_id")
    @Fetch(FetchMode.SELECT)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Transient
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Transient
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "hiredate")
    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "work_type")
    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    @Basic
    @Column(name = "skill_level")
    public Integer getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }

    @Basic
    @Column(name = "pile_counts")
    public Integer getPileCounts() {
        return pileCounts;
    }

    public void setPileCounts(Integer pileCounts) {
        this.pileCounts = pileCounts;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public Person(int id, String name, String sex, Integer age, Integer deptId, Integer roleId, String phone,
			String password, Date birthday, Date hiredate, String email, String workType, Integer skillLevel,
			Integer pileCounts, String status, Timestamp createTime, Timestamp updateTime, String remark) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.deptId = deptId;
		this.roleId = roleId;
		this.phone = phone;
		this.password = password;
		this.birthday = birthday;
		this.hiredate = hiredate;
		this.email = email;
		this.workType = workType;
		this.skillLevel = skillLevel;
		this.pileCounts = pileCounts;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.remark = remark;
	}

    @Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", sex=" + sex + ", age=" + age + ", deptId=" + deptId
				+ ", roleId=" + roleId + ", phone=" + phone + ", password=" + password + ", birthday=" + birthday
				+ ", hiredate=" + hiredate + ", email=" + email + ", workType=" + workType + ", skillLevel="
				+ skillLevel + ", pileCounts=" + pileCounts + ", status=" + status + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", remark=" + remark + "]";
	}



}
