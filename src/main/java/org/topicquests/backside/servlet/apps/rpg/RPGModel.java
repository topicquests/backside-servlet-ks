/**
 * 
 */
package org.topicquests.backside.servlet.apps.rpg;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.rpg.api.IRPGModel;
import org.topicquests.backside.servlet.apps.tm.api.ISocialBookmarkLegend;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.INodeTypes;

/**
 * @author jackpark
 *
 */
public class RPGModel implements IRPGModel {
	private ServletEnvironment environment;
	private IUserModel userModel;

	/**
	 * 
	 */
	public RPGModel(ServletEnvironment env) {
		environment = env;
		userModel = environment.getUserModel();
		environment.logDebug("RPGModel started");
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.rpg.api.IRPGModel#shutDown()
	 */
	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public IResult addLeaderToGuild(String guildLocator, String leaderId, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult removeLeaderFromGuild(String guildLocator, String leaderId, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult addMemberToGuild(String guildLocator, String memberId, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult removeMemberFromGuild(String guildLocator, String memberId, ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

}
