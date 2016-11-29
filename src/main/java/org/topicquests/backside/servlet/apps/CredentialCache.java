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
package org.topicquests.backside.servlet.apps;

import java.util.*;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.ks.api.ITicket;

/**
 * @author park
 * <p>This, of course, will not scale</p>
 */
public class CredentialCache {
	private ServletEnvironment environment;
	private Map<String, ITicket> cache;
	
	/**
	 * 
	 */
	public CredentialCache(ServletEnvironment env) {
		environment = env;
		cache = new HashMap<String,ITicket>();
	}

	public void putTicket(String token, ITicket ticket) {
		synchronized(cache) {
			cache.put(token, ticket);
		}
	}
	
	public ITicket getTicket(String token) {
		synchronized(cache) {
			return cache.get(token);
		}
	}
	
	public void removeTicket(String token) {
		synchronized(cache) {
			cache.remove(token);
		}
	}
}
