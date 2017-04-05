package org.bes.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "budget")
public class Budget {

	@Id
	@Column(name = "id", length = 40)
	private String id;
	@Column(name = "cost", columnDefinition = "double(12,2) default 0")
	private double cost;
	@Column(name = "categoryId", length = 40)
	private String categoryId;
	@Transient
	private String categoryCode;
	@Transient
	private String categoryName;
	@Column(name = "deptId", length = 40)
	private String deptId;
	@Transient
	private String deptName;
	@Column(name = "time", length = 20, columnDefinition = "dateTime")
	@Temporal(TemporalType.DATE)
	private Date time;
	@Transient
	private String ownerDeptId;

	@Transient
	private String startTime;
	@Transient
	private String endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getOwnerDeptId() {
		return ownerDeptId;
	}

	public void setOwnerDeptId(String ownerDeptId) {
		this.ownerDeptId = ownerDeptId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
