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
package org.topicquests.backside.servlet.apps.admin.api;

import org.topicquests.support.api.IResult;

/**
 * @author park
 */
public interface IAdminModel extends IInviteModel {

	/**
	 * @param userName
	 * @return
	 */
	IResult removeUser(String userName);

	/**
	 * List users for inspection
	 *
	 * @param start
	 * @param count
	 * @return a possibly empty list of {@link ITicket} objects
	 */
	IResult listUsers(int start, int count);

	/**
	 * <p>NOTE: <em>role</em> is really a string of comma-delimited roles
	 * up to 255 characters in length</p>
	 * <p>At the UI, an Admin will insert or delete a role code from that string.</p>
	 * <p>What is returned here is that revised role string.</p>
	 *
	 * @param userId
	 * @param newRole
	 * @return
	 */
	IResult addUserRole(String userId, String newRole);

	IResult removeUserRole(String userId, String oldRole);

	IResult updateUserEmail(String userId, String newEmail);
	
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
