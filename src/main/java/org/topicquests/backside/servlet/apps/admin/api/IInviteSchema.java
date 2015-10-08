/**
 * 
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
