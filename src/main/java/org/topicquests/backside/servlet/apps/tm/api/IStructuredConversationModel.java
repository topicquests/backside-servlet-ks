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
package org.topicquests.backside.servlet.apps.tm.api;

import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.INodeTypes;

/**
 * @author jackpark
 *
 */
public interface IStructuredConversationModel {
	
	/**
	 * 
	 * @param nodeType from {@link INodeTypes}
	 * @param parentLocator can be <code>null</code>
	 * @param contextLocator can be <code>null</code> <em>ONLY IF</em> <code>parentLocator</code> is <code>null</code>
	 * @param locator
	 * @param label
	 * @param details
	 * @param language
	 * @param url
	 * @param userId
	 * @param isPrivate
	 * @return can return an error message "BadNodeType"
	 */
	IResult newConversationNode(String nodeType, String parentLocator, String contextLocator, String locator,
			String label, String details, String language, String url, String userId, boolean isPrivate);

	/**
	 * Add a <em>transcluded</em> child to a parent in a particular context
	 * @param parentLocator
	 * @param contextLocator
	 * @param childLocator
	 * @param language TODO
	 * @param credentials
	 * @return
	 */
	IResult transcludeChildNode(String parentLocator, String contextLocator, String childLocator, String language, ITicket credentials);
}
