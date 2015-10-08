/**
 * 
 */
package org.topicquests.backside.servlet.apps.usr;

import java.sql.*;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ISecurity;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserPersist;
import org.topicquests.backside.servlet.apps.usr.persist.H2UserDatabase;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.ks.api.ICoreIcons;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITQDataProvider;
import org.topicquests.ks.tm.api.ISubjectProxy;
import org.topicquests.ks.tm.api.ISubjectProxyModel;

import com.google.common.io.BaseEncoding;

/**
 * @author park
 *
 */
public class UserModel implements IUserModel {
	private ServletEnvironment environment;
	private IUserPersist database;
	private ITQDataProvider topicMap;
	private ISubjectProxyModel nodeModel;
    /**
     * Pools Connections for each local thread
     * Must be closed when the thread terminates
     */
    private ThreadLocal <Connection>localMapConnection = new ThreadLocal<Connection>();

	/**
	 * 
	 */
	public UserModel(ServletEnvironment env) throws Exception {
		environment = env;
		String dbName=environment.getStringProperty("UserDatabase");
		String userName=environment.getStringProperty("MyDatabaseUser");
		String userPwd=environment.getStringProperty("MyDatabasePwd");
		String dbPath = environment.getStringProperty("UserDatabasePath");
		database = new H2UserDatabase(environment,dbName,userName,userPwd,dbPath);
		SystemEnvironment tmenv = environment.getTopicMapEnvironment();
		System.out.println("FOO "+tmenv);
		topicMap = tmenv.getDatabase();
		nodeModel = topicMap.getSubjectProxyModel();
		validateDefaultAdmin();
	}

	private void validateDefaultAdmin() {
		//from config-props.xml
		String admin = environment.getStringProperty("DefaultAdminEmail");
		String pwd = environment.getStringProperty("DefaultAdminPwd");
		System.out.println("VALIDATE "+admin+" | "+pwd);
		IResult r = authenticate(admin,pwd);
		if (r.getResultObject() == null) {
			r = this.insertUser(admin, "defaultadmin", pwd, "Default Admin", "", ISecurity.ADMINISTRATOR_ROLE, "", "", false);
			//This user is far more than an Admin
			StringBuilder buf = new StringBuilder(ISecurity.ADMINISTRATOR_ROLE).append(", ").append(ISecurity.OWNER_ROLE);
			r = this.updateUserRole("defaultadmin", buf.toString());
		}
	}
	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult authenticate(String email, String password) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.authenticate(con, email, password);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#getTicket(java.lang.String)
	 */
	@Override
	public IResult getTicket(String userName) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.getTicket(con, userName);
	}
	
	@Override
	public IResult getTicketByEmail(String email) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.getTicketByEmail(con, email);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#insertUser(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUser(String email, String userName, String password, String userFullName, String avatar, String role, String homepage, String geolocation, boolean addTopic) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
//		System.out.println("INSERTUSER "+con);
		
//		System.out.println("INSERTUSER "+password);
		IResult result = new ResultPojo();
		if (addTopic) {
			String s = userFullName;
			if (s.equals(""))
				s = userName;
			ISubjectProxy n = nodeModel.newInstanceNode(userName, ITQCoreOntology.USER_TYPE, s, "", "en", 
					ITQCoreOntology.SYSTEM_USER, ICoreIcons.PERSON_ICON_SM, ICoreIcons.PERSON_ICON, false);
			result = topicMap.putNode(n, false);
		}
		IResult x = database.insertUser(con, email, userName, password, userFullName, avatar, role, homepage, geolocation);
		if (x.hasError())
			result.addErrorString(x.getErrorString());
		result.setResultObject(x.getResultObject());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#insertEncryptedUser(java.lang.String, java.lang.String, java.lang.String)
	 * /
	@Override
	public IResult insertEncryptedUser(String userName, String password,
			String grant) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#insertUserData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUserData(String userName, String propertyType,
			String propertyValue) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();

		return database.insertUserData(con, userName, propertyType, propertyValue);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#updateUserData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserData(String userName, String propertyType,
			String newValue) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.updateUserData(con, userName, propertyType, newValue);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#existsUsername(java.lang.String)
	 */
	@Override
	public IResult existsUsername(String userName) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.existsUsername(con, userName);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#removeUser(java.lang.String)
	 */
	@Override
	public IResult removeUser(String userName) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.removeUser(con, userName);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#changeUserPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult changeUserPassword(String userName, String newPassword) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.changeUserPassword(con, userName, newPassword);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#addUserRole(java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserRole(String userName, String newRole) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.updateUserRole(con, userName, newRole);
	}
	
	@Override
	public IResult updateUserEmail(String userName, String newEmail) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.updateUserEmail(con, userName, newEmail);
	}


	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserModel#listUserLocators()
	 */
	@Override
	public IResult listUserLocators() {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.listUserLocators(con);
	}
	
	@Override
	public IResult listUsers(int start, int count) {
		Connection con = null;
		IResult r = getMapConnection();
		if (r.hasError())
			return r;
		con = (Connection)r.getResultObject();
		return database.listUsers(con, start, count);
	}
	
  	private IResult getMapConnection() {
  		synchronized(localMapConnection) {
  			IResult result = new ResultPojo();
  			try {
  				Connection con = this.localMapConnection.get();
  				//because we don't "setInitialValue", this returns null if nothing for this thread
  				if (con == null) {
  					con = database.getConnection();
  					System.out.println("GETMAPCONNECTION "+con);
  					localMapConnection.set(con);
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
