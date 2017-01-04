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
package org.topicquests.backside.servlet.api;

/**
 * @author park
 * <p>Microformats are just a kind of JSON ontology</p>
 * <p>This microformat defines the <em>credentials</em> which
 *  determine who is authenticated, and who is not.<p>
 * <p>This microformat is a <em>root</em> for all application
 *  microformats</p>
 */
public interface ICredentialsMicroformat {

	/**
	 * JSON Attributes
	 */
	public static final String
		/** absolutely required field */
		USER_HANDLE		= "uName",
		/** required field */
		USER_ID			= "uId",
		/** required for authentication */
		USER_EMAIL		= "uEmail",
		/** password is encrypted, is only required for authentication */
		USER_PWD		= "uPwd",
		USER_NEW_PWD	= "uNewPwd",
		USER_IP			= "uIP",
		/** Required once individual is logged in; returned by RESP_TOKEN */
		SESSION_TOKEN	= "sToken";
	
	/**
	 * Response token JSON attributes
	 */
	public static final String
		RESP_TOKEN		= "rToken",
		RESP_MESSAGE	= "rMsg";
	
	/**
	 * Core Command JSON attributes
	 */
	public static final String
		VERB			= "verb",
		/** encapsulate a list of verb/cargo pairs */
		SEQUENCE_VERB	= "sequence",
		MODIFIERS		= "mods",
		CARGO			= "cargo";
	
	/**
	 * COMMON MODIFIERS
	 */
	public static final String
		ITEM_FROM		= "from",
		ITEM_COUNT		= "count",
		ITEM_SORT		= "sort",
		DATE_INC_SORT	= "DateIncSort",
		DATE_DEC_SORT	= "DateDecSort",
		ALPHA_INC_SORT	= "AlphaIncSort",
		ALPHA_DEC_SORT	= "AlphaDecSort";
	
	/**
	 * COMMON VERBS
	 */
	public static final String
		V_GET			= "get",
		V_PUT			= "put",
		V_DELETE		= "del",
		V_FIND			= "find";
}
