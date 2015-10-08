/**
 * 
 */
package org.topicquests.backside.servlet.apps.auth.api;

/**
 * @author park
 *
 */
public interface IAuthMicroformat {
	
	public static final String
		VALIDATE		= "Validate", //GET
		AUTHENTICATE	= "Auth", 	//POST
		LOGOUT			= "LogOut"; //POST
}
