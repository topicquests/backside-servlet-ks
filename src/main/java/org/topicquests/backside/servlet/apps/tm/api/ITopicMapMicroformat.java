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
package org.topicquests.backside.servlet.apps.tm.api;

import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.ks.api.ITQCoreOntology;

/**
 * @author park
 *
 */
public interface ITopicMapMicroformat extends ICredentialsMicroformat {
	//Verbs
	public static final String
		//GET
		GET_TOPIC				= "GetTopic",
		FULL_TEXT_SEARCH		= "ftSrch",
		FULL_TEXT_PHRASE_SEARCH	= "ftPhrSrch",  

		PUT_TOPIC				= "PutTopic",
		//POST
		REMOVE_TOPIC			= "RemTopic",
		//followed by cargo with specs for the topic,
		//e.g. Locator = some value or not present, meaning needs locator
		// SuperType or ParentType, label, description, language, IsPrivate
		NEW_INSTANCE_TOPIC		= "NewInstance",
		NEW_SUBCLASS_TOPIC		= "NewSub",
		//Cargo: 
		//  inOf: nodeType
		//  lox: locator or "" for null
		//  ConParentLocator: locator or "" for null
		// ContextLocator: locator or "" for null
		// label : label
		// details : details or ""
		// Lang : language code
		// uName: userId  // already in rest of query
		// isPrv: 'T' or 'F'
		NEW_CONVERSATION_NODE	= "NewConvNode",
		//allows to add key/value pairs and special items
		ADD_FEATURES_TO_TOPIC	= "AddFeatures",
		LIST_INSTANCE_TOPICS	= "ListInstances",
		LIST_SUBCLASS_TOPICS	= "ListSubclasses",
		LOAD_TREE				= "LoadTree",
		GET_TOPIC_BY_URL		= "GetByURL",
		ADD_PIVOT				= "AddPivot",	//post
		ADD_RELATION			= "AddRelation",	//post
		ADD_CHILD_NODE			= "AddChildNode", // inside EXTRAS
		FIND_OR_PROCESS_TAG		= "FindProcessTag", //post
		FIND_OR_CREATE_BOOKMARK	= "FindProcessBookmark"; //post
		
		
	
	//attributes
	public static final String
		TOPIC_LOCATOR 					= ITQCoreOntology.LOCATOR_PROPERTY,
		SUPERTYPE_LOCATOR				= ITQCoreOntology.SUBCLASS_OF_PROPERTY_TYPE,
		PARENT_LOCATOR					= ITQCoreOntology.INSTANCE_OF_PROPERTY_TYPE,
		CONVERSATION_PARENT_LOCATOR 	= "ConParentLocator",
		CONTEXT_LOCATOR					= "ContextLocator", // for parent/child nodes
		TOPIC_LABEL			= ITQCoreOntology.LABEL_PROPERTY,
		TOPIC_DETAILS		= ITQCoreOntology.DETAILS_PROPERTY,
		LIST_PROPERTY		= "ListProperty",
		//2-character code, e.g. "en"
		LANGUAGE			= "Lang",
		URL					= ITQCoreOntology.RESOURCE_URL_PROPERTY,
		// will be a tiny JSON object with added key/value pairs
		EXTRAS				= "extras",
		//nodes get images
		LARGE_IMAGE_PATH	= ITQCoreOntology.LARGE_IMAGE_PATH,
		SMALL_IMAGE_PATh	= ITQCoreOntology.SMALL_IMAGE_PATH,
		// "t" or "f" case insensitive
		IS_PRIVATE			= ITQCoreOntology.IS_PRIVATE_PROPERTY,
		TAG_NAMES			= "TagNames",
		//FEATURES creates a space for additional key/value pairs
		FEATURES			= "Features";
}
