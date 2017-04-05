package org.bes.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "costcategory")
public class CostCategory {

	@Id
	@Column(name = "id", length = 40)
	private String id;
	@Column(name = "code", length = 40)
	private String code;
	@Column(name = "name", length = 200)
	private String name;
	@Column(name = "deptId", length = 40)
	private String deptId;
	@Transient
	private String deptName;
	@Column(name = "parentId", length = 40)
	private String parentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
