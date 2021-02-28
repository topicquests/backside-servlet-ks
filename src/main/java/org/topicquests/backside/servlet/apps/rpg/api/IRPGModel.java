/**
 * 
 */
package org.topicquests.backside.servlet.apps.rpg.api;

import org.topicquests.support.api.IResult;
import org.topicquests.ks.api.ITicket;

/**
 * @author jackpark
 *
 */
public interface IRPGModel {
	public static final String
		LEADER_SUFFIX		= "_LDR",
		MEMBER_SUFFIX		= "_MBR";

	/**
	 * Manipulate User Credentials
	 * @param guildLocator
	 * @param leaderId
	 * @param credentials
	 * @return
	 */
	IResult addLeaderToGuild(String guildLocator, String leaderId, ITicket credentials);
	
	IResult removeLeaderFromGuild(String guildLocator, String leaderId, ITicket credentials);
	
	IResult addMemberToGuild(String guildLocator, String memberId, ITicket credentials);

	IResult removeMemberFromGuild(String guildLocator, String memberId, ITicket credentials);

	/**
	 * If <code>questLocator</code> == <code>null</code> this effectively
	 * exits a given quest
	 * @param guildLocator
	 * @param questLocator can be <code>null</code> or ""
	 * @param credentials
	 * @return
	 */
	IResult setCurrentQuestId(String guildLocator, String questLocator, ITicket credentials);
	
	/**
	 * 
	 * @param guildLocator
	 * @param rootNodeLocator can be <code>null</code> or ""
	 * @param credentials
	 * @return
	 */
	IResult setCurrentRootNodeId(String guildLocator, String rootNodeLocator, ITicket credentials);
	
	/**
	 * 
	 * @param guildLocator
	 * @param questLocator cannot be <code>null</code>
	 * @param credentials
	 * @return
	 */
	IResult joinQuest(String guildLocator, String questLocator, ITicket credentials);
	
	void shutDown();
}
