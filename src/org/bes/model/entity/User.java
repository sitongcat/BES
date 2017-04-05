package org.bes.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "id", length = 40, nullable = false)
	private String id;
	@Column(name = "userName", length = 20, nullable = false)
	private String userName;
	@Column(name = "password", length = 20, nullable = false)
	private String password;
	@Column(name = "name", length = 20, nullable = false)
	private String name;
	@Column(name = "deptId", length = 40, nullable = false)
	private String deptId;
	@Transient
	private String deptName;
	@Column(name = "type", length = 10, nullable = false)
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
