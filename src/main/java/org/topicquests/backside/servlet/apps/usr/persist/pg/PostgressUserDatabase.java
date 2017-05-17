/**
 * 
 */
package org.topicquests.backside.servlet.apps.usr.persist.pg;

import java.sql.Connection;
import java.sql.SQLException;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.IRDBMSDatabase;
import org.topicquests.backside.servlet.apps.usr.api.IUserPersist;
import org.topicquests.support.api.IResult;

/**
 * @author Admin
 *
 */
public class PostgressUserDatabase implements IUserPersist, IRDBMSDatabase {
	private ServletEnvironment environment;

	/**
	 * 
	 */
	public PostgressUserDatabase(ServletEnvironment env, String dbName, String userName, String pwd, String filePath) {
		environment = env;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IRDBMSDatabase#getConnection()
	 */
	@Override
	public Connection getConnection() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IRDBMSDatabase#shutDown()
	 */
	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.api.IRDBMSDatabase#closeConnection(java.sql.Connection)
	 */
	@Override
	public void closeConnection(Connection con) throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#authenticate(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult authenticate(Connection con, String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#getTicketByHandle(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult getTicketByHandle(Connection con, String userHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#getTicketByEmail(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult getTicketByEmail(Connection con, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#getTicketById(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult getTicketById(Connection con, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#insertUser(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUser(Connection con, String email, String userHandle, String userId, String password,
			String userFullName, String avatar, String role, String homepage, String geolocation) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#insertUserData(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult insertUserData(Connection con, String userName, String propertyType, String propertyValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#updateUserData(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserData(Connection con, String userName, String propertyType, String newValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#removeUserData(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult removeUserData(Connection con, String userName, String propertyType, String oldValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#existsUsername(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult existsUsername(Connection con, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#removeUser(java.sql.Connection, java.lang.String)
	 */
	@Override
	public IResult removeUser(Connection con, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#changeUserPassword(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult changeUserPassword(Connection con, String userName, String newPassword) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#addUserRole(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult addUserRole(Connection con, String userName, String newRole) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#removeUserRole(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult removeUserRole(Connection con, String userName, String oldRole) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#updateUserEmail(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult updateUserEmail(Connection con, String userName, String newEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#listUserLocators(java.sql.Connection)
	 */
	@Override
	public IResult listUserLocators(Connection con) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#listUsers(java.sql.Connection, int, int)
	 */
	@Override
	public IResult listUsers(Connection con, int start, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#exportSchema()
	 */
	@Override
	public void exportSchema() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.usr.api.IUserPersist#migrateUserId(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public IResult migrateUserId(Connection con, String oldUserId, String newUserId) {
		// TODO Auto-generated method stub
		return null;
	}

}
