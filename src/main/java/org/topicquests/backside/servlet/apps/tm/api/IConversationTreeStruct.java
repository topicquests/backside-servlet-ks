/*
 * Copyright 2016, TopicQuests
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
package org.topicquests.backside.servlet.apps.tm.api;

import java.util.List;

import net.minidev.json.JSONObject;
import org.topicquests.ks.tm.api.ISubjectProxy;

/**
 * @author jackpark
 *
 */
public interface IConversationTreeStruct {
	public static final String
		ROOT 		= "root",
		CHILDNODES	= "kids";
	
	/**
	 * Root is the {@link ISubjectProxy} as a {@link JSONObject}
	 * @param root
	 */
	void setRoot(JSONObject root);
	JSONObject getRoot();
	
	/**
	 * A child is a {@link JSONObject} instance of {@link IConversationTreeStruct}
	 * @param child
	 */
	void addChild(JSONObject child);
	
	List<JSONObject> listChildNodes();
	
	JSONObject getData();
	
}
