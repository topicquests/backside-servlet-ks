/**
 * 
 */
package test;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.tm.api.IStructuredConversationModel;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITQDataProvider;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.INodeTypes;
import org.topicquests.ks.tm.api.ISubjectProxy;
import org.topicquests.ks.tm.api.ISubjectProxyModel;

/**
 * @author jackpark
 *
 */
public class ConversationTest1 {
	private ServletEnvironment environment;
	private ITQDataProvider topicMap;
	private IStructuredConversationModel conversationModel;
	private ITicket credentials;
	private final String
		MAP_LOC			= "MyFirstMap",
		QUESTION_LOC	= "MyFirstQuestion",
		ANSWER_LOC		= "MyFirstAnswer";

	/**
	 * 
	 */
	public ConversationTest1() {
		try {
			environment = new ServletEnvironment(false);
			topicMap = environment.getTopicMapEnvironment().getDatabase();
			conversationModel = environment.getConversationModel();
			credentials = new TicketPojo(ITQCoreOntology.SYSTEM_USER);
			runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Steps:<br/>
	 * <li>Create a Map node</li>
	 * <li>Create a Question node with map as parent</li>
	 * <li>Createan Answer node with Question as parent</li>
	 * </p>
	 */
	private void runTest() {
		IResult r = conversationModel.newConversationNode(INodeTypes.CONVERSATION_MAP_TYPE, null, null, 
				MAP_LOC, "My first map", null, "en", "joe", false);
		ISubjectProxy sp = (ISubjectProxy)r.getResultObject();
		System.out.println("AAA "+r.getErrorString()+" | "+sp.toJSONString());
		r = conversationModel.newConversationNode(INodeTypes.ISSUE_TYPE, MAP_LOC, MAP_LOC, 
				QUESTION_LOC, "Why is the sky blue?", null, "en", "joe", false);
		sp = (ISubjectProxy)r.getResultObject();
		System.out.println("BBB"+r.getErrorString()+" | "+sp.toJSONString());
		r = conversationModel.newConversationNode(INodeTypes.POSITION_TYPE, QUESTION_LOC, MAP_LOC, 
				ANSWER_LOC, "Nobody really knows.", null, "en", "joe", false);
		sp = (ISubjectProxy)r.getResultObject();
		System.out.println("CCC"+r.getErrorString()+" | "+sp.toJSONString());
		environment.shutDown();
	}

}
