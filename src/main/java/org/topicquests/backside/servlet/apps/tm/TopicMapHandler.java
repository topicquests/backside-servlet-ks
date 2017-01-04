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
import org.topicquests.ks.api.ICoreIcons;
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
		conModel = environment.getConversationModel();
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
		String verb = getVerb(jsonObject);
		int code = 0;
		IResult r;
		System.out.println("TopicMapHandler.handleGet "+verb);
		if (verb.equals(IUserMicroformat.LIST_USERS)) {
			String startS = notNullString(jsonObject.getAsString(ICredentialsMicroformat.ITEM_FROM));
			String countS = notNullString(jsonObject.getAsString(ICredentialsMicroformat.ITEM_COUNT));
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
			String locator = notNullString(jsonObject.getAsString(ITopicMapMicroformat.TOPIC_LOCATOR));
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
			String startS = getItemFrom(jsonObject);
			String countS = getItemCount(jsonObject);
			String typeLocator = notNullString(jsonObject.getAsString(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE));
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
/*		} else if (verb.equals(ITopicMapMicroformat.LIST_ALL_BLOG_POSTS)) { 
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
			r = model.listAllBlogPosts(start, count, credentials);
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

			} */
		} else if (verb.equals(ITopicMapMicroformat.LIST_SUBCLASS_TOPICS)) { 
			String startS = getItemFrom(jsonObject);
			String countS = getItemCount(jsonObject);
			String superClassLocator = notNullString(jsonObject.getAsString(ITQCoreOntology.SUBCLASS_OF_PROPERTY_TYPE));
			environment.logDebug("TopicMapHandler.listSubClasses "+superClassLocator);
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
			r = model.listSubclassTopics(superClassLocator, start, count, credentials);
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
		} else if (verb.equals(ITopicMapMicroformat.LOAD_TREE)) {
			String rootLocator = notNullString(jsonObject.getAsString(ITopicMapMicroformat.TREE_ROOT_LOCATOR));
			String maxDepth = notNullString(jsonObject.getAsString(ITopicMapMicroformat.MAX_TREE_DEPTH));
			String startS = getItemFrom(jsonObject);
			String countS = getItemCount(jsonObject);
			int start = 0, count = -1, depth = -1;
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
			if (!maxDepth.equals("")) {
				try {
					depth = Integer.valueOf(maxDepth);
				} catch (Exception e3) {}
			}
			r = model.getNodeTree(rootLocator, depth, start, count, credentials);
			//Treat the result just as if it's a SubjectProxy, which it is
			if (r.getResultObject() != null) {
				ISubjectProxy n = (ISubjectProxy)r.getResultObject();
				System.out.println("LOADTREE "+n);
				returnMessage.put(ICredentialsMicroformat.CARGO, (JSONObject)n.getData());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				message = "Not found";
				code = BaseHandler.RESPONSE_OK;
			}
		} else if (verb.equals(ITopicMapMicroformat.LIST_TREE_CHILD_NODES)) {
			String locator = notNullString(jsonObject.getAsString(ITopicMapMicroformat.TOPIC_LOCATOR));
			String context = notNullString(jsonObject.getAsString(ITopicMapMicroformat.CONTEXT_LOCATOR));
			r = model.listTreeChildNodesJSON(locator, context, credentials);
			System.out.println("LIST_TREE_CHILD_NODES "+r.getResultObject());
			if (r.getResultObject() != null) {
				JSONObject n = (JSONObject)r.getResultObject();
				System.out.println("LISTTREE "+n);
				returnMessage.put(ICredentialsMicroformat.CARGO, n);
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				message = "Not found";
				code = BaseHandler.RESPONSE_OK;
			}
		} else if (verb.equals(ITopicMapMicroformat.COLLECT_CONVERSATION_TREE)) {
			String locator = notNullString(jsonObject.getAsString(ITopicMapMicroformat.TOPIC_LOCATOR));
			String context = notNullString(jsonObject.getAsString(ITopicMapMicroformat.CONTEXT_LOCATOR));
			r = model.collectParentChildTree(locator, context, credentials);
			System.out.println("COLLECT_TREE "+r.getResultObject());
			if (r.getResultObject() != null) {
				JSONObject n = (JSONObject)r.getResultObject();
				System.out.println("CollectTree "+n);
				returnMessage.put(ICredentialsMicroformat.CARGO, n);
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				message = "Not found";
				code = BaseHandler.RESPONSE_OK;
			}
		} else if (verb.equals(ITopicMapMicroformat.GET_TOPIC_BY_URL)) {
			String url = jsonObject.getAsString(ITQCoreOntology.RESOURCE_URL_PROPERTY);
			if (url != null) {
				r = model.listTopicsByURL(url, credentials);
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
		String verb = getVerb(jsonObject);
		int code = 0;
		IResult r;
		System.out.println("TopicMapHandler.handlePost "+verb);
		if (verb.equals(ITopicMapMicroformat.PUT_TOPIC)) {
			if (cargo != null) {
				r = model.putTopic(cargo);
				returnMessage.put(ICredentialsMicroformat.CARGO, ((ISubjectProxy)r.getResultObject()).getData());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-1-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.UPDATE_TOPIC)) {
			if (cargo != null) {
				r = model.updateTopic(cargo, true);
				returnMessage.put(ICredentialsMicroformat.CARGO, ((ISubjectProxy)r.getResultObject()).getData());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-1-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.UPDATE_TOPIC_TEXT_FIELDS)) {
			if (cargo != null) {
				String lox = cargo.getAsString("locator");
				String label = cargo.getAsString("title");
				String details = cargo.getAsString("body");
				String language = cargo.getAsString("Lang");
				if (language == null) language = "en"; //default
				r = model.updateTopicTextFields(lox, label, details, language, true, credentials);
				returnMessage.put(ICredentialsMicroformat.CARGO, "Updated");
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-1-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			}
		} else if (verb.equals(ITopicMapMicroformat.NEW_INSTANCE_TOPIC)) {
			if (cargo != null) {
				System.out.println("CARGO "+cargo.toJSONString());
				r = model.newInstanceNode(cargo, credentials);
				returnMessage.put(ICredentialsMicroformat.CARGO, ((ISubjectProxy)r.getResultObject()).getData());
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				String x = IErrorMessages.MISSING_TOPIC+"-TMServletPost-2-"+verb;
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
				String locator = cargo.getAsString("lox");
				String nodeType = cargo.getAsString(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE);
				String parentLoc = cargo.getAsString(ITopicMapMicroformat.CONVERSATION_PARENT_LOCATOR);
				String contextLoc = cargo.getAsString(ITopicMapMicroformat.CONTEXT_LOCATOR);
				String userId = cargo.getAsString(ICredentialsMicroformat.USER_ID);
				String pvt = cargo.getAsString(ITopicMapMicroformat.IS_PRIVATE);
				String label = cargo.getAsString("label");
				String details = cargo.getAsString("details");
				String language = cargo.getAsString("Lang");
				String url = cargo.getAsString("url");
				boolean isPrivate = false;
				if (pvt.equalsIgnoreCase("T"))
					isPrivate = true;
				String det = null;
				if (details != null && !details.equals(""))
					det = details;
				String lox = null;
				if (locator != null && !locator.equals(""))
					lox = locator;
				r = conModel.newConversationNode(nodeType, parentLoc, contextLoc, lox, label, det, language, url, userId, isPrivate);
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
		} else if (verb.equals(ITopicMapMicroformat.ADD_RELATION)) {
			if (cargo != null) {
				String relTypeLoc = cargo.getAsString("RelTypLoc");
				String sourceLoc = cargo.getAsString(ITopicMapMicroformat.REL_SRC_LOCATOR);
				String targetLoc = cargo.getAsString(ITopicMapMicroformat.REL_TRG_LOCATOR);
				String smallIcon = ICoreIcons.RELATION_ICON_SM;
				String largeIcon = ICoreIcons.RELATION_ICON;
				System.out.println("THM.add "+sourceLoc+" | "+targetLoc+" | "+relTypeLoc);
				if (relTypeLoc == null) {
					throw new ServletException(cargo.toJSONString());
				}
				r = model.addRelation(sourceLoc, targetLoc, relTypeLoc, smallIcon, largeIcon, false, false, credentials);
				returnMessage.put(ICredentialsMicroformat.CARGO, "Relation Done");
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
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
			String url = notNullString(jsonObject.getAsString(ITQCoreOntology.RESOURCE_URL_PROPERTY));
			String title = notNullString(jsonObject.getAsString(ITopicMapMicroformat.TOPIC_LABEL));
			String details = notNullString(jsonObject.getAsString("details"));
			String language  = notNullString(jsonObject.getAsString(ITopicMapMicroformat.LANGUAGE));
			String userId  = notNullString(jsonObject.getAsString(ICredentialsMicroformat.USER_ID));
			JSONObject tagLabels = (JSONObject)jsonObject.get(ITopicMapMicroformat.LIST_PROPERTY);
			r = model.findOrCreateBookmark(url, title, details, language, userId, tagLabels, credentials);
			environment.logDebug("CondoHandler.FindOrCreateBookmark "+r.getErrorString()+" | "+r.getResultObject());
			if (!r.hasError()) {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";
			} else {
				message = r.getErrorString();
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
			}
			
		} else if (verb.equals(ITopicMapMicroformat.FIND_OR_PROCESS_TAG)) {
			String bookmarkLocator = notNullString(jsonObject.getAsString(ITQCoreOntology.LOCATOR_PROPERTY));
			List<String> tagLabels = ((List<String>)jsonObject.get(ITopicMapMicroformat.LIST_PROPERTY));
			String language  = notNullString(jsonObject.getAsString(ITopicMapMicroformat.LANGUAGE));
			r = model.findOrProcessTags(bookmarkLocator, tagLabels, language, credentials);
			environment.logDebug("CondoHandler.FindOrProcessTag "+r.getErrorString()+" | "+r.getResultObject());
			if (r.hasError()) {
				message = r.getErrorString();
				code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
			} else {
				code = BaseHandler.RESPONSE_OK;
				message = "ok";				
			}	
		} else if (verb.equals(ITopicMapMicroformat.TRANSCLUDE_CHILD)) {
			if (cargo != null) {
				String parent = cargo.getAsString("parent");
				String child = cargo.getAsString("child");
				String context = cargo.getAsString("context");
				String language = cargo.getAsString("Lang");
				r = conModel.transcludeChildNode(parent, context, child, language, credentials);
				if (!r.hasError()) {
					code = BaseHandler.RESPONSE_OK;
					message = "ok";
				} else {
					message = r.getErrorString();
					code = BaseHandler.RESPONSE_INTERNAL_SERVER_ERROR;
				}
			} else {
				String x = IErrorMessages.MISSING_CARGO+"-TMServletPost-"+verb;
				environment.logError(x, null);
				throw new ServletException(x);
			} 
		} else {
			String x = IErrorMessages.BAD_VERB+"-AdminServletPost-"+verb;
			environment.logError(x, null);
			throw new ServletException(x);
		}
		returnMessage.put(ICredentialsMicroformat.RESP_TOKEN, rtoken);
		returnMessage.put(ICredentialsMicroformat.RESP_MESSAGE, message);
		super.sendJSON(returnMessage.toJSONString(), code, response);
		returnMessage = null;
	}
	
	public void shutDown() {
		model.shutDown();
	}

}
