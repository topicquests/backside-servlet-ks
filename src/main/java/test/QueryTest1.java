/**
 * 
 */
package test;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.INodeTypes;
import org.topicquests.es.util.JSONQueryUtil;;

/**
 * @author park
 *
 */
public class QueryTest1 {
	private ServletEnvironment environment;
	private SystemEnvironment tmEnvironment;
	private JSONQueryUtil queryUtil;
	private ITicket credentials;
	/**
	 * 
	 */
	public QueryTest1(ServletEnvironment env) {
		environment = env;
		tmEnvironment = environment.getTopicMapEnvironment();
		queryUtil = new JSONQueryUtil();
		credentials = new TicketPojo();
		credentials.setUserLocator(ITQCoreOntology.SYSTEM_USER);
		runTest();
	}
	
	private void runTest() {
		JSONObject result = new JSONObject();
		JSONArray mustterms = queryUtil.createJA();
		mustterms.add(queryUtil.term(ITQCoreOntology.RESOURCE_URL_PROPERTY, "http://worldmap.harvard.edu/"));
		mustterms.add(queryUtil.term(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE, INodeTypes.BOOKMARK_TYPE));
		result.put("query", queryUtil.bool(queryUtil.must(mustterms)));
		System.out.println("A "+result.toJSONString());
		IResult r = tmEnvironment.getDatabase().runQuery(result.toJSONString(), 0, -1, credentials);
		System.out.println("B "+r.getErrorString()+" | "+r.getResultObject());
		
	}

}
