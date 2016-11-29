/*
 * Copyright 2015, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.backside.servlet.apps.usr.api;

import org.topicquests.common.api.IResult;
import org.topicquests.backside.servlet.api.ISecurity;
/**
 * @author park
 *
 */
public interface IUserModel {
	
    /**
     * Authenticate this user. Return <code>null</code> if doesn't authenticate
     * @param email
     * @param password
     * @return			Ticket  can return <code>null</code>
     */
	IResult authenticate(String email, String password);

    /**
     * Very dangerous method. Use only after authentication, e.g. 
     * in web services.
      * @param userName
     * @return
     */
	IResult getTicket(String userName);
	
	/**
	 * Can return <code>null</code> inside Return object
	 * @param email
	 * @return
	 */
	IResult getTicketByEmail(String email);
	
    /**
     * <p>
     * Throws an exception if user already exists. Should
     * use <code>existsUsername</code> first
     * </p>
     * @param email 
     * @param userHandle
     * @param userId TODO
     * @param password
     * @param userFullName 
     * @param avatar 
     * @param role based on {@link ISecurity} roles
     * @param homepage 
     * @param geolocation 
     * @param addTopic <code>false</code> only for default administrator
     * @return
     */
    IResult insertUser(String email,
    				  String userHandle,
    				  String userId, String password, 
    				  String userFullName, String avatar, String role, 
    				  String homepage, String geolocation, boolean addTopic);
    
    /**
     * Used when importing from an XML export
     * @param userName
     * @param password is already encrypted
     * @param grant
     * @return
     */
//    IResult insertEncryptedUser(String userName, String password, String grant);
    
    IResult insertUserData(String userName, String propertyType, String propertyValue);
    
    IResult removeUserData(String userName, String propertyType, String propertyValue);
    
   // IResult addUserData(String userName, String propertyType, String newValue);
    
    /**
     * Returns Boolean value as result
     * @param userName
     * @return
     */
    IResult existsUsername(String userName);
    
    /**
     * Returns Boolean value as result
     * @param email
     * @return
     */
    IResult existsUserEmail(String email);
    
    /**
     * @param userName
     * @return
     */
    IResult removeUser(String userName);

	/**
	 * Used when <code>grants</code> or <code>password</code> changes
	 * @param ticket
	 * @return
	 */
    IResult changeUserPassword(String userName, String newPassword);
        
    /**
     * <p>OLD: <em>role</em> is really a string of comma-delimited roles
     *  up to 255 characters in length</p>
     * <p>NEW: <em>roles</em> are collections 
     * <p>At the UI, an Admin will insert or delete a role code from that string.</p>
     * <p>What is returned here is that revised role string.</p>
     * @param userName
     * @param newRole
     * @return
     */
    IResult addUserRole(String userName, String newRole);
    
    IResult updateUserEmail(String userName, String newEmail);


    /**
     * <p>Return List of <code>locators</code></p>
     * <p>NOTE: a userLocator is the username (a single unique word)</p>
     * @return	can return <code>null</code> or List<String>
     * 
     */
    IResult listUserLocators();
    
    /**
     * List users from the user database
     * @param start
     * @param count
     * @return
     */
    IResult listUsers(int start, int count);
    
    
    
    void shutDown();
}
