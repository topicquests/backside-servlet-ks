/**
 * 
 */
package org.topicquests.backside.servlet.apps.usr;

import java.io.IOException;

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
public class UserServlet extends HttpServlet {
	private ServletEnvironment environment;
	private UserHandler handler;
    
	public UserServlet(ServletEnvironment env, String basePath) {
		environment = env;
		handler = new UserHandler(environment,basePath);
		environment.logDebug("UserServlet started");
	}
	
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handler.executePost(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handler.executeGet(request, response);
	}

    public void destroy() {
		environment.shutDown();
		handler.shutDown();
    }

}
