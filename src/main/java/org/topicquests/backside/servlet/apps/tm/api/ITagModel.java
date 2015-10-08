/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm.api;

import java.util.List;

import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.ISubjectProxy;

/**
 * @author park
 *
 */
public interface ITagModel {

	/**
	 * Convert <code>tagNames</code> into tag locators then
	 * create or reuse tags and add them to the node.
	 * @param node
	 * @param tagNames
	 * @param credentials
	 * @return
	 */
	IResult addTagsToNode(ISubjectProxy node, List<String> tagNames, ITicket credentials);
}
