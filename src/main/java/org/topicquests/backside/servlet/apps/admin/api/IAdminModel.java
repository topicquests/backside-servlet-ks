/**
 * 
 */
package org.topicquests.backside.servlet.apps.admin.api;

import org.topicquests.common.api.IResult;

/**
 * @author park
 *
 */
public interface IAdminModel extends IInviteModel {
	
    /**
     * @param userName
     * @return
     */
    IResult removeUser(String userName);

    /**
     * List users for inspection
     * @param start
     * @param count
     * @return a possibly empty list of {@link ITicket} objects
     */
    IResult listUsers(int start, int count);
    
    /**
     * <p>NOTE: <em>role</em> is really a string of comma-delimited roles
     *  up to 255 characters in length</p>
     * <p>At the UI, an Admin will insert or delete a role code from that string.</p>
     * <p>What is returned here is that revised role string.</p>
     * @param userName
     * @param newRole
     * @return
     */    
    IResult updateUserRole(String userName, String newRole);
        
    IResult updateUserEmail(String userName, String newEmail);

    void shutDown();
}
