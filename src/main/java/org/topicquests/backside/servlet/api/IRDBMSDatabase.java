/*
 *  Copyright (C) 2009 Jack Park,
 * 	mail : jackpark@gmail.com
 *
 *  Part of IBIS Server, an open source project.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.topicquests.backside.servlet.api;

import java.sql.SQLException;
import java.sql.Connection;

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
public interface IRDBMSDatabase {

	Connection getConnection() throws Exception;

    void shutDown();
    
    void closeConnection(Connection con) throws SQLException;
}
