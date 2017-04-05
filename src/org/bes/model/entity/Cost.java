package org.bes.model.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "cost")
@DynamicInsert
public class Cost {

	@Id
	@Column(name = "id", length = 40)
	private String id;
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
	@Column(name = "cost", columnDefinition = "double(12,2) DEFAULT 0.0")
	private double cost;
	@Column(name = "occurrenceTime", length = 20, columnDefinition = "dateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date occurrenceTime;
	@Column(name = "audit", columnDefinition = "varchar(1) default 1")
	private String audit;
	@Column(name = "auditTime", length = 20, columnDefinition = "dateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date auditTime;
	@Transient
	private String ownerDeptId;
	@Transient
	private String ownerDeptName;
	@Transient
	private String ownerId;
	@Transient
	private String ownerName;

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

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Date getOccurrenceTime() {
		return occurrenceTime;
	}

	public void setOccurrenceTime(Date occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getOwnerDeptId() {
		return ownerDeptId;
	}

	public void setOwnerDeptId(String ownerDeptId) {
		this.ownerDeptId = ownerDeptId;
	}

	public String getOwnerDeptName() {
		return ownerDeptName;
	}

	public void setOwnerDeptName(String ownerDeptName) {
		this.ownerDeptName = ownerDeptName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
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
