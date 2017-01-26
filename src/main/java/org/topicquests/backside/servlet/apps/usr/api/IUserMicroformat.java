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

import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.apps.admin.api.IAdminMicroformat;

/**
 * @author park
 *         <p>Microformat for passing user information</p>
 *         <p>These apply <em>only</em> to the User App, which only deals with
 *         the user database, not the topic map</p>
 *
 * see org.topicquests.backside.servlet.apps.admin.api.IAdminMicroformat
 */
public interface IUserMicroformat extends ICredentialsMicroformat {
	/**
	 * JSON Attributes
	 */
	public static final String USER_ROLE = "uRole";
	public static final String USER_AVATAR = "uAvatar";
	public static final String USER_GEOLOC = "uGeoloc";
	public static final String USER_HOMEPAGE = "uHomepage";
	public static final String USER_FULLNAME = "uFullName";

	/**
	 * starts a list of USER_PROPERTY objects
	 */
	public static final String PROP_KEY = "pKey";
	public static final String PROP_VAL = "pVal";

	/**
	 * Verbs
	 */
	public static final String NEW_USER = "NewUser";    //POST
	public static final String LIST_USERS = "ListUsers";    //GET
	public static final String GET_USER_BY_EMAIL = "GetUser";  //GET
	public static final String GET_USER_BY_ID = "GetUsrId";
	public static final String GET_USER_BY_HANDLE = "GetUsrHndl";


}
