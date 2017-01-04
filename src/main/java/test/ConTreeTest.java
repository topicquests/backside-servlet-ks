/**
 * 
 */
package test;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.tm.TopicMapModel;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.api.INodeTypes;

/**
 * @author jackpark
 *
 */
public class ConTreeTest {
	private ServletEnvironment environment;
	private ITopicMapModel model;
	private ITicket credentials;
	private final String lox = "b921a18c-5677-4ea6-b076-f6f11dec3e9f";
	
	/**
	 * 
	 */
	public ConTreeTest(ServletEnvironment env) {
		environment = env;
		model = new TopicMapModel(environment);
		credentials = new TicketPojo(ITQCoreOntology.SYSTEM_USER);
		IResult r = model.collectParentChildTree(lox, lox, credentials);
		System.out.println("A "+r.getErrorString()+" "+r.getResultObject());
	}

}
/**
{
	"root": {
		"cNL": [{
			"contextLocator": "b921a18c-5677-4ea6-b076-f6f11dec3e9f",
			"smallImagePath": "\/images\/ibis\/issue_sm.png",
			"subject": "Third Test Question",
			"locator": "25238ebf-7735-49ff-8bcb-14467da1b30e"
		}],
		"crtr": "joe",
		"pvL": [{
			"relationType": "DocumentCreatorRelationType",
			"documentLocator": "joe",
			"relationLocator": "b921a18c-5677-4ea6-b076-f6f11dec3e9fDocumentCreatorRelationTypejoe",
			"documentLabel": "Joe Sixpack",
			"documentType": "UserType",
			"relationLabel": "DocumentCreatorRelationType",
			"documentSmallIcon": "\/images\/person_sm.png"
		}],
		"_ver": "1479348013803",
		"lEdDt": "2016-11-16T18:00:13-08:00",
		"label": ["First post"],
		"inOf": "BlogNodeType",
		"crDt": "2016-11-13T17:32:09-08:00",
		"trCl": ["TypeType", "ClassType", "NodeType", "BlogNodeType"],
		"lox": "b921a18c-5677-4ea6-b076-f6f11dec3e9f",
		"sIco": "\/images\/publication_sm.png",
		"isPrv": false,
		"details": ["We really need to be able to say something useful."],
		"lIco": "\/images\/publication.png"
	},
	"kids": [{
		"root": {
			"cNL": [],
			"crtr": "joe",
			"pvL": [{
				"relationType": "DocumentCreatorRelationType",
				"documentLocator": "joe",
				"relationLocator": "25238ebf-7735-49ff-8bcb-14467da1b30eDocumentCreatorRelationTypejoe",
				"documentLabel": "Joe Sixpack",
				"documentType": "UserType",
				"relationLabel": "DocumentCreatorRelationType",
				"documentSmallIcon": "\/images\/person_sm.png"
			}],
			"_ver": "1479182813508",
			"lEdDt": "2016-11-14T20:06:53-08:00",
			"label": ["Third Test Question"],
			"inOf": "IssueNodeType",
			"pNL": [{
				"contextLocator": "b921a18c-5677-4ea6-b076-f6f11dec3e9f",
				"smallImagePath": "\/images\/publication_sm.png",
				"subject": "First post",
				"locator": "b921a18c-5677-4ea6-b076-f6f11dec3e9f"
			}],
			"crDt": "2016-11-14T20:06:53-08:00",
			"trCl": ["TypeType", "ClassType", "NodeType", "IssueNodeType"],
			"lox": "25238ebf-7735-49ff-8bcb-14467da1b30e",
			"sIco": "\/images\/ibis\/issue_sm.png",
			"isPrv": false,
			"details": ["Sup?"],
			"lIco": "\/images\/ibis\/issue.png"
		}
	}]
}
 */
