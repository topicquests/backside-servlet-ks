/**
 *
 */
package test;
import java.util.*;
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
public class FullTextSearchTest2 {
	private ServletEnvironment environment;
	private SystemEnvironment tmEnvironment;
	private ProviderEnvironment database;
	private Client client;
	private ElasticQueryDSL queryDSL;
	private final String
		QUERY = "PositionNode type", //"\"PositionNode type\"",
		FIELD1 = "label",
		FIELD2 = "details",
		INDEX = "topics";

	/**
	 *
	 */
	public FullTextSearchTest2(ServletEnvironment env) {
		environment = env;
		tmEnvironment = environment.getTopicMapEnvironment();
		database = tmEnvironment.getProvider();
		client = database.getClient();
		queryDSL = environment.getQueryDSL();
		runTest();
	}
	void runTest() {
		List<String>fields = new ArrayList<String>();
		fields.add(FIELD1);
		fields.add(FIELD2);
		String theQuery = queryDSL.fullTextQueryMultipleFields(QUERY, true, fields, 0, 10);
		System.out.println("AAA "+theQuery);
		IResult r = client.listObjectsByQuery(theQuery, INDEX );
		System.out.println("BBB "+r.getErrorString()+" | "+r.getResultObject());
		if (r.hasError()) System.exit(1);
	}

}

/**
 * This one, with \" \" crashed
 * Note that it got "phrase"
AAA {
  "from" : 0,
  "size" : 10,
  "query" : {
    "bool" : {
      "should" : [ {
        "match" : {
          "label" : {
            "query" : "\"PositionNode type\"",
            "type" : "phrase"
          }
        }
      }, {
        "match" : {
          "details" : {
            "query" : "\"PositionNode type\"",
            "type" : "phrase"
          }
        }
      } ]
    }
  }
}

* This one without \" \" did not get phrase but didn't crash
* results are inaccurate in terms of phrase query
AAA {
  "from" : 0,
  "size" : 10,
  "query" : {
    "bool" : {
      "should" : [ {
        "match" : {
          "label" : {
            "query" : "PositionNode type",
            "type" : "boolean"
          }
        }
      }, {
        "match" : {
          "details" : {
            "query" : "PositionNode type",
            "type" : "boolean"
          }
        }
      } ]
    }
  }
}
BBB  | [{"crtr":"SystemUser","_ver":"1444584909656","lEdDt":"2015-10-11T10:35:09-07:00","label":"Property type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:09-07:00","sbOf":"TypeType","lox":"PropertyType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Property type","lIco":"\/images\/cogwheel.png"}, {"crDt":"2015-11-11T11:04:52-08:00","crtr":"SystemUser","lox":"TypeType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"_ver":"1447268692935","lEdDt":"2015-11-11T11:04:52-08:00","details":"Topic Map root type","label":"Type type","lIco":"\/images\/cogwheel.png","isFdrtd":false}, {"crtr":"SystemUser","_ver":"1444584929773","lEdDt":"2015-10-11T10:35:29-07:00","label":"Tag Bookmark Relation Type","isFdrtd":false,"trCl":["TypeType","RelationType"],"crDt":"2015-10-11T10:35:29-07:00","sbOf":"RelationType","lox":"TagBookmarkRelationType","sIco":"\/images\/snowflake_sm.png","isPrv":false,"details":"ISocialBookmarkLegend extension Tag Bookmark relation type","lIco":"\/images\/snowflake.png"}, {"crtr":"SystemUser","_ver":"1444584929912","lEdDt":"2015-10-11T10:35:29-07:00","label":"User Bookmark Relation Type","isFdrtd":false,"trCl":["TypeType","RelationType"],"crDt":"2015-10-11T10:35:29-07:00","sbOf":"RelationType","lox":"UserBookmarkRelationType","sIco":"\/images\/snowflake_sm.png","isPrv":false,"details":"ISocialBookmarkLegend extension User Bookmark relation type","lIco":"\/images\/snowflake.png"}, {"crtr":"SystemUser","_ver":"1444584910005","lEdDt":"2015-10-11T10:35:10-07:00","label":"ResourceType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:10-07:00","sbOf":"TypeType","lox":"ResourceType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Resource type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584909836","lEdDt":"2015-10-11T10:35:09-07:00","label":"Role type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:09-07:00","sbOf":"TypeType","lox":"RoleType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Role type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1447201184663","lEdDt":"2015-11-10T16:19:44-08:00","label":"Class type","isFdrtd":false,"crDt":"2015-11-10T16:19:44-08:00","trCl":["TypeType"],"sbOf":"TypeType","lox":"ClassType","isPrv":false,"sIco":"\/images\/cogwheel_sm.png","details":"Topic Map upper Class type","lIco":"\/images\/cogwheel.png"},
        {"crtr":"SystemUser","_ver":"1444584910121","lEdDt":"2015-10-11T10:35:10-07:00","label":"LegendType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:10-07:00","sbOf":"TypeType","lox":"LegendType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Legend type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584909917","lEdDt":"2015-10-11T10:35:09-07:00","label":"UserType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:09-07:00","sbOf":"TypeType","lox":"UserType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper User type","lIco":"\/images\/cogwheel.png"}, {"crtr":"SystemUser","_ver":"1444584910225","lEdDt":"2015-10-11T10:35:10-07:00","label":"ScopeType type","isFdrtd":false,"trCl":["TypeType"],"crDt":"2015-10-11T10:35:10-07:00","sbOf":"TypeType","lox":"ScopeType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper Scope type","lIco":"\/images\/cogwheel.png"}]
* This one uses a revised API to specify isPhraseQuery
* is Accurate
AAA {
  "from" : 0,
  "size" : 10,
  "query" : {
    "bool" : {
      "should" : [ {
        "match" : {
          "label" : {
            "query" : "PositionNode type",
            "type" : "phrase"
          }
        }
      }, {
        "match" : {
          "details" : {
            "query" : "PositionNode type",
            "type" : "phrase"
          }
        }
      } ]
    }
  }
}
BBB  | [{"crtr":"SystemUser","_ver":"1444584914844","lEdDt":"2015-10-11T10:35:14-07:00","label":"PositionNodeType","isFdrtd":false,"trCl":["TypeType","ClassType","NodeType"],"crDt":"2015-10-11T10:35:14-07:00","sbOf":"NodeType","lox":"PositionNodeType","sIco":"\/images\/cogwheel_sm.png","isPrv":false,"details":"Topic Map upper PositionNode type; a response to an Issue\/Question","lIco":"\/images\/cogwheel.png"}]

 */
