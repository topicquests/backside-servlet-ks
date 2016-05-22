/*
 * Copyright 2015, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.backside.servlet.apps.usr.api;

/**
 * @author park
 *
 */
public interface IUserSchema {
	public static final String 
		USER_EMAIL		="email",
		USER_NAME		="name",
		USER_FULLNAME	= "fullName",
		USER_PASSWORD	= "pwd",
		USER_AVATAR		= "avatar",
		USER_ROLE		= "role",
		USER_GEOLOC		= "geoloc",
		USER_HOMEPAGE	= "homepage";
	
		
	
	public static final String [] TABLES = {
			"CREATE TABLE users ("
			+ "email VARCHAR(255) PRIMARY KEY,"
			+ "pwd VARCHAR(128) NOT NULL,"
			+ "name VARCHAR(128) NOT NULL,"
			+ "fullName VARCHAR(128) NOT NULL)",
			//a rule can be a list of roles, each a short string
			//the first role is usually the primary role, e.g. USER_ROLE from ISecurity
		//	+ "role VARCHAR(255),"
		//	+ "avatar VARCHAR(128))",
			"CREATE TABLE userprops ("
			+ "name VARCHAR(255) NOT NULL,"
			+ "prop VARCHAR(16) NOT NULL,"
			+ "val VARCHAR(128) NOT NULL)",
			"CREATE INDEX userindex ON users(email)",
			"CREATE INDEX propindex ON userprops(name)"};
	
	public static final String getUserByEmail =
			"SELECT * FROM users WHERE lower(email)=lower(?)";
	public static final String getUserByName =
			"SELECT * FROM users WHERE lower(name)=lower(?)";
	public static final String removeUser =
			"DELETE FROM users WHERE name=?";
	public static final String updateUserPwd = 
			"UPDATE users  SET pwd=? WHERE name=?";
	public static final String updateUserRole = 
			"UPDATE users  SET role=? WHERE name=?";
	public static final String updateUserEmail = 
			"UPDATE users  SET email=? WHERE name=?";
	
	public static final String getUserProperties =
			"SELECT * FROM userprops WHERE lower(name)=lower(?)";
	
	public static final String updateUserProperty =
			"UPDATE userprops  SET val=? WHERE prop=? AND name=?";
	public static final String putUser =
			"INSERT INTO users values(?, ?, ?, ?)";
	public static final String putUserProperty=
			"INSERT INTO userprops values(?,?,?)";
	
	public static final String listUserNames =
			"SELECT name FROM users";
	
	  public final String listUsers =
			  "SELECT * FROM users ";

	  public final String listUsersLimited =
			  "SELECT * FROM users LIMIT ? OFFSET ?";

	//TODO more
	public static final String authenticate =
			"select * FROM users WHERE email=? AND pwd=?";
}
