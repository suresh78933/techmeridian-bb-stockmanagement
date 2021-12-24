package com.techmeridian.stockmanagement.common;

public enum RoleEnum {

	SYSTEM_ADMIN(1), ADMIN(2), MANAGER(3), USER(4);

	private int role;

	public int getRoleId() {
		return this.role;
	}

	private RoleEnum(int role) {
		this.role = role;
	}
}
