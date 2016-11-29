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
package org.topicquests.backside.servlet.persist.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.IRDBMSDatabase;
import org.topicquests.common.api.IResult;
/**
 * <p>Title: StoryReader Engine</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008 Jack Park</p>
 *
 * <p>Company: NexistGroup</p>
 *
 * @author Jack Park
 */
public class H2DatabaseDriver implements IRDBMSDatabase {
	protected ServletEnvironment environment;
    public static final String h2JdbcDriver =
            "org.h2.Driver";
    protected String userName, password, connectionString;

    public H2DatabaseDriver(ServletEnvironment env, String dbName,String userName, String pwd, String filePath)
            throws Exception {
    	System.out.println("H2DBStart "+filePath);
    	environment = env;
        this.userName = userName;
        this.password = pwd;
        connectionString = "jdbc:h2:file:"+filePath+"/"+dbName;//+";create=true;";
        Class.forName(h2JdbcDriver).newInstance();
        environment.logDebug("H2Database started: "+dbName);
    }

    public Connection getSQLConnection() throws SQLException {
    	System.out.println("DATABASE GETCONNECTION "+connectionString);
    	//userName and password are used when creating tables, so they must be
    	// used when accessing those tables
        return DriverManager.getConnection(connectionString, userName,password);
    }

    
	////////////////////////////
	//Utilities
	
	public void closeConnection(Connection conn, IResult r) {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			r.addErrorString(e.getMessage());
		}
	}
	public void closeResultSet(ResultSet rs, IResult r) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			r.addErrorString(e.getMessage());
		}
	}
	public void closePreparedStatement(PreparedStatement ps, IResult r) {
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			r.addErrorString(e.getMessage());
		}
	}
	public void closeStatement(Statement s, IResult r) {
		try {
			if (s != null)
				s.close();
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			r.addErrorString(e.getMessage());
		}
	}

	@Override
	public Connection getConnection() throws Exception {
		return getSQLConnection();
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeConnection(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
/**
 * Template
		IResult result = new ResultObject();
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			conn = getSQLConnection();
			//TODO
		} catch (Exception e) {
			environment.logError(e.getMessage(),e);
			result.addErrorString(e.getMessage());
		} finally {
			closeResultSet(rs,result);
			closePreparedStatement(s,result);
			closeConnection(conn,result);
		}
		return result;
 */
