/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.topicquests.backside.servlet.ServletEnvironment;

/**
 * @author park
 *
 */
public class TopicMapServlet extends HttpServlet {
	private ServletEnvironment environment;
	private TopicMapHandler handler;

	/**
	 * 
	 */
	public TopicMapServlet(ServletEnvironment env, String basePath) {
		environment = env;
		handler = new TopicMapHandler(environment, basePath);
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
