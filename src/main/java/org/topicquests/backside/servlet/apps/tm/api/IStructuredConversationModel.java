/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm.api;

import org.topicquests.common.api.IResult;
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
	 * @param userId
	 * @param isPrivate
	 * @return can return an error message "BadNodeType"
	 */
	IResult newConversationNode(String nodeType, String parentLocator, String contextLocator, String locator, String label, String details, String language,
								String userId, boolean isPrivate);

}
