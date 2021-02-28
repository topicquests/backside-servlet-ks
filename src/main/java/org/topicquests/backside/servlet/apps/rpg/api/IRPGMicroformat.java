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
		REMOVE_MEMBER		= "RemoveMember",
		SET_CURRENT_QUEST	= "SetCurQuest",
		SET_CURRENT_ROOT_ID	= "SetCurRootId",
		JOIN_QUEST			= "JoinQuest";
	
	// PROPERTIES
	public static final String
		GUILD_LOCATOR		= "gLoc",
		QUEST_LOCATOR		= "qLoc",
		ROOT_ID				= "rnLoc",
		MEMBER_ID			= "mId",
		GUILD_MEMBER_LIST	= "GuildMemberList", //rpg.json
		CURRENT_QUEST_ID	= "GuildCurrentQuest", //rpg.json
		CURRENT_ROOT_NODE_ID= "GuildCurrentRootNode"; // rpg.json
}
