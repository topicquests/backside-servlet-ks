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
package org.topicquests.backside.servlet;

import java.util.Map;

import org.nex.config.ConfigPullParser;
import org.topicquests.backside.servlet.apps.CredentialCache;
import org.topicquests.backside.servlet.apps.tm.StructuredConversationModel;
import org.topicquests.backside.servlet.apps.tm.api.IStructuredConversationModel;
import org.topicquests.backside.servlet.apps.usr.UserModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.ks.StatisticsUtility;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.util.LoggingPlatform;

/**
 * @author park
 *
 */
public class ServletEnvironment {
	private static ServletEnvironment instance;
	private LoggingPlatform log=null;
	private Map<String,Object>properties;
	private StatisticsUtility stats;
	private SystemEnvironment tmEnvironment=null;
	private CredentialCache cache;
	private IUserModel userModel;
	private IStructuredConversationModel conversationModel;
	private boolean isShutDown = false;
	
	
	/**
	 * @param isConsole <code>true</code> means boot JSONTopicMap console
	 */
	public ServletEnvironment(boolean isConsole) throws Exception { 
		log = LoggingPlatform.getInstance("logger.properties"); 
		System.out.println("xServletEnvironment- "+(log==null));
		logDebug("xxServletEnvironment-");
		ConfigPullParser p = new ConfigPullParser("config-props.xml");
		properties = p.getProperties();
		System.out.println("PROPS "+properties);
		cache = new CredentialCache(this);
		tmEnvironment = new SystemEnvironment();
		stats = tmEnvironment.getStats();
		System.out.println("STARTING USER");
		userModel = new UserModel(this);
		System.out.println("STARTED USER "+getStringProperty("ServerPort"));
		//String urx = getStringProperty("ServerURL");
		//int port = Integer.valueOf(getStringProperty("ServerPort")).intValue();
		conversationModel = new StructuredConversationModel(this);
		isShutDown = false;
		System.out.println("ServletEnvironment+");
		instance = this;
		logDebug("ServletEnvironment+");
	}

	public static ServletEnvironment getInstance() {
		return instance;
	}
	
	public IStructuredConversationModel getConversationModel() {
		return conversationModel;
	}
	
	public IUserModel getUserModel() {
		return userModel;
	}
	
	public CredentialCache getCredentialCache() {
		return cache;
	}
	
	public SystemEnvironment getTopicMapEnvironment() {
		return tmEnvironment;
	}
	
	public void shutDown() {
		//This might be called by several servlets
		if (!isShutDown) {
			log.shutDown();
			if (tmEnvironment != null)
				tmEnvironment.shutDown();
			isShutDown = true;
		}
	}
	////////////////////////
	// Utilities
	////////////////////////
	
	public String getStringProperty(String key) {
		return (String)properties.get(key);
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public void logDebug(String msg) {
		log.logDebug(msg);

	}

	public void logError(String msg, Exception e) {
		log.logError(msg, e);
	}

}
