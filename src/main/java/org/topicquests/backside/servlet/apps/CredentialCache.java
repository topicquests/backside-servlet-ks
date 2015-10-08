/**
 * 
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
