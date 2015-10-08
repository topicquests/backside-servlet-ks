/**
 * 
 */
package org.topicquests.backside.servlet.apps.admin.api;

import java.sql.Connection;

import org.topicquests.backside.servlet.api.IRDBMSDatabase;
import org.topicquests.common.api.IResult;


/**
 * @author park
 *
 */
public interface IInviteDatabase extends IRDBMSDatabase {
		
	IResult existsInvite(Connection con, String userEmail);
	
	IResult addInvite(Connection con, String userEmail);
	
	IResult removeInvite(Connection con, String userEmail);
	
	/**
	 * @param con
	 * @param start
	 * @param count -1 means list all
	 * @return
	 */
	IResult listInvites(Connection con, int start, int count);

}
