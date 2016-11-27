/**
 * 
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
