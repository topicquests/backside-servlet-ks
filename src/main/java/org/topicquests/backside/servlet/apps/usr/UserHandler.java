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
package org.topicquests.backside.servlet.apps.usr;

import java.util.*;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.api.IErrorMessages;
import org.topicquests.backside.servlet.api.ISecurity;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.backside.servlet.apps.admin.api.IAdminMicroformat;
import org.topicquests.backside.servlet.apps.usr.api.IUserMicroformat;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserSchema;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ITicket;

import com.google.common.io.BaseEncoding;

/**
 * @author park
 *
 */
public class UserHandler  extends BaseHandler {
	//access to user database
	private IUserModel model;
	/**
	 * 
	 */
	public UserHandler(ServletEnvironment env, String basePath) {
		super(env,basePath);
		System.out.println("User CondoHandler");
		try {
			model = new UserModel(environment);
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		String message = "", rtoken="";
		String verb = getVerb(jsonObject);
		System.out.println("UserHandler.get verb: "+verb);
		int code = 0;
		IResult r;
		if (verb.equals(IUserMicroformat.LIST_USERS)) {
			String startS = getItemFrom(jsonObject);
			String countS = getItemCount(jsonObject);
			int start = 0, count = -1;
			if (!startS.equals("")) {
				try {
					start = Integer.valueOf(startS);
				} catch (Exception e1) {}
			}
			if (!countS.equals("")) {
				try {
					count = Integer.valueOf(countS);
				} catch (Exception e2) {}
			}
			//TODO: note: we are ignoring any SORT modifiers
			//This really returns some live cargo in the form of a list of user objects in JSON format
			// We are restricting this to: name, email, avatar, homepage, geolocation, role
			r = model.listUsers(start, count);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				//Time to take that list apart
				System.out.println("UserHandler.ListUsers "+r.getResultObject());
				if (r.getResultObject() != null) {
					List<ITicket> usrs = (List<ITicket>)r.getResultObject();
					Iterator<ITicket>itr = usrs.iterator();
					List<JSONObject>jsonUsers = new ArrayList<JSONObject>();
					while (itr.hasNext()) {
						jsonUsers.add(ticketToUser(itr.next()));
					}
					returnMessage.put(ICredentialsMicroformat.CARGO, jsonUsers);
					code = BaseHandler.RESPONSE_OK;
					message = "ok";
				} else {
					message = "Not found";
					code = BaseHandler.RESPONSE_OK;
				}
			}
		} else if (verb.equals(IUserMicroformat.GET_USER)) {
			String email = getEmail(jsonObject);
			r = model.getTicketByEmail(email);
			if (r.getResultObject() != null) {
				ITicket t = (ITicket)r.getResultObject();
				JSONObject jUser = ticketToUser(t);
				returnMessage.put(ICredentialsMicroformat.CARGO, jUser);
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				message = "Not found";
				code = BaseHandler.RESPONSE_OK;
			}
		} else {
			String x = IErrorMessages.BAD_VERB+"-UserServletGet-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);
		}
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;
	}
	
	public void handlePost(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		String message = "", rtoken="";
		String verb = getVerb(jsonObject);
		int code = 0;
		IResult r;
		System.out.println("UserHandler.post verb: "+verb);
		if (verb.equals(IUserMicroformat.NEW_USER)) {
			String email = getEmail(jsonObject);
			//TODO sanity check
			String userName = getUserHandle(jsonObject);
			String fullName = jsonObject.getAsString(IUserMicroformat.USER_FULLNAME);
			String password = jsonObject.getAsString(IUserMicroformat.USER_PWD);
			String avatar = notNullString(jsonObject.getAsString(IUserMicroformat.USER_AVATAR));
			String homepage = notNullString(jsonObject.getAsString(IUserMicroformat.USER_HOMEPAGE));
			System.out.println("NEWUSER "+homepage);
			String geolocation = notNullString(jsonObject.getAsString(IUserMicroformat.USER_GEOLOC));
			String role = notNullString(jsonObject.getAsString(IUserMicroformat.USER_ROLE));
			if (role.equals("")) 
				role = ISecurity.USER_ROLE; //default role
			byte [] foo = BaseEncoding.base64().decode(password);
			String creds = new String(foo);
			r = model.insertUser(email, userName, UUID.randomUUID().toString(), creds, fullName, avatar, role, homepage, geolocation, true);
			System.out.println("NEWUSER2 "+r.getErrorString());
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				rtoken = newUUID();
				message = "ok";
				code = BaseHandler.RESPONSE_OK;
			}
			returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
			returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		} else if (verb.equals(IAdminMicroformat.UPDATE_USER_PASSWORD)) {
			String userName = getUserHandle(jsonObject);
			String password = jsonObject.getAsString(IUserMicroformat.USER_PWD);
			byte [] foo = BaseEncoding.base64().decode(password);
			String creds = new String(foo);
		//	System.out.println("FFFF "+creds);
			r = model.changeUserPassword(userName, creds);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				message = "ok";
				code = BaseHandler.RESPONSE_OK;
			}
		} else if (verb.equals(IAdminMicroformat.REMOVE_USER)) {
			String userName = getUserHandle(jsonObject);
			System.out.println("UserHandler.removeUser "+userName);
			r = model.removeUser(userName);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = r.getErrorString();
			} else {
				message = "ok";
				code = BaseHandler.RESPONSE_OK;
			}
		} else if (verb.equals(IAdminMicroformat.UPDATE_USER_DATA)) {
			String userName = getUserHandle(jsonObject);
			String key = jsonObject.getAsString(IUserMicroformat.PROP_KEY);
			String val = jsonObject.getAsString(IUserMicroformat.PROP_VAL);
			r = model.insertUserData(userName, key, val);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = r.getErrorString();
			} else {
				message = "ok";
				code = BaseHandler.RESPONSE_OK;
			}
		} else {
			String x = IErrorMessages.BAD_VERB+"-UserServletPost-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);
		}

		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;
	}
	

	public void shutDown() {
		model.shutDown();
	}
}
