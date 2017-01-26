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

import org.topicquests.backside.servlet.api.ICredentialsMicroformat;

/**
 * @author park
 */
public interface IAdminMicroformat extends ICredentialsMicroformat {

	/**
	 * Verbs
	 */
	public static final String REMOVE_USER = "RemUser";  //POST
	public static final String UPDATE_USER_ROLE = "UpdUsRol"; //POST
	public static final String REMOVE_USER_ROLE = "RemUsRol"; //POST
	public static final String UPDATE_USER_EMAIL = "UpdUsEma"; //POST user
	public static final String UPDATE_USER_PASSWORD = "UpdUsPwd"; //POST user
	public static final String UPDATE_USER_DATA = "UpdUsDat"; //POST user
	public static final String REMOVE_USER_DATA = "RemUsDat"; //POST user

	/**
	 * subject to common modifiers
	 */
	public static final String LIST_USERS = "ListUsers";    //GET
	public static final String LIST_INVITES = "ListInvites"; //GET
	public static final String NEW_INVITE = "NewInvite"; //POST
	public static final String REMOVE_INVITE = "RemoveInvite"; //POST
	public static final String EXISTS_INVITE = "ExistsInvite"; //GET
}
