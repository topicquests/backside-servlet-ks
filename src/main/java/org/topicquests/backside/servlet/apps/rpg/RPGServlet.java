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
package org.topicquests.backside.servlet.apps.rpg;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.topicquests.backside.servlet.ServletEnvironment;

/**
 * @author park
 * This servlet is fired from config-props.xml
 */
public class RPGServlet extends HttpServlet {
	private ServletEnvironment environment;
	private RPGHandler handler;

	/**
	 * 
	 */
	public RPGServlet(ServletEnvironment env, String basePath) {
		environment = env;
		handler = new RPGHandler(environment, basePath);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//	handler.executePost(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//	handler.executeGet(request, response);
	}

    public void destroy() {
		environment.shutDown();
	//	handler.shutDown();
    }

}
