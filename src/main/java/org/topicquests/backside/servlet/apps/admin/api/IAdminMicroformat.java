/**
 * 
 */
package org.topicquests.backside.servlet.apps.admin.api;

import org.topicquests.backside.servlet.api.ICredentialsMicroformat;

/**
 * @author park
 *
 */
public interface IAdminMicroformat extends ICredentialsMicroformat {

	/** Verbs */
	public static final String
		REMOVE_USER				= "RemUser",  //POST
		UPDATE_USER_ROLE		= "UpdUsRol", //POST
		UPDATE_USER_EMAIL		= "UpdUsEma", //POST
		UPDATE_USER_PASSWORD	= "UpdUsPwd", //POST
		/** subject to common modifiers */
		LIST_USERS				= "ListUsers",	//GET
		LIST_INVITES			= "ListInvites", //GET
		NEW_INVITE				= "NewInvite", //POST
		REMOVE_INVITE			= "RemoveInvite", //POST
		EXISTS_INVITE			= "ExistsInvite"; //GET


}
