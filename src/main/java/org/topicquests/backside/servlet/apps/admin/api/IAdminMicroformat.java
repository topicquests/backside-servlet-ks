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
 *
 */
public interface IAdminMicroformat extends ICredentialsMicroformat {

	/** Verbs */
	public static final String
		REMOVE_USER				= "RemUser",  //POST
		UPDATE_USER_ROLE		= "UpdUsRol", //POST
		UPDATE_USER_EMAIL		= "UpdUsEma", //POST user
		UPDATE_USER_PASSWORD	= "UpdUsPwd", //POST user
		UPDATE_USER_DATA		= "UpdUsDat", //POST user
		/** subject to common modifiers */
		LIST_USERS				= "ListUsers",	//GET
		LIST_INVITES			= "ListInvites", //GET
		NEW_INVITE				= "NewInvite", //POST
		REMOVE_INVITE			= "RemoveInvite", //POST
		EXISTS_INVITE			= "ExistsInvite"; //GET


}
