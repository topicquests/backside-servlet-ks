/**
 * 
 */
package org.topicquests.backside.servlet.apps.base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.topicquests.backside.servlet.ServletEnvironment;

/**
 * @author park
 * <p>A 404 default servlet</p>
 */
public class BaseServlet extends HttpServlet {
	private ServletEnvironment environment;
    
	public BaseServlet(ServletEnvironment env, String basePath) {
		environment = env;
	}
	
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//IGNORE
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//This is really a 404 servlet: if some browser does a GET here, we respond thus
		response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
    	out.write("<h1>Wrong!</h1>");
    	out.close();
	}

    public void destroy() {
		environment.shutDown();  
    }

}
