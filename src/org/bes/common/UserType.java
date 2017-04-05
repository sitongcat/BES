package org.bes.common;


public enum UserType {

	ADMIN("0"),
	DEPTADMIN("1"),
	NORMAL("2");

	private String type;

	private UserType(String type) {
		this.type = type;
	}

	public static UserType getType(String type) {
		if (type == null) {
			return NORMAL;
		}
		switch (type) {
			case "0": return ADMIN;
			case "1": return DEPTADMIN;
			default: return NORMAL;
		}
	}

	public String toString() {
		return this.type;
	}

}
