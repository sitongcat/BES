package org.bes.model.dto;


import org.bes.model.entity.Dept;
import org.bes.model.entity.User;

public class LoginInfo {

	private User user;
	private Dept dept;

	private boolean admin;
	private boolean financial;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isFinancial() {
		return financial;
	}

	public void setFinancial(boolean financial) {
		this.financial = financial;
	}
}
