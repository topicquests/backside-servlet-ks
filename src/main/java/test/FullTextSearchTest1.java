/**
 *
 */
package test;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.util.ElasticQueryDSL;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.node.provider.Client;
import org.topicquests.node.provider.ProviderEnvironment;

/**
 * @author park
 *
 */
public class FullTextSearchTest1 {
	private ServletEnvironment environment;
	private SystemEnvironment tmEnvironment;
	private ProviderEnvironment database;
	private Client client;
	private ElasticQueryDSL queryDSL;
	private final String
		QUERY = "TypeType",
		FIELD = "sbOf", //list all classes which are subOf TypeType
		INDEX = "topics";

	/**
	 *
	 */
	public FullTextSearchTest1(ServletEnvironment env) {
		environment = env;
		tmEnvironment = environment.getTopicMapEnvironment();
		database = tmEnvironment.getProvider();
		client = database.getClient();
		queryDSL = environment.getQueryDSL();
		runTest();
	}

	void runTest() {
		String theQuery = queryDSL.fullTextQuerySingleField(QUERY, false, FIELD, 0, 10);
		System.out.println("AAA "+theQuery);
		IResult r = client.listObjectsByQuery(theQuery, INDEX );
		System.out.println("BBB "+r.getErrorString()+" | "+r.getResultObject());
		if (r.hasError()) System.exit(1);
	}
}
/**
AAA {
  "from" : 0,
  "size" : 10,
  "query" : {
    "match" : {
      "sbOf" : {
        "query" : "TypeType",
        "type" : "boolean"
      }
    }
  }
}
BBB  | [{"crtr":"SystemUser","_ver":"1444584909656","lEdDt":"2015-10-11T10:35:09-07:00","label":"Property type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:09-07:00","sbOf":"TypeType","lox":"PropertyType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Property type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584910121","lEdDt":"2015-10-11T10:35:10-07:00","label":"LegendType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:10-07:00","sbOf":"TypeType","lox":"LegendType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Legend type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584909917","lEdDt":"2015-10-11T10:35:09-07:00","label":"UserType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:09-07:00","sbOf":"TypeType","lox":"UserType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper User type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584910225","lEdDt":"2015-10-11T10:35:10-07:00","label":"ScopeType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:10-07:00","sbOf":"TypeType","lox":"ScopeType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Scope type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584910005","lEdDt":"2015-10-11T10:35:10-07:00","label":"ResourceType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:10-07:00","sbOf":"TypeType","lox":"ResourceType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Resource type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584909836","lEdDt":"2015-10-11T10:35:09-07:00","label":"Role type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:09-07:00","sbOf":"TypeType","lox":"RoleType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Role type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1447201184663","lEdDt":"2015-11-10T16:19:44-08:00","label":"Class type","isFdrtd":false,"crDt":"2015-11-10T16:19:44-08:00","trCl":["TypeType"],"sbOf":"TypeType","lox":"ClassType","isPrv":false,"sIco":"\/images\/cogwheel_sm.png","details":"Topic Map upper Class type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584909736","lEdDt":"2015-10-11T10:35:09-07:00","label":"Rule type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:09-07:00","sbOf":"TypeType","lox":"RuleType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Rule type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1447201187685","lEdDt":"2015-11-10T16:19:47-08:00","label":"Relation type","isFdrtd":false,"crDt":"2015-11-10T16:19:47-08:00","trCl":["TypeType"],"sbOf":"TypeType","lox":"RelationType","isPrv":false,"sIco":"\/images\/cogwheel_sm.png","details":"Topic Map upper Relation type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1447201190703","lEdDt":"2015-11-10T16:19:50-08:00","label":"Ontology type","isFdrtd":false,"crDt":"2015-11-10T16:19:50-08:00","trCl":["TypeType"],"sbOf":"TypeType","lox":"OntologyType","isPrv":false,"sIco":"\/images\/cogwheel_sm.png","details":"Topic Map upper Ontology type","lIco":"\/images\/cogwheel.png"}]

 */
