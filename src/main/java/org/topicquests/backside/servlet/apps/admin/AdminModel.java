/**
 * 
 */
package org.topicquests.backside.servlet.apps.admin;

import java.sql.Connection;
import java.sql.SQLException;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.admin.api.IAdminModel;
import org.topicquests.backside.servlet.apps.admin.api.IInviteDatabase;
import org.topicquests.backside.servlet.apps.admin.persist.H2InviteDatabase;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;

/**
 * @author park
 *
 */
public class AdminModel implements IAdminModel {
	private ServletEnvironment environment;
	private IUserModel userModel;
	private IInviteDatabase database;
	
    /**
     * Pools Connections for each local thread
     * Must be closed when the thread terminates
     */
    private ThreadLocal <Connection>localMapConnection = new ThreadLocal<Connection>();

	/**
	 * 
	 */
	public AdminModel(ServletEnvironment env) throws Exception {
		environment = env;
		userModel = environment.getUserModel();
		//build invite database
		String dbName=environment.getStringProperty("InviteDatabase");
		String userName=environment.getStringProperty("MyDatabaseUser");
		String userPwd=environment.getStringProperty("MyDatabasePwd");
		String dbPath = environment.getStringProperty("UserDatabasePath");
		database = new H2InviteDatabase(environment,dbName,userName,userPwd,dbPath);

	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.admin.api.IInviteModel#existsInvite(java.lang.String)
	 */
	@Override
	public IResult existsInvite(String userEmail) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.existsInvite(con, userEmail);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.admin.api.IInviteModel#addInvite(java.lang.String)
	 */
	@Override
	public IResult addInvite(String userEmail) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		System.out.println("MODELADDINVITE "+con+" "+userEmail);
		return database.addInvite(con, userEmail);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.admin.api.IInviteModel#removeInvite(java.lang.String)
	 */
	@Override
	public IResult removeInvite(String userEmail) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.removeInvite(con, userEmail);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.admin.api.IAdminModel#removeUser(java.lang.String)
	 */
	@Override
	public IResult removeUser(String userName) {
		return userModel.removeUser(userName);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.admin.api.IAdminModel#listUsers(int, int)
	 */
	@Override
	public IResult listUsers(int start, int count) {
		return userModel.listUsers(start, count);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.admin.api.IAdminModel#updateUserRole(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserRole(String userName, String newRole) {
		System.out.println("UpdateUserRole "+userName+" "+newRole);
		return userModel.updateUserRole(userName, newRole);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.admin.api.IAdminModel#updateUserEmail(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserEmail(String userName, String newEmail) {
		return userModel.updateUserEmail(userName, newEmail);
	}
	
	@Override
	public IResult listInvites(int start, int count) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.listInvites(con, start, count);
	}


  	private IResult getMapConnection() {
  		synchronized(localMapConnection) {
  			IResult result = new ResultPojo();
  			try {
  				Connection con = this.localMapConnection.get();
  				//because we don't "setInitialValue", this returns null if nothing for this thread
  				if (con == null) {
  					con = database.getConnection();
  					localMapConnection.set(con);
  					System.out.println("GETMAPCONNECTIOn "+con);
  				}
  				result.setResultObject(con);
  			} catch (Exception e) {
  				result.addErrorString(e.getMessage());
  				environment.logError(e.getMessage(),e);
  			}
  			return result;
  		}
      }

      public IResult closeLocalConnection(){
      	 IResult result = new ResultPojo();
      	 boolean isError = false;
           try {
        	   synchronized(localMapConnection) {
    	         Connection con = this.localMapConnection.get();
    	         if (con != null)
    	           con.close();
    	         localMapConnection.remove();
    	       //  localMapConnection.set(null);
        	   }
           } catch (SQLException e) {
             isError = true;
             result.addErrorString(e.getMessage());
           }
           if (!isError)
          	 result.setResultObject("OK");
           return result;
       }

	@Override
	public void shutDown() {
		closeLocalConnection();
	}

}
