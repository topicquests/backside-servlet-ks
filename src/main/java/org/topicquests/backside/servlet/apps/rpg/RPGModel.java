/**
 * 
 */
package org.topicquests.backside.servlet.apps.rpg;
import java.util.*;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.rpg.api.IRPGMicroformat;
import org.topicquests.backside.servlet.apps.rpg.api.IRPGModel;
import org.topicquests.backside.servlet.apps.tm.api.ISocialBookmarkLegend;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

import org.topicquests.ks.tm.api.IDataProvider;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.api.INodeTypes;
import org.topicquests.ks.tm.api.IProxy;

/**
 * @author jackpark
 *
 */
public class RPGModel implements IRPGModel {
	private ServletEnvironment environment;
	private IDataProvider topicMap;
	private IUserModel userModel;

	/**
	 * 
	 */
	public RPGModel(ServletEnvironment env) {
		environment = env;
		userModel = environment.getUserModel();
		topicMap = environment.getTopicMapEnvironment().getDataProvider();
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
		IResult result = fetchTopic(guildLocator, credentials);
		IProxy g = (IProxy)result.getResultObject();
		if (g != null) {
			result = fetchUser(leaderId);
			ITicket t = (ITicket)result.getResultObject();
			//TODO
		}
		return result;
	}

	@Override
	public IResult removeLeaderFromGuild(String guildLocator, String leaderId, ITicket credentials) {
		IResult result = fetchTopic(guildLocator, credentials);
		IProxy g = (IProxy)result.getResultObject();
		if (g != null) {
			String ownerId = g.getCreatorId();
			if (ownerId.equals(leaderId)) {
				result.addErrorString("CANNOT REMOVE GUILD'S OWNER");
			} else {
				result = fetchUser(leaderId);
				ITicket t = (ITicket)result.getResultObject();
				JSONObject struct = this.makeUserStruct(guildLocator, leaderId, "leader");
				//TODO
			}
		}
		return result;
	}

	@Override
	public IResult addMemberToGuild(String guildLocator, String memberId, ITicket credentials) {
		IResult result = fetchTopic(guildLocator, credentials);
		IProxy g = (IProxy)result.getResultObject();
		if (g != null) {
			result = fetchUser(memberId);
			ITicket t = (ITicket)result.getResultObject();
			JSONObject struct = this.makeUserStruct(guildLocator, memberId, "member");
			//TODO
		}
		return result;
	}

	@Override
	public IResult removeMemberFromGuild(String guildLocator, String memberId, ITicket credentials) {
		IResult result = fetchTopic(guildLocator, credentials);
		IProxy g = (IProxy)result.getResultObject();
		if (g != null) {
			//Sanity check
			String ownerId = g.getCreatorId();
			if (ownerId.equals(memberId)) {
				result.addErrorString("CANNOT REMOVE GUILD'S OWNER");
			} else {
				result = fetchUser(memberId);
				ITicket t = (ITicket)result.getResultObject();
				//TODO
			}
		}
		return result;
	}

	@Override
	public IResult setCurrentQuestId(String guildLocator, String questLocator, ITicket credentials) {
		IResult result = fetchTopic(guildLocator, credentials);
		IProxy g = (IProxy)result.getResultObject();
		if (g != null) {
			if (questLocator != null && !questLocator.equals(""))
				g.setProperty(IRPGMicroformat.SET_CURRENT_QUEST, questLocator);
			else
				g.getData().remove(IRPGMicroformat.SET_CURRENT_QUEST);
			//result = topicMap.updateNode(g, true);
		}
		return result;
	}

	@Override
	public IResult setCurrentRootNodeId(String guildLocator, String rootNodeLocator, ITicket credentials) {
		IResult result = fetchTopic(guildLocator, credentials);
		IProxy g = (IProxy)result.getResultObject();
		if (g != null) {
			if (rootNodeLocator != null && !rootNodeLocator.equals(""))
				g.setProperty(IRPGMicroformat.SET_CURRENT_ROOT_ID, rootNodeLocator);
			else
				g.getData().remove(IRPGMicroformat.SET_CURRENT_ROOT_ID);
			//result = topicMap.updateNode(g, true);
		}
		return result;
	}

	@Override
	public IResult joinQuest(String guildLocator, String questLocator, ITicket credentials) {
		IResult result = fetchTopic(guildLocator, credentials);
		IProxy g = (IProxy)result.getResultObject();
		if (g != null) {
			result = fetchTopic(questLocator, credentials);
			IProxy q = (IProxy)result.getResultObject();
			if (q != null) {
				//TODO forge a pivot between the two
				//TODO forge a Biography node for the pivot relation
			}
		}
		return result;
	}


	private IResult fetchTopic(String locator, ITicket credentials) {
		IResult result = topicMap.getNode(locator, credentials);
		return result;
	}
		
	private IResult fetchUser(String userId) {
		return userModel.getTicketById(userId);
	}
	
	/**
	 * does not return <code>null</code>
	 * @param guild
	 * @return
	 */
	private List<JSONObject> getGuildMemberList(IProxy guild) {
		List<JSONObject>result = (List<JSONObject>)guild.getProperty(IRPGMicroformat.GUILD_MEMBER_LIST);
		if (result == null)
			result = new ArrayList<JSONObject>();
		return result;
	}
	/**
	 * 
	 * @param guildId
	 * @param userId
	 * @param role one of ("leader" or "member")
	 * @return
	 */
	private JSONObject makeUserStruct(String guildId, String userId, String role) {
		JSONObject result = new JSONObject();
		result.put("userId", userId);
		result.put("guildId", guildId);
		result.put("role", role);
		return result;
	}


}
