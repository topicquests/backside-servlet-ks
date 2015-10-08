/**
 * 
 */
package org.topicquests.backside.servlet.apps.admin.api;

import org.topicquests.common.api.IResult;

/**
 * @author park
 *
 */
public interface IInviteModel {

	IResult existsInvite(String userEmail);
	
	IResult addInvite(String userEmail);
	
	IResult removeInvite(String userEmail);

	/**
	 * 
	 * @param start
	 * @param count -1 means list all
	 * @return
	 */
	IResult listInvites(int start, int count);
}
