/**
 * 
 */
package org.topicquests.backside.servlet.apps.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.api.ISecurity;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.backside.servlet.apps.CredentialCache;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserSchema;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.api.ITicket;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class GUIHandler {
	private ServletEnvironment environment;
	private IUserModel model;
	private CredentialCache credentialCache;

	/**
	 * 
	 */
	public GUIHandler(ServletEnvironment env) {
		environment = env;
		model = environment.getUserModel();
		credentialCache = environment.getCredentialCache();
	}

	public void handleGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getPath(request);
		System.out.println("GUIGETPATH "+path);
		String html="";
		if (path.equals(""))
			html = getLoginHtml();
		else if (path.startsWith("menu")) {
			//Paint a menu
			String rtoken = path.substring("menu/".length());
			if (!rtoken.equals("")) {
				ITicket credentials = credentialCache.getTicket(rtoken);
				html = getMenuHtml(credentials, rtoken);
			} else {
				response.sendRedirect("/");
				return;
			}
		}
		sendHTML(html, response);
				
	}

	public void handlePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getPath(request);
		
		System.out.println("GUIPOSTPATH "+path);
		if (path.equals("login")) {
			String email = request.getParameter("email");
			String pwd = request.getParameter("pword");
			System.out.println("LoggingIn "+email);
			IResult r = model.authenticate(email, pwd);
			ITicket t = (ITicket)r.getResultObject();
			if (t != null) {
				String rtoken = newUUID();
				
				credentialCache.putTicket(rtoken, t);
				response.sendRedirect("/gui/menu/"+rtoken);
			}
		}

				
	}
	
	//not used
	String grabSessionToken(String path) {
		String result = path;
		return result;
	}
	
	String newUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Here from menu; only after login.
	 * @param credentials
	 * @param rToken
	 * @return
	 */
	String getMenuHtml(ITicket credentials, String rToken) throws ServletException {
		System.out.println("GETMENU "+credentials);
		StringBuilder buf = new StringBuilder();
		buf.append("<html><head></head><body>");
		
		buf.append("<h2>TMAServlet User Menu</h2>");
		if (isAdmin(credentials)) {
			buf.append("<p><a href=\"/administrator/"+getCoreQuery("admin", credentials.getUserLocator(), "", rToken)+"\">Administration View</a></p>");
		}
		buf.append("<p><a href=\"/upload/"+getCoreQuery("uUpload", credentials.getUserLocator(), "", rToken)+"\">Upload File</a></p>");
		buf.append("</body></html>");
		return buf.toString();
	}
	
	boolean isAdmin(ITicket credentials) {
		String roles = (String)credentials.getProperty(IUserSchema.USER_ROLE);
		return (roles.indexOf(ISecurity.ADMINISTRATOR_ROLE) > -1);
	}
	
	String getCoreQuery(String verb, String userId, String userIP, String sToken) throws ServletException {
		try {
			JSONObject json = new JSONObject();
			json.put(ICredentialsMicroformat.VERB, verb);
			json.put(ICredentialsMicroformat.USER_NAME, userId);
			json.put(ICredentialsMicroformat.SESSION_TOKEN, sToken);
			json.put(ICredentialsMicroformat.USER_IP, userIP);
			String result = json.toJSONString();
			result = URLEncoder.encode(result, "UTF-8");
			json = null;
			return result;
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			throw new ServletException(e);
		}
	}
	String getLoginHtml() {
		StringBuilder buf = new StringBuilder();
		buf.append("<html><head></head><body>");
		buf.append("<form name = \"login\" method=\"post\" action=\"login\">");
		buf.append("<p>ENTER EMAIL <input type=\"text\" name=\"email\"></p>");
		buf.append("<p>ENTER PASSWORD <input type=\"password\" name=\"pword\"></p>");
		buf.append("<input type=\"submit\" value=\"Check In\" name=\"Submit\">");
		buf.append("</form>");
		buf.append("</body></html>");
		return buf.toString();
	}

	protected void sendHTML(String html, HttpServletResponse response) throws IOException {
    	response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
    	out.write(html);
    	out.close();
	}

    /**
     * <p>Paths that come in without a trailing '/' can be null</p>
     * @param request
     * @return
     * @throws ServletException
     */
    protected String getPath(HttpServletRequest request) throws ServletException {
    	String path = notNullString(request.getPathInfo()).trim();
//    	System.out.println("FOO "+path);
//    	System.out.println("QUERY "+this.getQueryString(request));
//    	System.out.println("DATA "+request.getParameter("data"));
//    	System.out.println("LENGTH "+request.getContentLength());
    	//Enumeration<String> ex = request.getHeaderNames();
    	Enumeration<String> ex = request.getParameterNames();
    	if (ex != null) {
    		List<String>l = new ArrayList<String>();
    		while (ex.hasMoreElements())
    			l.add(ex.nextElement());
    		//System.out.println("HEADERNAMES "+l);
    		//HEADERNAMES [Origin, Accept, Connection, User-Agent, Referer, Host, DNT, Accept-Encoding, Accept-Language, Content-Length, Content-Type]
    		//That was no help
 //   		System.out.println("PARAMNAMES "+l);
    		//PARAMNAMES [{"verb":"Auth","uEmail":"joe","uPwd":"joe"}]
    		// the first one is our data
    	}
    	
    	try {
    		InputStream ins = request.getInputStream();
    		if (ins != null) {
    			StringBuilder buf = new StringBuilder();
    			int c;
    			while ((c = ins.read()) > -1)
    				buf.append((char)c);
        		System.out.println(buf.toString());
    			
    		}
    	} catch (Exception x) {
    		System.out.println("BaseHandler.getPath booboo "+x.getMessage());
    	}
    	if (path.startsWith("/"))
    		path = path.substring(1);
    	if (path.endsWith("/"))
    		path = path.substring(0,path.length()-1);
    	try {
    		path = URLDecoder.decode(path, "UTF8");
    	} catch (Exception e) {
    		throw new ServletException(e);
    	}
    	if (path != null && path.startsWith("/"))
    		path = path.substring(1);
    	System.out.println(path);
    	return path;
    }
    
    protected String notNullString(String val) {
    	if (val != null) return val;
    	return "";
    }

}
