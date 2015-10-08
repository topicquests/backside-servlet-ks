/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.util.*;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.tm.api.ISocialBookmarkLegend;
import org.topicquests.backside.servlet.apps.tm.api.ITagModel;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ICoreIcons;
import org.topicquests.ks.api.ITQDataProvider;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.INodeTypes;
import org.topicquests.ks.tm.api.ISubjectProxy;
import org.topicquests.ks.tm.api.ISubjectProxyModel;

/**
 * @author park
 *
 */
public class TagModel implements ITagModel {
	private ServletEnvironment environment;
	private ITQDataProvider topicMap;
	private ISubjectProxyModel nodeModel;

	/**
	 * 
	 */
	public TagModel(ServletEnvironment env) {
		environment = env;
		topicMap = environment.getTopicMapEnvironment().getDatabase();
		nodeModel = topicMap.getSubjectProxyModel();

	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITagModel#addTagsToNode(org.topicquests.model.api.node.ISubjectProxy, java.lang.String[], org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult addTagsToNode(ISubjectProxy node, List<String> tagNames,
			ITicket credentials) {
		String userId = credentials.getUserLocator();
		IResult result = new ResultPojo();
		IResult r;
		int len = tagNames.size();
		String name,lox;
		ISubjectProxy tag;
		for (int i=0;i<len;i++) {
			name = tagNames.get(i);
			lox = tagNameToLocator(name);
			//do we already know this tag?
			r = topicMap.getNode(lox, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			tag = (ISubjectProxy)r.getResultObject();
			if (tag == null) {
				//create a tag
				environment.logDebug("TagModel.addTagToNode-1 "+lox+" "+userId);
				System.out.println("TagModel.addTagToNode-1 "+lox+" "+userId);
				tag = nodeModel.newInstanceNode(lox, INodeTypes.TAG_TYPE, name, "", "en", userId, 
						ICoreIcons.TAG_SM, ICoreIcons.TAG, false);
				r = topicMap.putNode(tag, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			}
			//relate the tag to the user
			r = topicMap.getNode(userId, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			ISubjectProxy user = (ISubjectProxy)r.getResultObject();
			if (user != null) {
				environment.logDebug("TagModel.addTagToNode-2 "+tag+" "+user);
				System.out.println("TagModel.addTagToNode-2 "+tag+" "+user);
				r = nodeModel.relateExistingNodesAsPivots(tag, user, ISocialBookmarkLegend.TAG_USER_RELATION_TYPE, userId, 
						ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			}
			//relate the tag to the topic
			environment.logDebug("TagModel.addTagToNode-3 "+tag+" "+node);
			System.out.println("TagModel.addTagToNode-3 "+tag+" "+node);
			r = nodeModel.relateExistingNodesAsPivots(tag, node, ISocialBookmarkLegend.TAG_BOOKMARK_RELATION_TYPE, userId, 
					ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			//relating does all the puts
		}
		return result;
	}

	///////////////////////////
	// utililties
	///////////////////////////
	
	private String tagNameToLocator(String name) {
		String result = name;
		result = result.toLowerCase();
		result = result.replaceAll(" ", "_");
		result = result.replaceAll("'", "_");
		result = result.replaceAll(":", "_");
//		result = result.replaceAll("\+", "P");
//		result = result.replaceAll("-", "M");
		result = result+"_TAG";
		return result;
	}
}
