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
package org.topicquests.backside.servlet.apps.admin.api;



/**
 * @author park
 *
 */
public interface IInviteSchema {
	public static final String [] INVITE_SCHEMA = { 
        "CREATE TABLE invites ("+
        	"email VARCHAR(255) PRIMARY KEY)",
        "CREATE INDEX inviteindex ON invites(email)"};
	
	  public final String getInvite =
	      "SELECT email FROM invites WHERE email=?";
	  public final String insertInvite =
	      "INSERT INTO invites (email) VALUES(?)";
	  public final String removeInvite =
	      "DELETE FROM invites WHERE email=?";
	  public final String listInvites =
		  "SELECT email FROM invites ";

	  public final String listInvitesLimited =
			  "SELECT email FROM invites OFFSET=? LIMIT=?";
}
