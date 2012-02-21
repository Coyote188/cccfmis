package cccf.ma.model;

import java.math.*;
import openjframework.model.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.io.Serializable;

public class CommentItemInfo {
	private String id;
	private String title;
	private String content;
	private Date createdTime;
	private UserInfo user;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public void setId(String value) {
		this.id = value;
	}

	public void setContent(String value) {
		this.content = value;
	}

	public void setUser(UserInfo value) {
		this.user = value;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public UserInfo getUser() {
		return user;
	}

}