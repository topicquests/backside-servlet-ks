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
package org.topicquests.backside.servlet.apps.stat;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.topicquests.backside.servlet.ServletEnvironment;

/**
 * @author park
 * <p>Provide WebServices for Node.js Clients</p>
 * <p>Only needs to respond to JSON Queries with JSON responses</p>
 */
public class StaticFileServlet extends HttpServlet {
	private ServletEnvironment environment;
	private FileHelper helper;
//	private StaticFileAppHandler handler;
    
	public StaticFileServlet(ServletEnvironment env, String basePath) {
		environment = env;
		helper = new FileHelper(environment);
//		handler = new StaticFileAppHandler(environment,basePath);
	}
		
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//handler.executePost(request, response);
	}
	
	////////////////////////////////////////
	//TODO
	// Using FileHelper is an interim solution. We are bypassing the entire
	// Authentication/Permissions facility as a transient solution.
	// We must modify this to use StaticFileAppHandler later.
	// This means that the fetch HREF to get a file will have to have
	// appended to the query a bunch of params, e.g. session token, userId, etc
	////////////////////////////////////////
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		helper.handleGet(request, response);
	}

    public void destroy() {
		environment.shutDown();  
    }

}
