package com.nixmash.shiro.models;

import java.io.Serializable;

public class Role implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 572257273198993868L;

	private Integer id;
	private String permission;
	private String roleName;

	public Role() {
	}

	public Role(String permission, String roleName) {
		this.permission = permission;
		this.roleName = roleName;
	}

	public Integer getId() {
		return id;
	}

	public String getPermission() {
		return permission;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", permission='" + permission + '\'' +
				", roleName='" + roleName + '\'' +
				'}';
	}
}
