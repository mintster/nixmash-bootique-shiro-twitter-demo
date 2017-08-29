package com.nixmash.shiro.models;

import java.io.Serializable;

public class Msg implements Serializable {

	private static final long serialVersionUID = -273923523991995637L;

	private String message;
	private Boolean hasMessage;

	public Msg() {
	}

	public Msg(String message) {
		this.message = message;
	}

	public Msg(Boolean hasMessage) {
		this.hasMessage = hasMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getHasMessage() {
		return hasMessage;
	}

	public void setHasMessage(Boolean hasMessage) {
		this.hasMessage = hasMessage;
	}

	@Override
	public String toString() {
		return "Msg{" +
				"message='" + message + '\'' +
				", hasMessage=" + hasMessage +
				'}';
	}
}
