/*
 *  Copyright (C) 2006  Jack Park,
 * 	mail : jackpark@gmail.com
 *
 *  Part of <TopicSpaces>, an open source project.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.topicquests.backside.servlet.apps.usr.api;

import java.sql.*;

import org.topicquests.backside.servlet.api.IRDBMSDatabase;
import org.topicquests.common.api.IResult;


/**
 *
 * @author Owner
 *
 */
public interface IUserPersist extends IRDBMSDatabase {

    /**
     * Authenticate this user. Return <code>null</code> if doesn't authenticate
     * @param connection
     * @param email
     * @param password
     * @return			Ticket  can return <code>null</code>
     */
	IResult authenticate(Connection con, String email, String password);

    /**
     * Very dangerous method. Use only after authentication, e.g. 
     * in web services.
     * @param con
     * @param userName
     * @return
     */
	IResult getTicket(Connection con, String userName);
	
	IResult getTicketByEmail(Connection con, String email);
    /**
     * <p>
     * Throws an exception if user already exists. Should
     * use <code>existsUsername</code> first
     * </p>
     * @param con
     * @param email 
     * @param userName
     * @param password
     * @param userFullName TODO
     * @param avatar
     * @param role	cannot be <code>null</code>
     * @param homepage TODO
     * @param geolocation TODO
     * @return
     */
    IResult insertUser(Connection con,
    				  String email,
    				  String userName,
    				  String password, String userFullName, String avatar, String role, String homepage, String geolocation);
    
    /**
     * Used when importing from an XML export
     * @param con
     * @param userName
     * @param password is already encrypted
     * @param grant
     * @return
     */
//    IResult insertEncryptedUser(Connection con, String userName, String password, String grant);
    
    IResult insertUserData(Connection con, String userName, String propertyType, String propertyValue);
    
    IResult updateUserData(Connection con, String userName, String propertyType, String newValue);
    
    IResult removeUserData(Connection con, String userName, String propertyType, String oldValue);
    /**
     * Returns Boolean value as result
     * @param con
     * @param userName
     * @return
     */
    IResult existsUsername(Connection con, String userName);
    /**
     *
     * @param con
     * @param userName
     * @return
     */
    IResult removeUser(Connection con, String userName);

	/**
	 * Used when <code>grants</code> or <code>password</code> changes
     * @param connection
	 * @param ticket
	 * @return
	 */
//	void updateUser(Connection con, Ticket ticket) throws TopicSpacesException;
    IResult changeUserPassword(Connection con, String userName, String newPassword);
    
 //   void changeUserGrant(Connection con, String userName, String newGrant)
//		throws TopicSpacesException;
    
    IResult addUserRole(Connection con, String userName, String newRole);
    
    IResult removeUserRole(Connection con, String userName, String oldRole);

    IResult updateUserEmail(Connection con, String userName, String newEmail);

    /**
     * <p>Return List of <code>locators</code></p>
     * @param con
     * @return	can return <code>null</code> or List<String>
     * 
     */
    IResult listUserLocators(Connection con);

    /**
     * 
     * @param con
     * @param start
     * @param count
     * @return a possibly empty list of {@link ITicket} objects
     */
    IResult listUsers(Connection con, int start, int count);
    /**
	 * Clear the database
	 * @throws TopicSpacesException
	 */
	void exportSchema() throws Exception;

}
