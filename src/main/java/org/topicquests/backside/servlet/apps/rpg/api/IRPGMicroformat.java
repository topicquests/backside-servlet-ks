/**
 * 
 */
package org.topicquests.backside.servlet.apps.rpg.api;


/**
 * @author jackpark
 *
 */
public interface IRPGMicroformat {

	//VERBS
	public static final String
		ADD_LEADER			= "AddLeader",
		REMOVE_LEADER		= "RemoveLeader",
		ADD_MEMBER			= "AddMember",
		REMOVE_MEMBER		= "RemoveMember";
	
	// PROPERTIES
	public static final String
		GUILD_LOCATOR		= "gLoc",
		MEMBER_ID			= "mId";
}
