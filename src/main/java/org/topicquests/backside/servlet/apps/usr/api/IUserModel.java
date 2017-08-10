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

import org.topicquests.backside.servlet.api.ISecurity;
import org.topicquests.support.api.IResult;

/**
 * @author park
 */
public interface IUserModel {

	/**
	 * Authenticate this user. Return <code>null</code> if doesn't authenticate
	 *
	 * @param email
	 * @param password
	 * @return Ticket can return <code>null</code>
	 */
	IResult authenticate(String email, String password);

	/**
	 * Can return <code>null</code> inside Return object
	 *
	 * @param userEmail
	 * @return
	 */
	IResult getTicketByEmail(String userEmail);

	IResult getTicketById(String userId);

	IResult getTicketByHandle(String userHandle);

	/**
	 * <p>
	 * Throws an exception if user already exists. Should
	 * use <code>existsUsername</code> first
	 * </p>
	 *
	 * @param email
	 * @param userHandle
	 * @param userId
	 * @param password
	 * @param userFullName
	 * @param avatar
	 * @param role         based on {@link ISecurity} roles
	 * @param homepage
	 * @param geolocation
	 * @param addTopic     <code>false</code> only for default administrator
	 * @return
	 */
	IResult insertUser(String email,
					   String userHandle,
					   String userId, String password,
					   String userFullName, String avatar, String role,
					   String homepage, String geolocation, boolean addTopic);

	IResult updateUserData(String userId, String propertyType, String propertyValue);

	IResult removeUserData(String userId, String propertyType, String propertyValue);

	/**
	 * Returns Boolean value as result
	 *
	 * @param userName
	 * @return
	 */
	IResult existsUsername(String userName);

	/**
	 * Returns Boolean value as result
	 *
	 * @param email
	 * @return
	 */
	IResult existsUserEmail(String email);

	/**
	 * @param userId
	 * @return
	 */
	IResult removeUser(String userId);

	/**
	 * Used when <code>grants</code> or <code>password</code> changes
	 *
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	IResult changeUserPassword(String userId, String newPassword);

	/**
	 * <p>OLD: <em>role</em> is really a string of comma-delimited roles
	 * up to 255 characters in length</p>
	 * <p>NEW: <em>roles</em> are collections
	 * <p>At the UI, an Admin will insert or delete a role code from that string.</p>
	 * <p>What is returned here is that revised role string.</p>
	 *
	 * @param userId
	 * @param newRole
	 * @return
	 */
	IResult addUserRole(String userId, String newRole);

	IResult updateUserEmail(String userId, String newEmail);


	/**
	 * <p>Return List of <code>locators</code></p>
	 * <p>NOTE: a userLocator is the username (a single unique word)</p>
	 *
	 * @return can return <code>null</code> or List<String>
	 */
	IResult listUserLocators();

	/**
	 * List users from the user database
	 *
	 * @param start
	 * @param count
	 * @return
	 */
	IResult listUsers(int start, int count);

	/**
	 * <p>This is for one-time migration of userId values when a user database is updated
	 * and you don't want to replace the topic map.</p>
	 * <p>This will not scale; can only be used with a few users in a new system.</p>
	 * <p>To use, force ElasticSearch to run in a different index (change the name in the yml file)
	 * then boot the new system into that index. Then ask <em>all</em>present users to sign up
	 * again. Then, use the Admin Migrate function to change each user's identity from the new one
	 * to the old one. Then reboot back into the topic map.</p>
	 * @param oldUserId  old replaces new
	 * @param newUserId
	 * @return
	 */
	IResult migrateUserId(String oldUserId, String newUserId);


	void shutDown();
}
