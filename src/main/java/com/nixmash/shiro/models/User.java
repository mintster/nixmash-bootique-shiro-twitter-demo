package com.nixmash.shiro.models;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -4746333924452133573L;

	private Long userId;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private Boolean enabled;
	private String providerUserid;

	public User() {
	}

	public User(String username, String email, String firstName, String lastName, String password, String providerUserid) {
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.providerUserid = providerUserid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getProviderUserid() {
		return providerUserid;
	}

	public void setProviderUserid(String providerUserid) {
		this.providerUserid = providerUserid;
	}

	@Override
	public String toString() {
		return "User{" +
				"user_id=" + userId +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", password='" + password + '\'' +
				", providerUserid='" + providerUserid + '\'' +
				", enabled=" + enabled +
				'}';
	}
}
