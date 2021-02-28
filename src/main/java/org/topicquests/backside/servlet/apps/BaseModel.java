/**
 * 
 */
package org.topicquests.backside.servlet.apps;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.IExtendedCoreIcons;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.ks.api.ICoreIcons;
import org.topicquests.ks.tm.api.IDataProvider;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.api.INodeTypes;
import org.topicquests.ks.tm.api.IProxy;
import org.topicquests.ks.tm.api.IProxyModel;

/**
 * @author jackpark
 *
 */
public class BaseModel {
	protected ServletEnvironment environment;
	protected SystemEnvironment tmEnvironment;
	protected IDataProvider topicMap;
	protected IProxyModel nodeModel;

	/**
	 * 
	 */
	public BaseModel(ServletEnvironment env) {
		environment = env;
		tmEnvironment = environment.getTopicMapEnvironment();
		topicMap = tmEnvironment.getDataProvider();
		nodeModel = tmEnvironment.getProxyModel();
	}

	protected IResult relateNodeToUser(IProxy node, String userId, String provenanceLocator, ITicket credentials) {
		IResult result = new ResultPojo();
		//NOW, relate this puppy
		String relation = "DocumentCreatorRelationType";
		IResult r;
			r = topicMap.getNode(userId, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			IProxy user = (IProxy)r.getResultObject();
			if (user != null) {
				//IProxy sourceNode,IProxy targetNode, String relationTypeLocator, String userId,
				//String smallImagePath, String largeImagePath, boolean isTransclude,boolean isPrivate
				environment.logDebug("RELATING-3 \n"+node.toJSONString()+"\n"+user.toJSONString());
				r = nodeModel.relateExistingNodes(node, user, relation, null, null,
						userId, provenanceLocator, ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			} else
				result.addErrorString("Missing User "+userId);
		return result;
	}
	
	/**
	 * Create a simple Biography Node
	 * @param userId
	 * @param title
	 * @param details
	 * @param language
	 * @param provenanceLocator TODO
	 * @return
	 */
	protected IProxy newBiographyNode(String userId, String title, String details, String language, String provenanceLocator) {
		IProxy result = nodeModel.newInstanceNode(INodeTypes.BIOGRAPHY_TYPE, title, details, language, userId, provenanceLocator, IExtendedCoreIcons.BIOGRAPHY_SM,
					IExtendedCoreIcons.BIOGRAPHY, false);
		return result;
	}
}
