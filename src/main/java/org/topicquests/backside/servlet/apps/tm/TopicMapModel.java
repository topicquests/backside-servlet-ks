/*
 * Copyright 2015, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.backside.servlet.apps.tm;

import java.util.*;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.apps.BaseModel;
import org.topicquests.backside.servlet.apps.tm.api.IConversationTreeStruct;
import org.topicquests.backside.servlet.apps.tm.api.ISocialBookmarkLegend;
import org.topicquests.backside.servlet.apps.tm.api.ITagModel;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
//import org.topicquests.es.util.JSONQueryUtil;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ICoreIcons;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.tm.api.IDataProvider;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.Proxy;
import org.topicquests.ks.tm.api.IChildStruct;
import org.topicquests.ks.api.INodeTypes;
import org.topicquests.ks.tm.api.IParentChildContainer;
import org.topicquests.ks.tm.api.IProxy;
import org.topicquests.ks.tm.api.IProxyModel;

/**
 * @author park
 *
 */
public class TopicMapModel extends BaseModel implements ITopicMapModel {
	private ITagModel tagModel;
	private ITicket systemCredentials;
//	private JSONQueryUtil queryUtil;

	/**
	 * 
	 */
	public TopicMapModel(ServletEnvironment env) {
		super(env);
		tagModel = new TagModel(environment);
		systemCredentials = new TicketPojo(ITQCoreOntology.SYSTEM_USER);
//		queryUtil = new JSONQueryUtil();
	}
	

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#putTopic(net.minidev.json.JSONObject, org.topicquests.model.api.ITicket)
	 * /
	@Override
	public IResult putTopic(JSONObject topic) {
		IProxy n = new Proxy(topic);
		IResult result = topicMap.putNode(n);
		return result;
	}

/*	@Override
	public IResult updateTopic(JSONObject topic, boolean checkVersion) {
		System.out.println("TopicMapModel.updateTopic "+topic.toJSONString());
		return topicMap.updateProxyFromJSON(topic, checkVersion);
	}*/

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#getTopic(java.lang.String, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult getTopic(String topicLocator, ITicket credentials) {
		return topicMap.getNode(topicLocator, credentials);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#removeTopic(java.lang.String, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult removeTopic(String topicLocator, ITicket credentials) {
		return topicMap.removeNode(topicLocator, credentials);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#query(net.minidev.json.JSONObject, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult query(JSONObject query, ITicket credentials) {
		IResult result = topicMap.runTextQuery(query.toJSONString(), credentials);
		return result;
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IResult listSubclassTopics(String superClassLocator,
			int start, int count, ITicket credentials) {
		IResult result = topicMap.listSubclassNodes(superClassLocator, start, count, credentials);
		return result;
	}
/***
	@Override
	public IResult listAllBlogPosts(int start, int count, ITicket credentials) {
		System.out.println("TopicMapModel.listAllBlogPosts "+start+" "+count);
		final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder t2 = QueryBuilders.termQuery(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE, INodeTypes.BLOG_TYPE);
		searchSourceBuilder.query(t2);
		SortBuilder sb = new FieldSortBuilder(ITQCoreOntology.CREATED_DATE_PROPERTY).order(SortOrder.DESC);
		searchSourceBuilder.sort(sb);
		//searchSourceBuilder.from(start);
		//if (count > -1)
		//	searchSourceBuilder.size(count);
		String q = searchSourceBuilder.toString();
		//IResult result = topicMap.executeQueryBuilder(searchSourceBuilder, credentials);
		IResult result = topicMap.runQuery(q, start, count, credentials);
		System.out.println("LISTALLBLOGPOSTS "+q);
		environment.logDebug("TopicMapModel.listBlogPostsByUser+ "+result.getErrorString()+" | "+result.getResultObject());
		//TopicMapModel.getTopicByURL+  | [org.topicquests.ks.tm.Proxy@6aab361d, org.topicquests.ks.tm.SubjectProxy@1098da46]
		List<Object> lx = (List<Object>)result.getResultObject();
		if (lx != null) {
			if (lx.size() > 0) {
				result.setResultObject(lx);
			} else
				result.setResultObject(null);
		}
		System.out.println("LISTALLBLOGPOSTS+ "+result.getErrorString()+" | "+result.getResultObject());
		return result;	}

	@Override
	public IResult listBlogPostsByUser(String userId, int start, int count,
			ITicket credentials) {
		final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder t1 = QueryBuilders.termQuery(ITQCoreOntology.CREATOR_ID_PROPERTY, userId);
		QueryBuilder t2 = QueryBuilders.termQuery(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE, INodeTypes.BLOG_TYPE);
		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(t1);
		query.must(t2);
		searchSourceBuilder.query(query);
		SortBuilder sb = new FieldSortBuilder(ITQCoreOntology.CREATED_DATE_PROPERTY).order(SortOrder.DESC);
		searchSourceBuilder.sort(sb);
		searchSourceBuilder.from(start);
		if (count > -1)
			searchSourceBuilder.size(count);
		IResult result = topicMap.runQuery(searchSourceBuilder.toString(), 0, -1, credentials);
		environment.logDebug("TopicMapModel.listBlogPostsByUser+ "+result.getErrorString()+" | "+result.getResultObject());
		//TopicMapModel.getTopicByURL+  | [org.topicquests.ks.tm.SubjectProxy@6aab361d, org.topicquests.ks.tm.SubjectProxy@1098da46]
		List<Object> lx = (List<Object>)result.getResultObject();
		if (lx != null) {
			if (lx.size() > 0) {
				result.setResultObject(lx);
			} else
				result.setResultObject(null);
		}
		System.out.println("LISTBLOGSBYUSER+ "+result.getErrorString()+" | "+result.getResultObject());
		return result;
	}
**/
	@Override
	public IResult listInstanceTopics(String typeLocator, int start, int count, ITicket credentials) {
		IResult result = topicMap.listInstanceNodes(typeLocator, start, count, credentials);
		return result;
	}

	@Override
	public IResult listTopicsByKeyValue(String propertyKey, String value,
			int start, int count, ITicket credentials) {
		return  topicMap.listNodesByKeyValuePair(propertyKey, value, start, count, null, null, credentials);
	}

	@Override
	public IResult listUserTopics(int start, int count, ITicket credentials) {
		IResult result = topicMap.listInstanceNodes(ITQCoreOntology.USER_TYPE, start, count, credentials);
		return result;
	}

	@Override
	public IResult listTreeChildNodesJSON(String rootNodeLocator,
			String contextLocator, ITicket credentials) {
		IResult r = topicMap.getNode(rootNodeLocator, credentials);
		IProxy root = (IProxy)r.getResultObject();
		IResult result = _listTreeChildNodesJSON(root, contextLocator, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}
	
	/**
	 * Returns a {@link JSONObject} containing {lox:node} pairs
	 * @param root
	 * @param contextLocator
	 * @param credentials
	 * @return
	 */
	private IResult _listTreeChildNodesJSON(IProxy root, String contextLocator, ITicket credentials) {
		System.out.println("_list "+root.toJSONString());
		IResult result = new ResultPojo();
		List<IProxy> kids = ((IParentChildContainer)root).listChildNodes(contextLocator, credentials);
		result.setResultObject(kids);
		return result;
	}

	/**
	 * Recursive walk down a parent-child tree to populate <code>parent</code>
	 * @param parent
	 * @param root
	 * @param contextLocator
	 * @param credentials
	 * @return
	 */
	private IResult _collectParentChildTree(IConversationTreeStruct parent, IProxy root, String contextLocator, ITicket credentials) {
		IResult result = new ResultPojo();
		IResult r = _listTreeChildNodesJSON(root, contextLocator, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		List<IProxy> snappers = (List<IProxy>)r.getResultObject();
		//For each kid:
		// create a ChildNodeStruct
		// recurse to fill that kid's tree branches
		// add that ChildNodeStruct to parent
		if (snappers != null && !snappers.isEmpty()) {
			Iterator<IProxy> itr = snappers.iterator();
			JSONObject jo;
			IProxy p;
			String key;
			IConversationTreeStruct snapper;
			while (itr.hasNext()) {
				p = itr.next();
				jo = p.getData();
				snapper = new ConTreeStruct(jo);
				r = _collectParentChildTree(snapper, p, contextLocator, credentials);
				parent.addChild(snapper.getData());
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			}
		}
		return result;
	}
	
	@Override 
	public IResult collectParentChildTree(String rootNodeLocator, String contextLocator, ITicket credentials) {
		IResult r = topicMap.getNode(rootNodeLocator, credentials);
		IProxy root = (IProxy)r.getResultObject();
		System.out.println("COLPARCHILTR "+contextLocator+" "+root.toJSONString());
		IConversationTreeStruct p = new ConTreeStruct(root.getData());
		//the following call recursively populates p
		IResult result = _collectParentChildTree(p, root, contextLocator, credentials);
		result.setResultObject(p.getData());
		return result;
	}

	@Override
	public IResult newInstanceNode(JSONObject theTopicShell, ITicket credentials) {
		IResult result  = new ResultPojo();
		environment.logDebug("TopicMapModel.newInstanceNode- "+theTopicShell.toJSONString());
		//{"uId":"SystemUser",
		//"lox":"28bc48f5-ebf7-43e2-b4da-7df44984ecbd","isPrv":"F",
		//"Lang":"en",
		//"details":"Made by devtests\/NodeBulder",
		//"label":"Hello World","inOf":"ClassType"}
		String locator = theTopicShell.getAsString(ITopicMapMicroformat.TOPIC_LOCATOR);
		String typeLocator =  theTopicShell.getAsString(ITopicMapMicroformat.PARENT_LOCATOR);
		String lang = theTopicShell.getAsString(ITopicMapMicroformat.LANGUAGE);
		String userId = theTopicShell.getAsString(ICredentialsMicroformat.USER_ID);
		String label = theTopicShell.getAsString(ITopicMapMicroformat.TOPIC_LABEL);
		String description = theTopicShell.getAsString(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = theTopicShell.getAsString(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = theTopicShell.getAsString(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = theTopicShell.getAsString(ITopicMapMicroformat.IS_PRIVATE);
		String url = theTopicShell.getAsString(ITopicMapMicroformat.URL);
		Object op = theTopicShell.get(ITQCoreOntology.SCOPE_LIST_PROPERTY_TYPE);
		String provenanceLocator = null;
		if (op != null) {
			if (op instanceof String)
				provenanceLocator = (String)op;
			else
				provenanceLocator = ((List<String>)op).get(0); //TODO that's a hack
		}
		environment.logDebug("TopicMapModel.newInstanceNode-x "+locator+" | "+typeLocator+" | "+
				label+" | "+description+" | "+lang+" "+userId);

		//Added feature
		JSONObject extras = null;//(JSONObject)theTopicShell.get(ITopicMapMicroformat.EXTRAS);
		if (extras != null)
			System.out.println("NEWINSTANCE "+extras.toJSONString());
		boolean isPrivate = false;
		IResult r;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		IProxy n = null;
		environment.logDebug("TopicMapModel.newInstanceNode-1 "+locator);
		if (locator != null) {
			environment.logDebug("TopicMapModel.newInstanceNode-a ");
			//First, see if this exists
			r = topicMap.getNode(locator, systemCredentials);
			environment.logDebug("TopicMapModel.newInstanceNode-b "+r.getErrorString()+" | "+r.getResultObject());
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			n = (IProxy)r.getResultObject();
			environment.logDebug("TopicMapModel.newInstanceNode-c "+r.getErrorString()+" | "+r.getResultObject());
			if (r.getResultObject() != null) {
				result.setResultObject(n);
				return result;
			}
			environment.logDebug("TopicMapModel.newInstanceNode-d "+locator+" | "+typeLocator+" | "+
					label+" | "+description+" | "+lang+" "+userId);
			n = nodeModel.newInstanceNode(locator, typeLocator, label, description, lang, userId, provenanceLocator, smallImagePath, largeImagePath, isPrivate);
		} else 
			n = nodeModel.newInstanceNode(typeLocator, label, description, lang, userId, provenanceLocator, smallImagePath, largeImagePath, isPrivate);
		
		environment.logDebug("TopicMapModel.newInstanceNode-2 "+n.toJSONString());
		if (url != null && !url.equals(""))
			n.setURL(url);
	/*		if (extras != null) {
				JSONObject jo = n.getData();
				Iterator<String>itr = extras.keySet().iterator();
				String key;
				while (itr.hasNext()) {
					key = itr.next();
					if (key.equals(ITopicMapMicroformat.ADD_CHILD_NODE)) {
						//THIS is a partial IChildStruct
						// We must fetch its locator (parent)
						// and finish the IChildStruct
						//TODO
						JSONObject kid = (JSONObject)extras.get(key);
						String parent = kid.getAsString("locator");
						String context = kid.getAsString("contextLocator");
						String transclude = kid.getAsString("transcluder");
						String icon, subject;
						r = topicMap.getNode(parent, credentials);
						if (r.hasError())
							result.addErrorString(r.getErrorString());
						IProxy pnt = (IProxy)r.getResultObject();
						icon = pnt.getSmallImage();
						subject = pnt.getLabel(lang);
						//Add a child to the parent
						nodeModel.addChildNode(pnt,context, n.getLocator(), transclude);
						//pnt.addChildNode(context, smallImagePath, n.getLocator(), label, transclude);
						pnt.setLastEditDate(new Date());
						r = topicMap.putNode(pnt);
						if (r.hasError())
							result.addErrorString(r.getErrorString());
						// add parent to new node
						//nodeModel.addParentNode(n, context, icon, parent, subject);
						((IParentChildContainer)n).addParentNode(context, kid.getAsString("node_type"));
						
						
					} else {
						//////////////////////////////
						//NOTE: we are putting values
						// BIG ISSUE: overwriting old values
						//////////////////////////////
						jo.put(key, extras.get(key));
					}
				}
			}*/
		if (n.getLocator() == null) {
			environment.logError("Missing lox for "+typeLocator+" | "+label, null);
		}
		r = topicMap.putNode(n);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		environment.logDebug("TopicMapModel.newInstanceNode-3 "+r.getErrorString()+" | "+n.toJSONString());
		
		r = relateNodeToUser(n, userId, provenanceLocator, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		result.setResultObject(n);
		return result;
		
	}
	


	@Override
	public IResult newSubclassNode(JSONObject theTopicShell, ITicket credentials) {
		String locator = theTopicShell.getAsString(ITopicMapMicroformat.TOPIC_LOCATOR);
		String superClassLocator =  theTopicShell.getAsString(ITopicMapMicroformat.SUPERTYPE_LOCATOR);
		String lang = theTopicShell.getAsString(ITopicMapMicroformat.LANGUAGE);
		String userId = theTopicShell.getAsString(ITQCoreOntology.CREATOR_ID_PROPERTY);
		String label = theTopicShell.getAsString(ITopicMapMicroformat.TOPIC_LABEL);
		String description = theTopicShell.getAsString(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = theTopicShell.getAsString(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = theTopicShell.getAsString(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = theTopicShell.getAsString(ITopicMapMicroformat.IS_PRIVATE);
		Object op = theTopicShell.get(ITQCoreOntology.SCOPE_LIST_PROPERTY_TYPE);
		String provenanceLocator = null;
		if (op != null) {
			if (op instanceof String)
				provenanceLocator = (String)op;
			else
				provenanceLocator = ((List<String>)op).get(0); //TODO that's a hack
		}
		boolean isPrivate = false;
		IResult r;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		IProxy n = null;
		if (locator != null) {
			//First, see if this exists
			r = topicMap.getNode(locator, systemCredentials);
			if (r.getResultObject() != null)
				return r;
			n = nodeModel.newSubclassNode(locator, superClassLocator, label, description, lang, userId, provenanceLocator, smallImagePath, largeImagePath, isPrivate);
		} else
			n = nodeModel.newSubclassNode(superClassLocator, label, description, lang, userId, provenanceLocator, smallImagePath, largeImagePath, isPrivate);
			
		IResult result = topicMap.putNode(n);
		result.setResultObject(n.getData());
		r = relateNodeToUser(n, userId, provenanceLocator, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}

	@Override
	public IResult addFeaturesToNode(JSONObject cargo, ITicket credentials) {
		String lox = cargo.getAsString(ITopicMapMicroformat.TOPIC_LOCATOR);
		String userId = cargo.getAsString(ITQCoreOntology.CREATOR_ID_PROPERTY);
		IResult r = topicMap.getNode(lox, credentials);
		IResult result = new ResultPojo();
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		IProxy n = (IProxy)r.getResultObject();
		boolean isChanged = false;
		if (n != null) {
			Iterator<String> itr = cargo.keySet().iterator();
			String key;
			while (itr.hasNext()) {
				key = itr.next();
				if (!key.equals(ITopicMapMicroformat.TOPIC_LOCATOR) &&
					!key.equals(ITQCoreOntology.CREATOR_ID_PROPERTY)) {
					if (key.equals(ITQCoreOntology.RESOURCE_URL_PROPERTY)) {
						isChanged = true;
						n.setURL(cargo.getAsString(ITQCoreOntology.RESOURCE_URL_PROPERTY));
					}
				}
			}
			n.doUpdate();
			//result = topicMap.updateNode(n, true);
			//Pay attention to OptimisticLock
			
		} else {
			result.addErrorString("Missing Node "+lox);
		}

		return result;
	}

	@Override
	public IResult getNodeTree(String rootLocator, int maxDepth, int start, int count, ITicket credentials) {
		return ((IDataProvider)topicMap).loadTree(rootLocator, maxDepth, start, count, credentials);
	}

	@Override
	public IResult getNodeByURL(String url, ITicket credentials) {		
		return topicMap.getNodeByURL(url, credentials);
	}
	
	@Override
	public IResult getBookmarkByURL(String url, ITicket credentials) {
		QueryBuilder t1 = QueryBuilders.termQuery(ITQCoreOntology.RESOURCE_URL_PROPERTY, url);
		QueryBuilder t2 = QueryBuilders.termQuery(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE, INodeTypes.BOOKMARK_TYPE);
		final BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(t1);
		query.must(t2);
		//TODO add start, count etc to query
		IResult result = topicMap.runTextQuery(query.toString(), credentials);
		environment.logDebug("TopicMapModel.getBookmarkByURL+ "+result.getErrorString()+" | "+result.getResultObject());
		//TopicMapModel.getTopicByURL+  | [org.topicquests.ks.tm.Proxy@6aab361d, org.topicquests.ks.tm.Proxy@1098da46]
		List<Object> lx = (List<Object>)result.getResultObject();
		if (lx != null) {
			if (lx.size() > 0) {
				try {
					IProxy n = (Proxy)(lx.get(0));
					result.setResultObject(n);
				} catch (Exception ex) {
					environment.logError(ex.getMessage(), ex);
					result.addErrorString(ex.getMessage());
					result.setResultObject(null);
				}
			} else
				result.setResultObject(null);
		}
		System.out.println("GETBOOKMARKBYURL+ "+result.getErrorString()+" | "+result.getResultObject());
		return result;
	}
	
	/*@Override
	public IResult addPivot(String topicLocator, String pivotLocator,
			String pivotRelationType, String smallImagePath,
			String largeImagePath, boolean isTransclude, boolean isPrivate,
			ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public IResult addRelation(String sourceLocator, String targetLocator,
			String relationTypeLocator, String smallImagePath,
			String largeImagePath, String provenanceLocator, boolean isTransclude,
			boolean isPrivate, ITicket credentials) {
		IResult result = new ResultPojo();
		IResult r = topicMap.getNode(sourceLocator, credentials);
		if (r.hasError()) result.addErrorString(r.getErrorString());
		IProxy source = (IProxy)r.getResultObject();
		r = topicMap.getNode(targetLocator, credentials);
		if (r.hasError()) result.addErrorString(r.getErrorString());
		IProxy target = (IProxy)r.getResultObject();
		environment.logDebug("RELATING-1 \n"+source.toJSONString()+"\n"+target.toJSONString());
		r = nodeModel.relateExistingNodes(source, target, relationTypeLocator, 
				null, null, smallImagePath, largeImagePath, credentials.getUserLocator(), provenanceLocator, isTransclude, isPrivate);
		if (r.hasError()) result.addErrorString(r.getErrorString());
		result.setResultObject(r.getResultObject());
		System.out.println("TMMODEL.addRel "+sourceLocator+" | "+targetLocator+" | "+relationTypeLocator+" "+r.getResultObject());
		return result;
	}

	@Override
	public IResult findOrProcessTags(String bookmarkLocator, List<String> tagLabels,
			String language, String provenanceLocator, ITicket credentials) {
		//get the bookmark node
		IResult r = topicMap.getNode(bookmarkLocator, credentials);
		if (r.hasError() && r.getResultObject() == null)
			return r;
		//now, process tags
		IProxy bkmk = (IProxy)r.getResultObject();
		r = tagModel.addTagsToNode(bkmk, tagLabels, provenanceLocator, credentials);
		return r;
	}

	@Override
	public IResult findOrCreateBookmark(String url, String title,
			String details, String language, String userId, String provenanceLocator, JSONObject tagLabels, ITicket credentials) {
		environment.logDebug("TopicMapModel.findOrCreateBookmark- "+url+" | "+userId);
		IResult result = this.getBookmarkByURL(url, credentials);
		IProxy bkmk = (IProxy)result.getResultObject();
		IProxy note = null;
		System.out.println("FindOrCreateBookmark-1 "+bkmk);
		boolean isNew = false;
		IResult r;
		if (bkmk == null) {
			isNew = true;
			//make a new one
			bkmk = nodeModel.newInstanceNode(INodeTypes.BOOKMARK_TYPE, 
					title, "", language, userId, provenanceLocator, ICoreIcons.BOOKMARK_SM, ICoreIcons.BOOKMARK, false);
			bkmk.setURL(url);
			result = topicMap.putNode(bkmk);
			result.setResultObject(bkmk);
			r = relateNodeToUser(bkmk, userId, provenanceLocator, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
		}
		if (!isNew) {
			//TODO THIS IS A SPECIAL QUERY
			String sql = ""; //TODO
			Object [] vals = new Object[0]; //todo
			r = topicMap.runSQLQuery(sql, vals, credentials);
			//TODO
			//It's an existing object; should we add this user to it?
	/*		List<JSONObject> pivs = topicMap.listRelationsByRelationType(ISocialBookmarkLegend.BOOKMARK_USER_RELATIONTYPE, 0, -1, credentials);
			Iterator<JSONObject>itr = pivs.iterator();
			JSONObject jo;
			boolean isFound = false;
			while (itr.hasNext()) {
				jo = itr.next();
				//user topic goes in as target on bookmark tuple
				if (jo.get("documentLocator").equals(userId)) {
					isFound = true;
					//this user already bookmarked this document
					break;
				}
			}
			if(!isFound) {
				r = this.relateNodeToUser(bkmk, userId, provenanceLocator, credentials);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			} */
		}
		if (details != null && !details.equals("")) {
			//add annotation and pivot
			r = addAnnotation(bkmk, details, language, userId, provenanceLocator, credentials, result);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			note = (IProxy)r.getResultObject();
		}
		System.out.println("FindOrCreateBookmark-2 "+bkmk+" "+tagLabels);
		if (bkmk != null && tagLabels != null && !tagLabels.isEmpty()) {
			
				List<String>tls = new ArrayList<String>();
				//We use a fixed set of keys: "tag1" "tag2" "tag3" "tag4"
				String label = tagLabels.getAsString("tag1");
				if (label != null && !label.equals(""))
					tls.add(label);
				label = tagLabels.getAsString("tag2");
				if (label != null && !label.equals(""))
					tls.add(label);
				label = tagLabels.getAsString("tag3");
				if (label != null && !label.equals(""))
					tls.add(label);
				label = tagLabels.getAsString("tag4");
				if (label != null && !label.equals(""))
					tls.add(label);
				r = this.findOrProcessTags(bkmk.getLocator(), tls, language, provenanceLocator, credentials);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
				if (note != null) {
					r = this.findOrProcessTags(note.getLocator(), tls, language, provenanceLocator, credentials);					
					if (r.hasError())
						result.addErrorString(r.getErrorString());
				}
			
		}
		System.out.println("FindOrCreateBookmark-3 "+result.getErrorString());
		return result;
	}

	/**
	 * Create an annotation node for this <code>bookmark</code>
	 * @param bookmark
	 * @param details
	 * @param language
	 * @param userId
	 * @param provenanceLocator TODO
	 * @param result
	 */
	private IResult addAnnotation(IProxy bookmark, String details, String language, String userId, String provenanceLocator, ITicket credentials, IResult result) {
		int len = details.length();
		String title = details;
		if (len > 40) {
			title = details.substring(0, 40)+"...";
		}
		//Make an annotation
		IProxy note = nodeModel.newInstanceNode(INodeTypes.ANNOTATION_TYPE, title, details, language,
				userId, provenanceLocator, ICoreIcons.NOTE_SM, ICoreIcons.NOTE, false);
		//save it
		IResult r = topicMap.putNode(note);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		r = relateNodeToUser(note, userId, provenanceLocator, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());

		//pivot annotation-bookmark
		environment.logDebug("RELATING-2 \n"+note.toJSONString()+"\n"+bookmark.toJSONString());
		r = nodeModel.relateExistingNodes(note, bookmark, ISocialBookmarkLegend.ANNOTATION_BOOKMARK_RELATION_TYPE,
				null, null, userId, provenanceLocator, ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		//pivot annotation-user
		r = relateNodeToUser(note, userId, provenanceLocator, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		r.setResultObject(note);
		return r;
	}

	@Override
	public IResult updateTopicTextFields(String locator, String label, String description, String language,
			boolean checkVersion, ITicket credentials) {
		System.out.println("UpdateTopicText- "+locator+" "+label+" "+description+" "+language);
		IResult result = this.getTopic(locator, credentials);
		IProxy t = (IProxy)result.getResultObject();
		boolean isChanged = false;
		if (t != null) {
			//Now must perform surgery on text fields.
			List<String> l;
			String f, x;
/*			if (!label.equals("")) {
				f = t.makeField(ITQCoreOntology.LABEL_PROPERTY, language);
				l = (List<String>)t.getProperty(f);
				if (l != null) {
					if (!l.contains(label)) {
						l.set(0, label);
						isChanged = true;
						t.setProperty(f, l);
					}
				}
				
			} */ //TODO disabled for now.
			/*	if (!description.equals("")) {
				f = t.makeField(ITQCoreOntology.DETAILS_PROPERTY, language);
				l = (List<String>)t.getProperty(f);
				System.out.println("UpdateTopicText1 "+l);
				if (l != null) {
					if (!l.contains(description)) {
						l.set(0, description);
						isChanged = true;
						t.setProperty(f, l);
					}
				} else {
					l = new ArrayList<String>();
					l.add(description);
					isChanged = true;
					t.setProperty(f, l);
				}
			}
			if (isChanged) {
				result = this.updateTopic(t.getData(), checkVersion);
				System.out.println("UpdateTopicText+ "+t.getData().toJSONString());
			}*/
		}
		return result;
	}

	@Override
	public IResult listByFullTextQuery(String queryString, String language, int start, int count, String sortBy,
			String sortDir, ITicket credentials) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		String lang = language;
		if (lang == null)
			lang = "en";
		QueryBuilder qb1 = QueryBuilders.matchQuery(makeField(ITQCoreOntology.LABEL_PROPERTY, lang), queryString);
		QueryBuilder qb2 = QueryBuilders.matchQuery(makeField(ITQCoreOntology.DETAILS_PROPERTY, lang), queryString);
		qb.should(qb1);
		qb.should(qb2);
		//TODO put start,count,sort, etc in that query
		System.out.println("FULLTEXTQUERY "+qb.toString());
		IResult result = topicMap.runTextQuery(qb.toString(), credentials);
		return result;
	}

	private String makeField(String fieldBase, String language) {
		String result = fieldBase;
		if (!language.equals("en"))
			result += language;
		return result;
	}


}
