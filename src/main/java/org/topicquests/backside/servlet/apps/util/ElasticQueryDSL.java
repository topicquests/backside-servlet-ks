/**
 * 
 */
package org.topicquests.backside.servlet.apps.util;

import java.util.*;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.ks.api.ITQCoreOntology;

/**
 * @author park
 * CREATE a variety of Query Strings appropriate to
 * given index, type, and query type
 */
public class ElasticQueryDSL {
	private ServletEnvironment environment;

	/**
	 * 
	 */
	public ElasticQueryDSL(ServletEnvironment env) {
		environment = env;
	}

	public String fullTextQuerySingleField(String textQuery, boolean isPhraseQuery,
					String fieldName, int start, int count) {
		QueryBuilder qb;
		if (!isPhraseQuery)
			qb = QueryBuilders.matchQuery(fieldName, textQuery);
		else
			qb = QueryBuilders.matchPhraseQuery(fieldName, textQuery);
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(qb);
		ssb.from(start);
		if (count > -1)
			ssb.size(count);
		
		return ssb.toString();
	}
	
	public String fullTextQueryMultipleFields(String textQuery, boolean isPhraseQuery,
			List<String> fieldNames, int start, int count) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		String fn;
		Iterator<String>itr = fieldNames.iterator();
		while (itr.hasNext()) {
			fn = itr.next();
			if (!isPhraseQuery)
				qb.should(QueryBuilders.matchQuery(fn, textQuery));
			else
				qb.should(QueryBuilders.matchPhraseQuery(fn, textQuery));
		}
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(qb);
		ssb.from(start);
		if (count > -1)
			ssb.size(count);
		return ssb.toString();
	}
	
	/**
	 * <p>e.g. "W?F*HW"
	 * where "?" matches any character
	 * where * matches zero or more characters</p>
	 * <p>Note, avoid using terms which begin with a wildcard,
	 * e.g. *foo</p>
	 * @param textQuery
	 * @param fieldName
	 * @param start
	 * @param count
	 * @return
	 * @see https://www.elastic.co/guide/en/elasticsearch/guide/current/_wildcard_and_regexp_queries.html
	 */
	public String wildCardTextQuerySingleField(String textQuery, String fieldName, int start, int count) {
		QueryBuilder qb  = QueryBuilders.wildcardQuery(fieldName, textQuery);
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(qb);
		ssb.from(start);
		if (count > -1)
			ssb.size(count);
		return ssb.toString();
	}
	
	/**
	 * Return all hits which begin with <code>prefix</code>, e.g. "Wh"
	 * @param prefix
	 * @param fieldName
	 * @param start
	 * @param count
	 * @return
	 */
	public String prefixTextQuerySingleField(String prefix, String fieldName, int start, int count) {
		QueryBuilder qb  = QueryBuilders.prefixQuery(fieldName, prefix);
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(qb);
		ssb.from(start);
		if (count > -1)
			ssb.size(count);
		return ssb.toString();
		
	}
		
	
	/**
	 * Here to establish QueryBuilder patterns
	 */
	void test() {
		SearchSourceBuilder b;
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		QueryBuilder qbmm = QueryBuilders.multiMatchQuery("name", "fieldNames");
		QueryBuilder qbm = QueryBuilders.matchQuery("name", "text");
		QueryBuilder qbmp = QueryBuilders.matchPhraseQuery("name", "text");
		QueryBuilder qb1 = QueryBuilders.termQuery(ITQCoreOntology.TUPLE_SUBJECT_PROPERTY, "");
		QueryBuilder qb2 = QueryBuilders.termQuery(ITQCoreOntology.TUPLE_OBJECT_PROPERTY, "");
		QueryBuilder qb3 = QueryBuilders.termQuery(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE, "");
		qb.must(qb3);
		qb.should(qb1);
		qb.should(qb2);
		QueryBuilder partial = QueryBuilders.regexpQuery("name", "regexp");
		QueryBuilder wildcard = QueryBuilders.wildcardQuery("name", "query");
	}
	//*/
}
