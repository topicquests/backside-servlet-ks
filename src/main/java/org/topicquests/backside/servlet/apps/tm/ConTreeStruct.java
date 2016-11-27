/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.util.*;

import org.topicquests.backside.servlet.apps.tm.api.IConversationTreeStruct;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class ConTreeStruct implements IConversationTreeStruct {
	private JSONObject data;
	/**
	 * 
	 */
	public ConTreeStruct() {
		data = new JSONObject();
	}

	public ConTreeStruct(JSONObject root) {
		data = new JSONObject();
		setRoot(root);
	}
	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.IConversationTreeStruct#setRoot(net.minidev.json.JSONObject)
	 */
	@Override
	public void setRoot(JSONObject root) {
		data.put(IConversationTreeStruct.ROOT, root);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.IConversationTreeStruct#getRoot()
	 */
	@Override
	public JSONObject getRoot() {
		return (JSONObject)data.get(IConversationTreeStruct.ROOT);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.IConversationTreeStruct#addChild(net.minidev.json.JSONObject)
	 */
	@Override
	public void addChild(JSONObject child) {
		List<JSONObject> l = listChildNodes();
		if (l == null) l = new ArrayList<JSONObject>();
		l.add(child);
		data.put(IConversationTreeStruct.CHILDNODES, l);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.IConversationTreeStruct#listChildNodes()
	 */
	@Override
	public List<JSONObject> listChildNodes() {
		return (List<JSONObject>)data.get(IConversationTreeStruct.CHILDNODES);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.IConversationTreeStruct#getData()
	 */
	@Override
	public JSONObject getData() {
		return data;
	}

}
