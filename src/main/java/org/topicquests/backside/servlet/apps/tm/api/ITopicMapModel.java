/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm.api;

import java.util.List;

import net.minidev.json.JSONObject;

import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ITicket;

/**
 * @author park
 *
 */
public interface ITopicMapModel {
	////////////////
	// General TopicMap handlers
	//NOTE about <em>version</em>
	//  If checking version, the system will issue
	//  an Exception with the message <em>OptimisticLockException</ema>
	//  if the version number in the saved node is less than that which exists
	//  in the index.
	////////////////
	
	/**
	 * Store this <code>topic</code> to the topicmap
	 * @param topic
	 * @param checkVersion <code>true</code> if version sensitive
	 * @return
	 */
	IResult putTopic(JSONObject topic, boolean checkVersion);
	
	IResult getTopic(String topicLocator, ITicket credentials);
	
	IResult removeTopic(String topicLocator, ITicket credentials);
	
	IResult query(JSONObject query, int start, int count, ITicket credentials);
	
	/**
	 * Note: a case could be made for 'listTopicsByURL' except that
	 * URL in this case refers to a lone PropertyType; the assumption
	 * is that, as a TopicMap, there can be one and only one topic
	 * with that identity property.
	 * @param url
	 * @param credentials
	 * @return
	 */
	IResult getTopicByURL(String url, ITicket credentials);
	////////////////
	// Specialized TopicMap handlers
	////////////////
	
	IResult listSubclassTopics(String superClassLocator, int start, int count, ITicket credentials);
	
	IResult listInstanceTopics(String typeLocator, int start, int count, ITicket credentials);
	
	IResult listTopicsByKeyValue(String propertyKey, String value, int start, int count, ITicket credentials);
	
	/**
	 * <p>Allow for a simple shell topic, crafted at web clients, to be filled out to a full topic and persisted
	 * and returned.</p>
	 * <p>Also looks for a <em>url</em> property.
	 * @param theTopicShell
	 * @param credentials
	 * @return returns the node's JSONObject
	 */
	IResult newInstanceNode(JSONObject theTopicShell, ITicket credentials);
	
	IResult newSubclassNode(JSONObject theTopicShell, ITicket credentials);
	
	/**
	 * Cargo must include<br/>
	 * <li>nodeLocator</li>
	 * <li>property type-value(s) pair</li>
	 * <li>userId</li>
	 * @param cargo
	 * @param credentials
	 * @return
	 */
	IResult addFeaturesToNode(JSONObject cargo, ITicket credentials);
	
	IResult addPivot(String topicLocator, String pivotLocator, String pivotRelationType,
					 String smallImagePath, String largeImagePath, boolean isTransclude, boolean isPrivate, ITicket credentials);

	IResult addRelation(String topicLocator, String pivotLocator, String pivotRelationType,
			 String smallImagePath, String largeImagePath, boolean isTransclude, boolean isPrivate, ITicket credentials);

	/**
	 * <p>For a given <code>tabLabel</code><br/>
	 *  1- See if tag exists.<br/>
	 *  2- If not, create tag node and pivot to <code>userId</code><br/>
	 *  3- Pivot to <code>bookmarkLocator</p>
	 * @param bookmarkLocator
	 * @param tagLabels
	 * @param language 
	 * @param credentials
	 * @return
	 */
	IResult findOrProcessTags(String bookmarkLocator, List<String> tagLabels, String language, ITicket credentials);
	
	/**
	 * Find bookmark node for <code>url</code> or otherwise make it and pivot to <code>userId</code>
	 * @param url
	 * @param title 
	 * @param language 
	 * @param userId
	 * @param tagLabels can be <code>null</code>
	 * @param credentials
	 * @return
	 */
	IResult findOrCreateBookmark(String url, String title, String language, String userId, List<String> tagLabels, ITicket credentials);
	
    /**
     * List users in the TopicMap
     * @param start
     * @param count
     * @param credentials
     * @return
     */
    IResult listUserTopics(int start, int count, ITicket credentials);

    IResult getNodeTree(String rootLocator, int maxDepth, int start, int count, ITicket credentials);
    
	void shutDown();
}
