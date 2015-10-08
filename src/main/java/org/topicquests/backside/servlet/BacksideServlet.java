/**
 * 
 */
package org.topicquests.backside.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author park
 * <p>Provide WebServices for Node.js Clients</p>
 * <p>Only needs to respond to JSON Queries with JSON responses</p>
 */
public class BacksideServlet extends HttpServlet {
	private ServletEnvironment environment;
	private AppHandler handler;
    
	public BacksideServlet(ServletEnvironment env, String basePath) {
		environment = env;
		handler = new AppHandler(environment,basePath);
	}
		
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handler.executePost(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handler.executeGet(request, response);
	}

    public void destroy() {
		environment.shutDown();  
    }

}
