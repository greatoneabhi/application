package com.application.entity;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import scala.Serializable;

@PrimaryKeyClass
public class UserKey implements Serializable {
	
	private static final long serialVersionUID = -2876358577607368207L;

	@PrimaryKeyColumn(name = "user_name", type = PrimaryKeyType.PARTITIONED)
	private String username;
	
	@PrimaryKeyColumn(name = "email_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private String emailId;
	
	public UserKey() {
		super();
	}
	
	public UserKey(final String username, final String emailId) {
		this.username = username;
		this.emailId = emailId;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
