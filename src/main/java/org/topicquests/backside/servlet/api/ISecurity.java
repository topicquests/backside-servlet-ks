/*
 *  Copyright (C) 2007  Jack Park,
 * 	mail : jackpark@gmail.com
 *
 *  Part of <TopicSpaces>, an open source project.
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

/**
 * 
 * @author jackpark
 *
 */
public interface ISecurity {
	public static final String GUEST_USER = "guest";
	/**
	 * Read-Only Permission
	 */
	public static final String READ_ONLY = "xro";
	/**
	 * Read-Write Permission
	 */
	public static final String READ_WRITE = "xrw";

	/**
	 * This is the GOD-like role: can do anything anywhere
	 */
	public static final String OWNER_ROLE = "ror";
	/**
	 * Portal administrator role: has Read-Write and Administrative
	 * rights outside Groups
	 */
	public static final String ADMINISTRATOR_ROLE = "rar";
	
	/**
	 * Portal moderator role: has Read-Write abilities above
	 * users, but below administrators, outside Groups
	 */
	public static final String MODERATOR_ROLE = "mor";
	
	/**
	 * General public browsing. Read-Only; cannot enter Groups
	 */
	public static final String GUEST_ROLE = "rgr";
	/**
	 * General public Read-Write; outside Groups
	 */
	public static final String USER_ROLE = "rur";
	/**
	 * Administrative and Read-Write inside particular Group
	 */
	public static final String MEMBER_ADMIN_ROLE = "rmar";
	/**
	 * Non-administrative Read-Write inside particular Group
	 */
	public static final String MEMBER_USER_ROLE = "rmur";
	/**
	 * Read-Only inside particular Group
	 */
	public static final String MEMBER_GUEST_ROLE = "rmgr";
}
