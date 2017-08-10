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
package org.topicquests.backside.servlet.apps.usr.persist.h2;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.IRDBMSDatabase;
import org.topicquests.backside.servlet.apps.usr.api.IUserMicroformat;
import org.topicquests.backside.servlet.apps.usr.api.IUserPersist;
import org.topicquests.backside.servlet.apps.usr.api.IUserSchema;
import org.topicquests.backside.servlet.apps.usr.persist.PasswordStorage;
import org.topicquests.backside.servlet.persist.rdbms.H2DatabaseDriver;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ITicket;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author park
 */
public class H2UserDatabase extends H2DatabaseDriver implements IUserPersist, IRDBMSDatabase {
	private final String h2JdbcDriver = "org.h2.Driver";
	private String userName, password, connectionString;

	/**
	 *
	 */
	public H2UserDatabase(ServletEnvironment env, String dbName, String userName, String pwd, String filePath)
			throws Exception {
		super(env, dbName, userName, pwd, filePath);
		init();
	}

	private void init() throws Exception {
		Connection con = null;
		ResultSet rs = null;
		Statement s = null;
		try {
			con = getSQLConnection();
			s = con.createStatement();
			rs = s.executeQuery("select * from users");
		} catch (Exception e) {
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

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#authenticate(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult authenticate(Connection con, String identifier, String password) {
		System.out.println("AUTH-0 "+identifier);

		IResult result = new ResultPojo();
		if (identifier == null || password == null) {
			result.addErrorString("Missing handle/email or password");
			return result;
		}
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			s = con.prepareStatement(IUserSchema.authenticate);
			s.setString(1, identifier);
			s.setString(2, identifier);
			rs = s.executeQuery();

			if (rs.next()) {
				String hash = rs.getString(IUserSchema.USER_PASSWORD);
				System.out.println("AUTH-2 "+hash);
				boolean verified = PasswordStorage.verifyPassword(password, hash);
				System.out.println("AUTH-9 "+verified);

				if (verified) {
					String id = rs.getString(IUserSchema.USER_ID);
					result = this.getTicketById(con, id);
				}
			}
			//otherwise return null
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			closePreparedStatement(s, result);
			closeResultSet(rs, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#getTicket(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult getTicketByHandle(Connection con, String userHandle) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			s = con.prepareStatement(IUserSchema.getUserByHandle);
			s.setString(1, userHandle);
			rs = s.executeQuery();
			if (rs.next()) {
				String id = rs.getString(IUserSchema.USER_ID);
				result = this.getTicketById(con, id);
			}
			//otherwise return null
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s, result);
			closeResultSet(rs, result);
		}
		return result;
	}

	@Override
	public IResult getTicketByEmail(Connection con, String email) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			s = con.prepareStatement(IUserSchema.getUserByEmail);
			s.setString(1, email);
			rs = s.executeQuery();
			if (rs.next()) {
				String id = rs.getString(IUserSchema.USER_ID);
				result = this.getTicketById(con, id);
			}
			//otherwise return null
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s, result);
			closeResultSet(rs, result);
		}
		return result;
	}

	@Override
	public IResult getTicketById(Connection con, String userId) {
		System.out.println("GETTICKET "+userId);
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		PreparedStatement s2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			s = con.prepareStatement(IUserSchema.getUserById);
			s.setString(1, userId);
			rs = s.executeQuery();
			if (rs.next()) {
				String key, val;
				ITicket t = new TicketPojo();
				t.setUserLocator(rs.getString(IUserSchema.USER_ID));
				t.setProperty(IUserSchema.USER_EMAIL, rs.getString(IUserSchema.USER_EMAIL));
				t.setProperty(IUserSchema.USER_NAME, rs.getString(IUserSchema.USER_NAME));
				t.setProperty(IUserSchema.USER_FULLNAME, rs.getString(IUserSchema.USER_FULLNAME));
				s2 = con.prepareStatement(IUserSchema.getUserProperties);
				s2.setString(1, userId);
				rs2 = s2.executeQuery();
				List<String> roles = new ArrayList<String>();
				while (rs2.next()) {
					key = rs2.getString("prop");
					if (key.equals(IUserSchema.USER_ROLE)) {
						roles.add(rs2.getString("val"));
					} else {
						val = rs2.getString("val");
						t.setProperty(key, val);
					}
				}
				//HERE, we must convert roles to string
				t.setProperty(IUserMicroformat.USER_ROLE, roles);
				result.setResultObject(t);
			}
			//otherwise return null
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s, result);
			closeResultSet(rs, result);
		}
		return result;
	}


	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#insertUser(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUser(Connection con, String email, String userHandle,
							  String userId, String password, String userFullName,
							  String avatar, String role, String homepage, String geolocation) {
		IResult result = new ResultPojo();
		PreparedStatement putUser = null;
		PreparedStatement putUserProperty = null;
		try {
			String hash = PasswordStorage.createHash(password);
			con.setAutoCommit(false);

			putUser = con.prepareStatement(IUserSchema.putUser);
			putUser.setString(1, email);
			putUser.setString(2, hash);
			putUser.setString(3, userId);
			putUser.setString(4, userHandle);
			putUser.setString(5, userFullName);
			int x = putUser.executeUpdate();

			putUserProperty = con.prepareStatement(IUserSchema.putUserProperty);
			putUserProperty.setString(1, userId);
			putUserProperty.setString(2, IUserSchema.USER_HOMEPAGE);
			putUserProperty.setString(3, homepage);
			x = putUserProperty.executeUpdate();

			putUserProperty.clearParameters();
			putUserProperty.setString(1, userId);
			putUserProperty.setString(2, IUserSchema.USER_GEOLOC);
			putUserProperty.setString(3, geolocation);
			x = putUserProperty.executeUpdate();

			putUserProperty.clearParameters();
			putUserProperty.setString(1, userId);
			putUserProperty.setString(2, IUserSchema.USER_ROLE);
			putUserProperty.setString(3, role);
			x = putUserProperty.executeUpdate();

			putUserProperty.clearParameters();
			putUserProperty.setString(1, userId);
			putUserProperty.setString(2, IUserSchema.USER_AVATAR);
			putUserProperty.setString(3, avatar);
			x = putUserProperty.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			result.addErrorString(e.getMessage());
			environment.logError(e.getMessage(), e);
			try {
				con.rollback();
			} catch (SQLException e2) {
				result.addErrorString(e2.getMessage());
				environment.logError(e2.getMessage(), e2);
			}
		} catch (Exception e) {
			result.addErrorString(e.getMessage());
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(putUser, result);
			if (putUserProperty != null) {
				closePreparedStatement(putUserProperty, result);
			}
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				result.addErrorString(e.getMessage());
				environment.logError(e.getMessage(), e);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#insertEncryptedUser(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 * /
	@Override
	public IResult insertEncryptedUser(Connection con, String userId,
			String password, String grant) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#insertUserData(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUserData(Connection con, String userId, String propertyType, String propertyValue) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.putUserProperty);
			s.setString(1, userId);
			s.setString(2, propertyType);
			s.setString(3, propertyValue);
			s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s, result);
		}
		return result;
	}

	@Override
	public IResult removeUserData(Connection con, String userId, String propertyType, String oldValue) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.removeUserProperty);
			s.setString(1, userId);
			s.setString(2, propertyType);
			s.setString(3, oldValue);
			s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
		} finally {
			closePreparedStatement(s, result);
		}
		return result;
	}
	
	//////////////////////////////////
	// AN ISSUE
	// If we try to insert a value that's already there, at risk of having two copies.
	// If we try to update a value that's not there, have to insert instead.
	// So, we choose Update and test to see if it's updatable
	/////////////////////////////////
	
	IResult getUserDataValue(Connection con, String userId, String propertyType) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			s = con.prepareStatement(IUserSchema.getUserPropertyValue);
			s.setString(1, userId);
			s.setString(2, propertyType);
			rs = s.executeQuery();
			if (rs.next())
				result.setResultObject(rs.getString("val"));
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s, result);
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#updateUserData(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserData(Connection con, String userId, String propertyType, String newValue) {
		IResult result = new ResultPojo();
		IResult r = getUserDataValue(con, userId, propertyType);
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		if (r.getResultObject() == null) {
			this.insertUserData(con, userId, propertyType, newValue);
		} else {
			PreparedStatement s = null;
			try {
				s = con.prepareStatement(IUserSchema.updateUserProperty);
				s.setString(1, newValue);
				s.setString(2, propertyType);
				s.setString(3, userId);
				boolean x = s.execute();
			} catch (Exception e) {
				environment.logError(e.getMessage(), e);
				result.addErrorString(e.getMessage());
			} finally {
				closePreparedStatement(s, result);
			}
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
			s = con.prepareStatement(IUserSchema.getUserByHandle);
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
			closePreparedStatement(s, result);
			closeResultSet(rs, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#removeUser(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult removeUser(Connection con, String userId) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.removeUser);
			s.setString(1, userId);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#changeUserPassword(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult changeUserPassword(Connection con, String userId, String newPassword) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			String hash = PasswordStorage.createHash(newPassword);
			s = con.prepareStatement(IUserSchema.updateUserPwd);
			s.setString(1, hash);
			s.setString(2, userId);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s, result);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IUserPersist#addUserRole(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult addUserRole(Connection con, String userId, String newRole) {
		return this.insertUserData(con, userId, IUserSchema.USER_ROLE, newRole);
	}

	@Override
	public IResult removeUserRole(Connection con, String userId, String oldRole) {
		return this.removeUserData(con, userId, IUserSchema.USER_ROLE, oldRole); //(con, userId, IUserSchema.USER_ROLE, oldRole);
	}

	@Override
	public IResult updateUserEmail(Connection con, String userId, String newEmail) {
		IResult result = new ResultPojo();
		PreparedStatement s = null;
		try {
			s = con.prepareStatement(IUserSchema.updateUserEmail);
			s.setString(1, newEmail);
			s.setString(2, userId);
			boolean x = s.execute();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closePreparedStatement(s, result);
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
			List<String> users = new ArrayList<String>();
			result.setResultObject(users);
			s = con.createStatement();
			rs = s.executeQuery(IUserSchema.listUserLocators);
			while (rs.next()) {
				users.add(rs.getString("id"));
			}
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closeStatement(s, result);
			closeResultSet(rs, result);
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
			List<ITicket> users = new ArrayList<ITicket>();
			result.setResultObject(users);
			s = con.prepareStatement(sql);
			if (count > -1) {
				s.setInt(1, count);
				s.setInt(2, start);
			}
			rs = s.executeQuery();
			String name, key, val, ava;
			ITicket t;
			IResult r;
			while (rs.next()) {
				t = new TicketPojo();
				name = rs.getString(IUserSchema.USER_NAME);
				//environment.logDebug("H2-1 "+name);
				r = this.getTicketByHandle(con, name);
				t = (ITicket) r.getResultObject();
				if (r.hasError())
					result.addErrorString(r.getErrorString());
				users.add(t);
			}
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			result.addErrorString(e.getMessage());
		} finally {
			closeStatement(s, result);
			closeResultSet(rs, result);
			if (s2 != null)
				closeStatement(s2, result);
			if (rs2 != null)
				closeResultSet(rs2, result);
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
				System.out.println("EXPORTING "+sql[i]);
				s.execute(sql[i]);
			}
			s.close();
			con.close();
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}

	@Override
	public IResult migrateUserId(Connection con, String oldUserId, String newUserId) {
		IResult result = new ResultPojo();
			
			PreparedStatement s = null;
			try {
				s = con.prepareStatement(IUserSchema.migrateUserId);
				s.setString(1, newUserId);
				s.setString(2, oldUserId);
				boolean x = s.execute();
				System.out.println("H2UserDatabase.migrating "+x+" "+oldUserId+" "+newUserId);
			} catch (Exception e) {
				environment.logError(e.getMessage(), e);
				result.addErrorString(e.getMessage());
			} finally {
				closePreparedStatement(s, result);
			}
		return result;
	}

}
