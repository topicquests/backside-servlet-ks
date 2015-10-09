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

import org.topicquests.common.api.IResult;

/**
 * @author park
 *
 */
public interface IInviteModel {

	IResult existsInvite(String userEmail);
	
	IResult addInvite(String userEmail);
	
	IResult removeInvite(String userEmail);

	/**
	 * 
	 * @param start
	 * @param count -1 means list all
	 * @return
	 */
	IResult listInvites(int start, int count);
}
