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
		System.out.println("User TopicMapHandler");
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
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		IResult r;
		if (verb.equals(IUserMicroformat.LIST_USERS)) {
			String startS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_FROM));
			String countS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_COUNT));
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
			String email = notNullString((String)jsonObject.get(ICredentialsMicroformat.USER_EMAIL));
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
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		IResult r;
		System.out.println("NEWUSER-");
		if (verb.equals(IUserMicroformat.NEW_USER)) {
			String email = (String)jsonObject.get(IUserMicroformat.USER_EMAIL);
			//TODO sanity check
			String userName = (String)jsonObject.get(IUserMicroformat.USER_NAME);
			String fullName = (String)jsonObject.get(IUserMicroformat.USER_FULLNAME);
			String password = (String)jsonObject.get(IUserMicroformat.USER_PWD);
			String avatar = notNullString((String)jsonObject.get(IUserMicroformat.USER_AVATAR));
			String homepage = notNullString((String)jsonObject.get(IUserMicroformat.USER_HOMEPAGE));
			System.out.println("NEWUSER "+homepage);
			String geolocation = notNullString((String)jsonObject.get(IUserMicroformat.USER_GEOLOC));
			String role = notNullString((String)jsonObject.get(IUserMicroformat.USER_ROLE));
			if (role.equals("")) 
				role = ISecurity.USER_ROLE; //default role
			byte [] foo = BaseEncoding.base64().decode(password);
			String creds = new String(foo);
			r = model.insertUser(email, userName, creds, fullName, avatar, role, homepage, geolocation, true);
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
