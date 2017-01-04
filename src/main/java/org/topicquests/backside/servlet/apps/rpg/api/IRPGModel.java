/**
 * 
 */
package org.topicquests.backside.servlet.apps.rpg.api;

import org.topicquests.common.api.IResult;
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

	void shutDown();
}
