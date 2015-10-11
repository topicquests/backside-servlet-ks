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
package org.topicquests.backside.servlet.apps.usr.persist;

import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.IRDBMSDatabase;
import org.topicquests.backside.servlet.apps.admin.api.IInviteSchema;
import org.topicquests.backside.servlet.apps.usr.api.IUserPersist;
import org.topicquests.backside.servlet.apps.usr.api.IUserSchema;
import org.topicquests.backside.servlet.persist.rdbms.H2DatabaseDriver;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ITicket;

/**
 * @author park
 *
 */
public class H2UserDatabase  extends H2DatabaseDriver implements IUserPersist, IRDBMSDatabase {
	private final String h2JdbcDriver = "org.h2.Driver";
    private String userName, password, connectionString;
	/**
	 * 
	 */
	public H2UserDatabase(ServletEnvironment env, String dbName, String userName, String pwd, String filePath)
			throws Exception {
		super(env,dbName,userName,pwd, filePath);
		init();
	}
	private void init() throws Exception {
		Connection con = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			con = getSQLConnection();
			System.out.println("H2UserDatabase.init-1 "+con);
			s= con.createStatement();
			rs = s.executeQuery("select * from users");
			environment.logDebug("H2UserDatabase.init "+rs.next());
			System.out.println("H2UserDatabase.init-2");
		} catch (Exception e) {
			System.out.println("H2UserDatabase.init-3 ");
			environment.logDebug("H2UserDatabase.init fail "+e.getMessage());
			exportSchema();
		} finally {
			if (s != null)
				s.close();
			if (rs != null)
				rs.close();
			if (con != null)
				con.close();
		}
	}

	/**
	 * Salt a password for the database
	 * @param input
	 * @return
	 * @throws Exception
	 */
	String sha1(String input) throws Exception {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#authenticate(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult authenticate(Connection con, String email, String password) {
		IResult result = new ResultPojo();
		if (email == null || password == null) {
			result.addErrorString("Missing email or password");
			return result;
		}
		PreparedStatement s = null;
		PreparedStatement s2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			String pwd = sha1(password);
			System.out.println("AUTH "+email+" "+pwd);
			s = con.prepareStatement(IUserSchema.authenticate);
			s.setString(1, email);
			s.setString(2, pwd);
			rs = s.executeQuery();

			if (rs.next()) {
				String key,val,ava,name;
				ITicket t = new TicketPojo();
		//		ava = rs.getString(IUserSchema.USER_AVATAR);
				name = rs.getString(IUserSchema.USER_NAME);
	//			if (!ava.equals(""))
	//				t.addAvatarLocator(ava);
				t.setUserLocator(name);
				t.setProperty(IUserSchema.USER_EMAIL, rs.getString(IUserSchema.USER_EMAIL));
				t.setProperty(IUserSchema.USER_FULLNAME, rs.getString(IUserSchema.USER_FULLNAME));
				s2 = con.prepareStatement(IUserSchema.getUserProperties);
				s2.setString(1, name);
				rs2 = s2.executeQuery();
				while (rs2.next()) {
					key = rs2.getString("prop");
					val = rs2.getString("val");
					t.setProperty(key, val);
				}
				result.setResultObject(t);
			}
			//otherwise return null
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			closePreparedStatement(s,result);
			closeResultSet(rs,result);
			if (s2 != null)
				closePreparedStatement(s2,result);
			if (rs2 != null)
				closeResultSet(rs2,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#getTicket(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult getTicket(Connection con, String userName) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		PreparedStatement s2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			s = con.prepareStatement(IUserSchema.getUserByName);
			s.setString(1, userName);
			rs = s.executeQuery();
			if (rs.next()) {
				String key,val,ava;
				String email = rs.getString(IUserSchema.USER_EMAIL);
				ITicket t = new TicketPojo();
			//	ava = rs.getString(IUserSchema.USER_AVATAR);
			//	if (!ava.equals(""))
			//		t.addAvatarLocator(ava);
				t.setUserLocator(rs.getString(IUserSchema.USER_NAME));
				t.setProperty(IUserSchema.USER_EMAIL, email);
			//	t.setProperty(IUserSchema.USER_ROLE, rs.getString(IUserSchema.USER_ROLE));
				t.setProperty(IUserSchema.USER_FULLNAME, rs.getString(IUserSchema.USER_FULLNAME));
				s2 = con.prepareStatement(IUserSchema.getUserProperties);
				s2.setString(1, userName);
				rs2 = s2.executeQuery();
				while (rs2.next()) {
					key = rs2.getString("prop");
					val = rs2.getString("val");
				//	System.out.println("GETPROP "+userName+" "+key+" | "+val);
					t.setProperty(key, val);
				}
				result.setResultObject(t);
			}
			//otherwise return null
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s,result);
			closeResultSet(rs,result);
			if (s2 != null)
				closePreparedStatement(s2,result);
			if (rs2 != null)
				closeResultSet(rs2,result);
		}
		return result;
	}
	
	@Override
	public IResult getTicketByEmail(Connection con, String email) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		PreparedStatement s2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			s = con.prepareStatement(IUserSchema.getUserByEmail);
			s.setString(1, email);
			rs = s.executeQuery();
			if (rs.next()) {
				String key,val,ava;
				String userName = rs.getString(IUserSchema.USER_NAME);
				ITicket t = new TicketPojo();
		//		ava = rs.getString(IUserSchema.USER_AVATAR);
		//		if (!ava.equals(""))
		//			t.addAvatarLocator(ava);
				t.setUserLocator(rs.getString(IUserSchema.USER_NAME));
				t.setProperty(IUserSchema.USER_EMAIL, email);
		//		t.setProperty(IUserSchema.USER_ROLE, rs.getString(IUserSchema.USER_ROLE));
				t.setProperty(IUserSchema.USER_FULLNAME, rs.getString(IUserSchema.USER_FULLNAME));
				s2 = con.prepareStatement(IUserSchema.getUserProperties);
				s2.setString(1, userName);
				rs2 = s2.executeQuery();
				while (rs2.next()) {
					key = rs2.getString("prop");
					val = rs2.getString("val");
				//	System.out.println("GETPROP "+userName+" "+key+" | "+val);
					t.setProperty(key, val);
				}
				result.setResultObject(t);
			}
			//otherwise return null
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s,result);
			closeResultSet(rs,result);
			if (s2 != null)
				closePreparedStatement(s2,result);
			if (rs2 != null)
				closeResultSet(rs2,result);
		}
		return result;	
	}


	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#insertUser(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUser(Connection con, String email, String userName,
			String password, String userFullName, String avatar, String role, String homepage, String geolocation) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		PreparedStatement s2 = null;
		try {
			String pwd = sha1(password);
			s = con.prepareStatement(IUserSchema.putUser);
			s.setString(1, email);
			s.setString(2, pwd);
			s.setString(3, userName);
			s.setString(4, userFullName);
		//	s.setString(5, "");
		//	s.setString(6, "");
			boolean x = s.execute();
			System.out.println("INSERTUSER1 "+x);
			s2 = con.prepareStatement(IUserSchema.putUserProperty);
			s2.setString(1, userName);
			s2.setString(2, IUserSchema.USER_HOMEPAGE);
			s2.setString(3, homepage);
			x = s2.execute();
			System.out.println("INSERTUSER2 "+x);
			s2.clearParameters();
			s2.setString(1, userName);
			s2.setString(2, IUserSchema.USER_GEOLOC);
			s2.setString(3, geolocation);
			x = s2.execute();
			s2.clearParameters();
			s2.setString(1, userName);
			s2.setString(2, IUserSchema.USER_ROLE);
			s2.setString(3, role);
			x = s2.execute();
			s2.clearParameters();
			s2.setString(1, userName);
			s2.setString(2, IUserSchema.USER_AVATAR);
			s2.setString(3, avatar);
			x = s2.execute();

			System.out.println("INSERTUSER3 "+x);
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			closePreparedStatement(s,result);
			if (s2 != null)
				closePreparedStatement(s2,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#insertEncryptedUser(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 * /
	@Override
	public IResult insertEncryptedUser(Connection con, String userName,
			String password, String grant) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#insertUserData(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUserData(Connection con, String userName,
			String propertyType, String propertyValue) {
		System.out.println("INSERTUSERDATA "+userName+" "+propertyType+" "+propertyValue);
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			s = con.prepareStatement(IUserSchema.putUserProperty);
			s.setString(1, userName);
			s.setString(2, propertyType);
			s.setString(3, propertyValue);
			s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#updateUserData(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserData(Connection con, String userName,
			String propertyType, String newValue) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.updateUserProperty);
			s.setString(1, newValue);
			s.setString(2, propertyType);
			s.setString(3, userName);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#existsUsername(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult existsUsername(Connection con, String userName) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		ResultSet rs = null;
		Boolean x;
		try {
			s = con.prepareStatement(IUserSchema.getUserByName);
			s.setString(1, userName);
			rs = s.executeQuery();
			if (rs.next()) {
				x = new Boolean(true);
			} else {
				x = new Boolean(false);
			}
			result.setResultObject(x);
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s,result);
			closeResultSet(rs,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#removeUser(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult removeUser(Connection con, String userName) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.removeUser);
			s.setString(1, userName);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#changeUserPassword(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult changeUserPassword(Connection con, String userName,
			String newPassword) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			String pwd = sha1(newPassword);
			s = con.prepareStatement(IUserSchema.updateUserPwd);
			s.setString(1, pwd);
			s.setString(2, userName);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#addUserRole(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserRole(Connection con, String userName, String newRole) {
		return updateUserData(con, userName, IUserSchema.USER_ROLE, newRole);
/*		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.updateUserRole);
			s.setString(1, newRole);
			s.setString(2, userName);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s,result);
		}
		return result; */
	}
	@Override
	public IResult updateUserEmail(Connection con, String userName,
			String newEmail) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.updateUserEmail);
			s.setString(1, newEmail);
			s.setString(2, userName);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#listUserLocators(java.sql.Connection)
	 */
	@Override
	public IResult listUserLocators(Connection con) {
		IResult result = new ResultPojo();
		Statement s = null;
		ResultSet rs = null;
		Boolean x;
		try {
			List<String>users = new ArrayList<String>();
			result.setResultObject(users);
			s = con.createStatement();
			rs = s.executeQuery(IUserSchema.listUserNames);
			while (rs.next()) {
				users.add(rs.getString("name"));
			}
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closeStatement(s,result);
			closeResultSet(rs,result);
		}
		return result;
	}

	@Override
	public IResult listUsers(Connection con, int start, int count) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		PreparedStatement s2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String sql = IUserSchema.listUsers;
		if (count > -1)
			sql = IUserSchema.listUsersLimited;
		
		try {
			List<ITicket>users = new ArrayList<ITicket>();
			result.setResultObject(users);
			s = con.prepareStatement(sql);
			if (count > -1) {
				s.setInt(1, start);
				s.setInt(2, count);
			}
			rs = s.executeQuery();
			String name, key,val, ava;
			ITicket t;
			while (rs.next()) {
				t = new TicketPojo();
				name = rs.getString(IUserSchema.USER_NAME);
			//	ava = rs.getString(IUserSchema.USER_AVATAR);
			//	if (!ava.equals(""))
			//		t.addAvatarLocator(ava);;
				t.setUserLocator(name);
				t.setProperty(IUserSchema.USER_EMAIL, rs.getString(IUserSchema.USER_EMAIL));
				//t.setProperty(IUserSchema.USER_ROLE, rs.getString(IUserSchema.USER_ROLE));
				t.setProperty(IUserSchema.USER_FULLNAME, rs.getString(IUserSchema.USER_FULLNAME));
				s2 = con.prepareStatement(IUserSchema.getUserProperties);
				s2.setString(1, name);
				rs2 = s2.executeQuery();
				while (rs2.next()) {
					key = rs2.getString("prop");
					val = rs2.getString("val");
			//		System.out.println("GETPROP2 "+name+" "+key+" | "+val);
					t.setProperty(key, val);
				}
				users.add(t);
			}
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closeStatement(s,result);
			closeResultSet(rs,result);
			if (s2 != null)
				closeStatement(s2,result);
			if (rs2 != null)
				closeResultSet(rs2,result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#exportSchema()
	 */
	@Override
	public void exportSchema() throws Exception {
	    Connection con = null;
	    try {
	      String[] sql = IUserSchema.TABLES;
	      int len = sql.length;
	      con = getSQLConnection();
	      Statement s = con.createStatement();
	      for (int i = 0; i < len; i++) {
	    	environment.logDebug(sql[i]);
	    	System.out.println("EXPORT: "+sql[i]);
	        s.execute(sql[i]);
	      }
	      s.close();
	      con.close();
	    }
	    catch (SQLException e) {
	      throw new Exception(e);
	    }
	}

}
