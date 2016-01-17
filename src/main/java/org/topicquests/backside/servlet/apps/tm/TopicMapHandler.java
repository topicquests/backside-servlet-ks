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
package org.topicquests.backside.servlet.apps.tm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.api.IErrorMessages;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.backside.servlet.apps.admin.api.IAdminMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.IStructuredConversationModel;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserMicroformat;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.ISubjectProxy;

/**
 * @author park
 *
 */
public class TopicMapHandler  extends BaseHandler {
	private static TopicMapHandler instance;
	private ITopicMapModel model;
	private IStructuredConversationModel conModel;
	
	/**
	 * 
	 */
	public TopicMapHandler(ServletEnvironment env, String basePath) {
		super(env,basePath);
		model = new TopicMapModel(environment);
		instance = this;
		conModel = new StructuredConversationModel(environment);
	}
	
	/**
	 * Handler is shared
	 * @return
	 */
	public static TopicMapHandler getInstance() {
		return instance;
	}

	public void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		JSONObject returnMessage = newJSONObject();
		String message = "", rtoken="";
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		IResult r;
		System.out.println("TopicMapHandler.handleGet "+verb);
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
			r = model.listUserTopics(start, count, credentials);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				//Time to take that list apart
				if (r.getResultObject() != null) {
					List<ISubjectProxy> usrs = (List<ISubjectProxy>)r.getResultObject();
					Iterator<ISubjectProxy>itr = usrs.iterator();
					List<JSONObject>jsonUsers = new ArrayList<JSONObject>();
					while (itr.hasNext()) {
						jsonUsers.add((JSONObject)itr.next().getData());
					}
					returnMessage.put(ICredentialsMicroformat.CARGO, jsonUsers);
					code = BaseHandler.RESPONSE_OK;
					message = "ok";
				} else {
					message = "Not found";
					code = BaseHandler.RESPONSE_OK;
				}

			}
		} else if (verb.equals(ITopicMapMicroformat.GET_TOPIC)) {
			String locator = notNullString((String)jsonObject.get(ITopicMapMicroformat.TOPIC_LOCATOR));
			r = model.getTopic(locator, credentials);
			System.out.println("GETTOPIC "+r.getResultObject());
			if (r.getResultObject() != null) {
				ISubjectProxy n = (ISubjectProxy)r.getResultObject();
				System.out.println("GETTOPIC "+n);
				returnMessage.put(ICredentialsMicroformat.CARGO, (JSONObject)n.getData());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				message = "Not found";
				code = BaseHandler.RESPONSE_OK;
			}
		} else if (verb.equals(ITopicMapMicroformat.LIST_INSTANCE_TOPICS)) { 
			String startS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_FROM));
			String countS = notNullString((String)jsonObject.get(ICredentialsMicroformat.ITEM_COUNT));
			String typeLocator = notNullString((String)jsonObject.get(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE));
			environment.logDebug("TopicMapHandler.listInstanceTopics "+typeLocator);
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
			r = model.listInstanceTopics(typeLocator, start, count, credentials);
			if (r.hasError()) {
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				message = r.getErrorString();
			} else {
				//Time to take that list apart
				if (r.getResultObject() != null) {
					List<ISubjectProxy> usrs = (List<ISubjectProxy>)r.getResultObject();
					Iterator<ISubjectProxy>itr = usrs.iterator();
					List<JSONObject>jsonUsers = new ArrayList<JSONObject>();
					while (itr.hasNext()) {
						jsonUsers.add((JSONObject)itr.next().getData());
					}
					returnMessage.put(ICredentialsMicroformat.CARGO, jsonUsers);
					code = BaseHandler.RESPONSE_OK;
					message = "ok";
				} else {
					message = "Not found";
					code = BaseHandler.RESPONSE_OK;
				}

			}
			
		} else if (verb.equals(ITopicMapMicroformat.LIST_SUBCLASS_TOPICS)) { 
			//TODO
		} else if (verb.equals(ITopicMapMicroformat.LOAD_TREE)) {
			//TODO
		} else if (verb.equals(ITopicMapMicroformat.GET_TOPIC_BY_URL)) {
			String url = (String)jsonObject.get(ITQCoreOntology.RESOURCE_URL_PROPERTY);
			if (url != null) {
				r = model.getTopicByURL(url, credentials);
				if (r.getResultObject() != null) {
					ISubjectProxy n = (ISubjectProxy)r.getResultObject();
					System.out.println("GETTOPICBYURL "+n);
					returnMessage.put(ICredentialsMicroformat.CARGO, (JSONObject)n.getData());
					code = BaseHandler.RESPONSE_OK;
					message = "ok";
				} else {
					message = "Not found";
					code = BaseHandler.RESPONSE_OK;
				}

			}
		}  else {
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
		JSONObject cargo = (JSONObject)jsonObject.get(ICredentialsMicroformat.CARGO);
		String message = "", rtoken="";
		String verb = (String)jsonObject.get(ICredentialsMicroformat.VERB);
		int code = 0;
		IResult r;
		System.out.println("TopicMapHandler.handlePost "+verb);
		if (verb.equals(ITopicMapMicroformat.PUT_TOPIC)) {
			//TODO
		} else if (verb.equals(ITopicMapMicroformat.NEW_INSTANCE_TOPIC)) {
			if (cargo != null) {
				System.out.println("CARGO "+cargo.toJSONString());
				r = model.newInstanceNode(cargo, credentials);
				returnMessage.put(ICredentialsMicroformat.CARGO, ((ISubjectProxy)r.getResultObject()).getData());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.NEW_SUBCLASS_TOPIC)) {
			if (cargo != null) {
				r = model.newSubclassNode(cargo, credentials);
				returnMessage.put(ICredentialsMicroformat.CARGO, ((ISubjectProxy)r.getResultObject()).getData());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.NEW_CONVERSATION_NODE)) {
			if (cargo != null) {
				String locator = (String)cargo.get("lox");
				String nodeType = (String)cargo.get(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE);
				String parentLoc = (String)cargo.get(ITopicMapMicroformat.CONVERSATION_PARENT_LOCATOR);
				String contextLoc = (String)cargo.get(ITopicMapMicroformat.CONTEXT_LOCATOR);
				String userId = (String)cargo.get("uName");
				String pvt = (String)cargo.get("isPvt");
				String label = (String)cargo.get("label");
				String details = (String)cargo.get("details");
				String language = (String)cargo.get("Lang");
				boolean isPrivate = false;
				if (pvt.equalsIgnoreCase("T"))
					isPrivate = true;
				String det = null;
				if (details != null && !details.equals(""))
					det = details;
				String lox = null;
				if (locator != null && !locator.equals(""))
					lox = locator;
				r = conModel.newConversationNode(nodeType, parentLoc, contextLoc, lox, label, det, language, userId, isPrivate);
				if (r.getResultObject() != null) {
					ISubjectProxy n = (ISubjectProxy)r.getResultObject();
					System.out.println("GETTOPICBYURL "+n);
					returnMessage.put(ICredentialsMicroformat.CARGO, n.getData());
					code = BaseHandler.RESPONSE_OK;
					message = "ok";
				} else {
					//there will be an error message
					code = BaseHandler.RESPONSE_OK;
					message = r.getErrorString();
				}
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.REMOVE_TOPIC)) {
			//TODO
		} else if (verb.equals(ITopicMapMicroformat.ADD_FEATURES_TO_TOPIC)) {
			if (cargo != null) {
				r = model.addFeaturesToNode(cargo, credentials);
			} else {
				String x = IErrorMessages.MISSING_CARGO+"-TMServletPost-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.FIND_OR_CREATE_BOOKMARK)) {
			System.out.println("FIND_OR_CREATE_BOOKMARK "+jsonObject.toJSONString());
			//FIND_OR_CREATE_BOOKMARK {"ListProperty":{"tag1":"Transclusion","tag4":"","tag2":
			//"Ted Nelson","tag3":"Wikipedia"},"uName":"jackpark","sToken":"419e24cc-22dc-404b-b0d1-a987d9a9cbc8",
			//"verb":"FindProcessBookmark","Lang":"en","label":"Transclusion - Wikipedia, the free encyclopedia","uIP":"",
			//"url":"https:\/\/en.wikipedia.org\/wiki\/Transclusion"}
			String url = notNullString((String)jsonObject.get(ITQCoreOntology.RESOURCE_URL_PROPERTY));
			String title = notNullString((String)jsonObject.get(ITopicMapMicroformat.TOPIC_LABEL));
			String language  = notNullString((String)jsonObject.get(ITopicMapMicroformat.LANGUAGE));
			String userId  = notNullString((String)jsonObject.get(ICredentialsMicroformat.USER_NAME));
			JSONObject tagLabels = (JSONObject)jsonObject.get(ITopicMapMicroformat.LIST_PROPERTY);
			r = model.findOrCreateBookmark(url, title, language, userId, tagLabels, credentials);
			environment.logDebug("TopicMapHandler.FindOrCreateBookmark "+r.getErrorString()+" | "+r.getResultObject());
			if (!r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				message = r.getErrorString();
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
			}
			
		} else if (verb.equals(ITopicMapMicroformat.FIND_OR_PROCESS_TAG)) {
			String bookmarkLocator = notNullString((String)jsonObject.get(ITQCoreOntology.LOCATOR_PROPERTY));
			List<String> tagLabels = ((List<String>)jsonObject.get(ITopicMapMicroformat.LIST_PROPERTY));
			String language  = notNullString((String)jsonObject.get(ITopicMapMicroformat.LANGUAGE));
			r = model.findOrProcessTags(bookmarkLocator, tagLabels, language, credentials);
			environment.logDebug("TopicMapHandler.FindOrProcessTag "+r.getErrorString()+" | "+r.getResultObject());
			if (r.hasError()) {
				message = r.getErrorString();
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
			} else {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";				
			}	
		} else {
			String x = IErrorMessages.BAD_VERB+"-AdminServletPost-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);
		}
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;	}
	
	public void shutDown() {
		model.shutDown();
	}

}
