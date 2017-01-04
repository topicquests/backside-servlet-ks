/**
 * 
 */
package org.topicquests.backside.servlet.apps;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.ks.api.ICoreIcons;
import org.topicquests.ks.api.ITQDataProvider;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.ISubjectProxy;
import org.topicquests.ks.tm.api.ISubjectProxyModel;

/**
 * @author jackpark
 *
 */
public class BaseModel {
	protected ServletEnvironment environment;
	protected SystemEnvironment tmEnvironment;
	protected ITQDataProvider topicMap;
	protected ISubjectProxyModel nodeModel;

	/**
	 * 
	 */
	public BaseModel(ServletEnvironment env) {
		environment = env;
		tmEnvironment = environment.getTopicMapEnvironment();
		topicMap = tmEnvironment.getDatabase();
		nodeModel = topicMap.getSubjectProxyModel();
	}

	protected IResult relateNodeToUser(ISubjectProxy node, String userId, ITicket credentials) {
		IResult result = new ResultPojo();
		//NOW, relate this puppy
		String relation = "DocumentCreatorRelationType";
		IResult r;
			r = topicMap.getNode(userId, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			ISubjectProxy user = (ISubjectProxy)r.getResultObject();
			if (user != null) {
				//ISubjectProxy sourceNode,ISubjectProxy targetNode, String relationTypeLocator, String userId,
				//String smallImagePath, String largeImagePath, boolean isTransclude,boolean isPrivate
				environment.logDebug("RELATING-3 \n"+node.toJSONString()+"\n"+user.toJSONString());
				r = nodeModel.relateExistingNodesAsPivots(node, user, relation, userId, 
						ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			} else
				result.addErrorString("Missing User "+userId);
		return result;
	}
}
