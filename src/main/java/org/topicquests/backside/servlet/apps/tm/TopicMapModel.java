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
import org.topicquests.backside.servlet.apps.tm.api.ISocialBookmarkLegend;
import org.topicquests.backside.servlet.apps.tm.api.ITagModel;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapMicroformat;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.es.util.JSONQueryUtil;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ICoreIcons;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITQDataProvider;
import org.topicquests.ks.api.ITicket;
import org.topicquests.ks.tm.SubjectProxy;
import org.topicquests.ks.tm.api.INodeTypes;
import org.topicquests.ks.tm.api.ISubjectProxy;
import org.topicquests.ks.tm.api.ISubjectProxyModel;

/**
 * @author park
 *
 */
public class TopicMapModel extends BaseModel implements ITopicMapModel {
	private ITagModel tagModel;
	private ITicket systemCredentials;
	private JSONQueryUtil queryUtil;

	/**
	 * 
	 */
	public TopicMapModel(ServletEnvironment env) {
		super(env);
		tagModel = new TagModel(environment);
		systemCredentials = new TicketPojo(ITQCoreOntology.SYSTEM_USER);
		queryUtil = new JSONQueryUtil();
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel#putTopic(net.minidev.json.JSONObject, org.topicquests.model.api.ITicket)
	 */
	@Override
	public IResult putTopic(JSONObject topic) {
		ISubjectProxy n = new SubjectProxy(topic);
		IResult result = topicMap.putNode(n);
		return result;
	}

	@Override
	public IResult updateTopic(JSONObject topic, boolean checkVersion) {
		return topicMap.updateProxyFromJSON(topic, checkVersion);
	}

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
	public IResult query(JSONObject query, int start, int count, ITicket credentials) {
		IResult result = topicMap.runQuery(query.toJSONString(), start, count, credentials);
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

	@Override
	public IResult listAllBlogPosts(int start, int count, ITicket credentials) {
		System.out.println("TopicMapModel.listAllBlogPosts "+start+" "+count);
		final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder t2 = QueryBuilders.termQuery(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE, INodeTypes.BLOG_TYPE);
		searchSourceBuilder.query(t2);
		SortBuilder sb = new FieldSortBuilder(ITQCoreOntology.CREATED_DATE_PROPERTY).order(SortOrder.DESC);
		searchSourceBuilder.sort(sb);
		searchSourceBuilder.from(start);
		if (count > -1)
			searchSourceBuilder.size(count);
		String q = searchSourceBuilder.toString();
		IResult result = topicMap.executeQueryBuilder(searchSourceBuilder, credentials);
		System.out.println("LISTALLBLOGPOSTS "+q);
		environment.logDebug("TopicMapModel.listBlogPostsByUser+ "+result.getErrorString()+" | "+result.getResultObject());
		//TopicMapModel.getTopicByURL+  | [org.topicquests.ks.tm.SubjectProxy@6aab361d, org.topicquests.ks.tm.SubjectProxy@1098da46]
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

	@Override
	public IResult listInstanceTopics(String typeLocator, int start, int count, ITicket credentials) {
		IResult result = topicMap.listInstanceNodes(typeLocator, start, count, credentials);
		return result;
	}

	@Override
	public IResult listTopicsByKeyValue(String propertyKey, String value,
			int start, int count, ITicket credentials) {
		IResult result = new ResultPojo(); //
		//TODO
		return result;
	}

	@Override
	public IResult listUserTopics(int start, int count, ITicket credentials) {
		IResult result = topicMap.listInstanceNodes(ITQCoreOntology.USER_TYPE, start, count, credentials);
		return result;
	}

	@Override
	public IResult listTreeChildNodesJSON(String rootNodeLocator,
			ITicket credentials) {
		IResult result = new ResultPojo();
		JSONObject map = new JSONObject();
		result.setResultObject(map);
		IResult r = topicMap.getNode(rootNodeLocator, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		ISubjectProxy root = (ISubjectProxy)r.getResultObject();
		List<String> kids = root.listParentChildTree();
		if (kids != null) {
			r = topicMap.multiGetNodes(kids, credentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			List<JSONObject> snappers = (List<JSONObject>)r.getResultObject();
			int len = snappers.size();
			JSONObject jo;
			for (int i=0;i<len;i++) {
				jo = snappers.get(i);
				map.put((String)jo.get("lox"), jo);
			}
		}
		return result;
	}

	@Override
	public IResult newInstanceNode(JSONObject theTopicShell, ITicket credentials) {
	//	System.out.println("CARGO2 "+theTopicShell.toJSONStringString());
	//	System.out.println("LOX "+theTopicShell.get("lox"));
		IResult result  = new ResultPojo();
		environment.logDebug("TopicMapModel.newInstanceNode- "+theTopicShell.toJSONString());
		//{"uName":"joe","isPrv":"F","sIco":"\/images\/bookmark_sm.png","Lang":"en",
		//"label":"Tech Reports | Knowledge Media Institute | The Open University",
		//"lIco":"\/images\/bookmark.png","inOf":"BookmarkNodeType","url":"http:\/\/kmi.open.ac.uk\/publications\/techreport\/kmi-10-01"}
		
		//{"crtr":"jackpark","sIco":"\/images\/publication_sm.png","isPrv":"F",
		//"Lang":"en","details":"Modified TQElasticKnowledgeSystem","label":"Another try after fixes to the topic map",
		//"lIco":"\/images\/publication.png","inOf":"BlogNodeType"}
		String locator = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String typeLocator =  (String)theTopicShell.get(ITopicMapMicroformat.PARENT_LOCATOR);
		String lang = (String)theTopicShell.get(ITopicMapMicroformat.LANGUAGE);
		String userId = (String)theTopicShell.get(ICredentialsMicroformat.USER_NAME);
		String label = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LABEL);
		String description = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = (String)theTopicShell.get(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = (String)theTopicShell.get(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = (String)theTopicShell.get(ITopicMapMicroformat.IS_PRIVATE);
		String url = (String)theTopicShell.get(ITopicMapMicroformat.URL);
		//Added feature
		JSONObject extras = (JSONObject)theTopicShell.get(ITopicMapMicroformat.EXTRAS);
		if (extras != null)
			System.out.println("NEWINSTANCE "+extras.toJSONString());
		boolean isPrivate = false;
		IResult r;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		ISubjectProxy n = null;
		if (locator != null) {
			//First, see if this exists
			r = topicMap.getNode(locator, systemCredentials);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			n = (ISubjectProxy)r.getResultObject();
			result.setResultObject(n);
			return result;
			//if (r.getResultObject() != null)
			//	return r;
			//n = nodeModel.newInstanceNode(locator, typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		} else {
			n = nodeModel.newInstanceNode(typeLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
			if (url != null && !url.equals(""))
				n.setURL(url);
			if (extras != null) {
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
						String parent = (String)kid.get("locator");
						String context = (String)kid.get("contextLocator");
						String transclude = (String)kid.get("transcluder");
						String icon, subject;
						r = topicMap.getNode(parent, credentials);
						if (r.hasError())
							result.addErrorString(r.getErrorString());
						ISubjectProxy pnt = (ISubjectProxy)r.getResultObject();
						icon = pnt.getSmallImage();
						subject = pnt.getLabel(lang);
						//Add a child to the parent
						nodeModel.addChildNode(pnt,context, smallImagePath, n.getLocator(), label, transclude);
						//pnt.addChildNode(context, smallImagePath, n.getLocator(), label, transclude);
						pnt.setLastEditDate(new Date());
						r = topicMap.putNode(pnt);
						if (r.hasError())
							result.addErrorString(r.getErrorString());
						// add parent to new node
						nodeModel.addParentNode(n, context, icon, parent, subject);
						//n.addParentNode(context, icon, parent, subject);
						
					} else {
						//////////////////////////////
						//NOTE: we are putting values
						// BIG ISSUE: overwriting old values
						//////////////////////////////
						jo.put(key, extras.get(key));
					}
				}
			}
			if (n.getLocator() == null) {
				environment.logError("Missing lox for "+typeLocator+" | "+label, null);
			}
			r = topicMap.putNode(n);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
			environment.logDebug("TopicMapModel.newInstance-1 "+r.getErrorString()+" | "+n.toJSONString());
		}
		
		environment.logDebug("TopicMapModel.newInstance-2 "+n.toJSONString());
		r = relateNodeToUser(n, userId, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		result.setResultObject(n);
		return result;
		
	}
	


	@Override
	public IResult newSubclassNode(JSONObject theTopicShell, ITicket credentials) {
		String locator = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String superClassLocator =  (String)theTopicShell.get(ITopicMapMicroformat.SUPERTYPE_LOCATOR);
		String lang = (String)theTopicShell.get(ITopicMapMicroformat.LANGUAGE);
		String userId = (String)theTopicShell.get(ITQCoreOntology.CREATOR_ID_PROPERTY);
		String label = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_LABEL);
		String description = (String)theTopicShell.get(ITopicMapMicroformat.TOPIC_DETAILS);
		String smallImagePath = (String)theTopicShell.get(ITopicMapMicroformat.SMALL_IMAGE_PATh);
		String largeImagePath = (String)theTopicShell.get(ITopicMapMicroformat.LARGE_IMAGE_PATH);
		String isp = (String)theTopicShell.get(ITopicMapMicroformat.IS_PRIVATE);
		boolean isPrivate = false;
		IResult r;
		if (isp.equalsIgnoreCase("t"))
			isPrivate = true;
		ISubjectProxy n = null;
		if (locator != null) {
			//First, see if this exists
			r = topicMap.getNode(locator, systemCredentials);
			if (r.getResultObject() != null)
				return r;
			n = nodeModel.newSubclassNode(locator, superClassLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
		} else
			n = nodeModel.newSubclassNode(superClassLocator, label, description, lang, userId, smallImagePath, largeImagePath, isPrivate);
			
		IResult result = topicMap.putNode(n);
		result.setResultObject(n.getData());
		r = relateNodeToUser(n, userId, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}

	@Override
	public IResult addFeaturesToNode(JSONObject cargo, ITicket credentials) {
		String lox = (String)cargo.get(ITopicMapMicroformat.TOPIC_LOCATOR);
		String userId = (String)cargo.get(ITQCoreOntology.CREATOR_ID_PROPERTY);
		IResult r = topicMap.getNode(lox, credentials);
		IResult result = new ResultPojo();
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		ISubjectProxy n = (ISubjectProxy)r.getResultObject();
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
						n.setURL((String)cargo.get(ITQCoreOntology.RESOURCE_URL_PROPERTY));
					}
				}
			}
			n.doUpdate();
			result = topicMap.updateNode(n, true);
			//Pay attention to OptimisticLock
			
		} else {
			result.addErrorString("Missing Node "+lox);
		}

		return result;
	}

	@Override
	public IResult getNodeTree(String rootLocator, int maxDepth, int start, int count, ITicket credentials) {
		return ((ITQDataProvider)topicMap).loadTree(rootLocator, maxDepth, start, count, credentials);
	}

	@Override
	public IResult listTopicsByURL(String url, ITicket credentials) {
		QueryBuilder qb1 = QueryBuilders.termQuery(ITQCoreOntology.RESOURCE_URL_PROPERTY, url);
		IResult result = topicMap.runQuery(qb1.toString(), 0, -1, credentials);
		environment.logDebug("TopicMapModel.listTopicsByURL+ "+result.getErrorString()+" | "+result.getResultObject());
		//TopicMapModel.getTopicByURL+  | [org.topicquests.ks.tm.SubjectProxy@6aab361d, org.topicquests.ks.tm.SubjectProxy@1098da46]
		List<Object> lx = (List<Object>)result.getResultObject();
		if (lx != null) {
			if (lx.size() > 0) {
				try {
					ISubjectProxy n = (SubjectProxy)(lx.get(0));
					result.setResultObject(n);
				} catch (Exception ex) {
					environment.logError(ex.getMessage(), ex);
					result.addErrorString(ex.getMessage());
					result.setResultObject(null);
				}
			} else
				result.setResultObject(null);
		}
		System.out.println("LISTTOPICSBYURL+ "+result.getErrorString()+" | "+result.getResultObject());
		return result;
	}
	
	@Override
	public IResult getBookmarkByURL(String url, ITicket credentials) {
		QueryBuilder t1 = QueryBuilders.termQuery(ITQCoreOntology.RESOURCE_URL_PROPERTY, url);
		QueryBuilder t2 = QueryBuilders.termQuery(ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE, INodeTypes.BOOKMARK_TYPE);
		final BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(t1);
		query.must(t2);
		IResult result = topicMap.runQuery(query.toString(), 0, -1, credentials);
		environment.logDebug("TopicMapModel.getBookmarkByURL+ "+result.getErrorString()+" | "+result.getResultObject());
		//TopicMapModel.getTopicByURL+  | [org.topicquests.ks.tm.SubjectProxy@6aab361d, org.topicquests.ks.tm.SubjectProxy@1098da46]
		List<Object> lx = (List<Object>)result.getResultObject();
		if (lx != null) {
			if (lx.size() > 0) {
				try {
					ISubjectProxy n = (SubjectProxy)(lx.get(0));
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
	
	@Override
	public IResult addPivot(String topicLocator, String pivotLocator,
			String pivotRelationType, String smallImagePath,
			String largeImagePath, boolean isTransclude, boolean isPrivate,
			ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult addRelation(String topicLocator, String pivotLocator,
			String pivotRelationType, String smallImagePath,
			String largeImagePath, boolean isTransclude, boolean isPrivate,
			ITicket credentials) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResult findOrProcessTags(String bookmarkLocator, List<String> tagLabels,
			String language, ITicket credentials) {
		//get the bookmark node
		IResult r = topicMap.getNode(bookmarkLocator, credentials);
		if (r.hasError() && r.getResultObject() == null)
			return r;
		//now, process tags
		ISubjectProxy bkmk = (ISubjectProxy)r.getResultObject();
		r = tagModel.addTagsToNode(bkmk, tagLabels, credentials);
		return r;
	}

	@Override
	public IResult findOrCreateBookmark(String url, String title,
			String details, String language, String userId, JSONObject tagLabels, ITicket credentials) {
		environment.logDebug("TopicMapModel.findOrCreateBookmark- "+url+" | "+userId);
		IResult result = this.getBookmarkByURL(url, credentials);
		ISubjectProxy bkmk = (ISubjectProxy)result.getResultObject();
		System.out.println("FindOrCreateBookmark-1 "+bkmk);
		boolean isNew = false;
		if (bkmk == null) {
			isNew = true;
			//make a new one
			bkmk = nodeModel.newInstanceNode(INodeTypes.BOOKMARK_TYPE, 
					title, "", language, userId, ICoreIcons.BOOKMARK_SM, ICoreIcons.BOOKMARK, false);
			bkmk.setURL(url);
			result = topicMap.putNode(bkmk);
			result.setResultObject(bkmk);
		}
		if (!isNew) {
			//It's an existing object; should we add this user to it?
			List<JSONObject> pivs = bkmk.listPivotsByRelationType(ISocialBookmarkLegend.BOOKMARK_USER_RELATIONTYPE);
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
				IResult r = this.relateNodeToUser(bkmk, userId, credentials);
				if (r.hasError())
					result.addErrorString(r.getErrorString());
			}
		}
		if (details != null && !details.equals("")) {
			//add annotation and pivot
			addAnnotation(bkmk, details, language, userId, credentials, result);
		}
		System.out.println("FindOrCreateBookmark-2 "+bkmk+" "+tagLabels);
		if (bkmk != null && tagLabels != null && !tagLabels.isEmpty()) {
			
				List<String>tls = new ArrayList<String>();
				//We use a fixed set of keys: "tag1" "tag2" "tag3" "tag4"
				String label = (String)tagLabels.get("tag1");
				if (label != null && !label.equals(""))
					tls.add(label);
				label = (String)tagLabels.get("tag2");
				if (label != null && !label.equals(""))
					tls.add(label);
				label = (String)tagLabels.get("tag3");
				if (label != null && !label.equals(""))
					tls.add(label);
				label = (String)tagLabels.get("tag4");
				if (label != null && !label.equals(""))
					tls.add(label);
				result = this.findOrProcessTags(bkmk.getLocator(), tls, language, credentials);
			
			
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
	 * @param result
	 */
	private void addAnnotation(ISubjectProxy bookmark, String details, String language, String userId, ITicket credentials, IResult result) {
		int len = details.length();
		String title = details;
		if (len > 40) {
			title = details.substring(0, 40)+"...";
		}
		//Make an annotation
		ISubjectProxy note = nodeModel.newInstanceNode(INodeTypes.ANNOTATION_TYPE, title, details, language,
				userId, ICoreIcons.NOTE_SM, ICoreIcons.NOTE, false);
		//save it
		IResult r = topicMap.putNode(note);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		//pivot annotation-bookmark
		r = nodeModel.relateExistingNodesAsPivots(note, bookmark, ISocialBookmarkLegend.ANNOTATION_BOOKMARK_RELATION_TYPE,
				userId, ICoreIcons.RELATION_ICON_SM, ICoreIcons.RELATION_ICON, false, false);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		//pivot annotation-user
		r = relateNodeToUser(note, userId, credentials);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
	}



}
