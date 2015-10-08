/**
 * 
 */
package org.topicquests.backside.servlet.apps;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.api.IErrorMessages;
import org.topicquests.backside.servlet.api.ISecurity;
import org.topicquests.backside.servlet.apps.auth.api.IAuthMicroformat;
import org.topicquests.backside.servlet.apps.usr.api.IUserMicroformat;
import org.topicquests.backside.servlet.apps.usr.api.IUserSchema;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.ks.SystemEnvironment;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITicket;

/**
 * @author park
 *
 */
public abstract class BaseHandler {
	/**
	 * 200
	 */
	public static final int RESPONSE_OK = HttpServletResponse.SC_OK;
	/**
	 * 400
	 */
	public static final int RESPONSE_BAD = HttpServletResponse.SC_BAD_REQUEST;
	/**
	 * 401
	 */
	public static final int RESPONSE_UNAUTHORIZED = HttpServletResponse.SC_UNAUTHORIZED;
	/**
	 * 403
	 */
	public static final int RESPONSE_FORBIDDEN = HttpServletResponse.SC_FORBIDDEN;
	/**
	 * 404
	 */
	public static final int RESPONSE_NOT_FOUND = HttpServletResponse.SC_NOT_FOUND;
	/**
	 * 407
	 */
	public static final int RESPONSE_AUTHENTICATION_REQUIRED = HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED;
	/**
	 * 500
	 */
	public static final int RESPONSE_INTERNAL_SERVER_ERROR = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	protected ServletEnvironment environment;
	protected SystemEnvironment tmEnvironment=null;
	protected String _basePath;
	private ITicket guestCredentials;
	protected CredentialCache credentialCache;

	/**
	 * 
	 */
	public BaseHandler(ServletEnvironment env, String basePath) {
		environment = env;
		_basePath = basePath;
		tmEnvironment = environment.getTopicMapEnvironment();
		guestCredentials = new TicketPojo();
		guestCredentials.setUserLocator(ITQCoreOntology.GUEST_USER);
		credentialCache = environment.getCredentialCache();
		//caller MUST send in "guest" as token if not authenticated
		credentialCache.putTicket(ITQCoreOntology.GUEST_USER, guestCredentials);
	}

	///////////////////////////////////////////////////////////////////
	// GetTicket is the workhorse of platform security:
	//	We must determine:
	//		a) who this user is
	//			which really means proving this user is not spoofing some other user
	//		b) if this user is authenticated
	// Theories behind this include:
	//		Combination of 
	//			username (which must be unique)
	//			caller's IP address
	//				issues of what happens on mobile users
	//		All of which means that the query string must include enough information
	//			to determine who this is.
	// CODE as written for processRequest really means this:
	//	each app should test to make sure that the verb it gets is a legal verb
	//  If not, it should throw an exception
	///////////////////////////////////////////////////////////////////
	
	/**
	 * The game here is to return two objects: {@link ITicket} and {@link JSONObject}
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	private IResult processRequest(HttpServletRequest request) throws ServletException {
		IResult result = new ResultPojo();
		ITicket t = guestCredentials; //default
		String pt = getPath(request);
		//seeing things like auth/{....}
		System.out.println("PR "+pt);
		//PR {"verb":"PutTopic","uIP":"173.164.129.250","uName":"jackpark","cargo":{"uName
		//":"jackpark","Lang":"en","Label":"My test topic","Details":"Created to test the
		//AngularApp code","LiP":"/images/cogwheel.png","SiP":"/images/cogwheel.sm.png","I
		//sPvt":"F","SType":"ClassType"}}
		if (!pt.startsWith("{")) {
			int where = pt.indexOf('/');
			if (where > -1)  {
				pt = pt.substring(where+1);
			}
		}
		JSONObject jo = jsonFromString(pt);
		result.setResultObjectA(jo);
		String x = (String)jo.get(ICredentialsMicroformat.USER_IP);
		System.out.println("GET IP "+x);
		String verb = (String)jo.get(ICredentialsMicroformat.VERB);
		if (!verb.equals(IAuthMicroformat.AUTHENTICATE)) {
			String token = (String)jo.get(ICredentialsMicroformat.SESSION_TOKEN);
			if (token != null) {
				t = credentialCache.getTicket(token);
				if (t == null) {
					x = IErrorMessages.TOKEN_NO_USER+token;
					//CASE: someone sent in a token, but we don't know who it is: big error
					// could be that the same platform logged out before
					environment.logError(x, null);
					throw new ServletException(x);
				} 
			} 
		}
		result.setResultObject(t);
		return result;
	}
	protected abstract void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException;
	
	protected abstract void handlePost(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException;
	
	
	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IResult r = processRequest(request);
		ITicket t = (ITicket)r.getResultObject();
		JSONObject jo = (JSONObject)r.getResultObjectA();
		handleGet(request,response, t, jo);
		r = null;
		t = null;
		jo = null;
	}
	
	public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IResult r = processRequest(request);
		ITicket t = (ITicket)r.getResultObject();
		JSONObject jo = (JSONObject)r.getResultObjectA();
		handlePost(request,response, t, jo);
		r = null;
		t = null;
		jo = null;
	}
	//////////////////////////////
	// utilities
	//////////////////////////////
    /**
     * Return a not <code>null</code> String
     */
    public String notNullString(String in) {
    	if (in == null) return "";
    	return in;
    }

    /**
     * <p>Paths that come in without a trailing '/' can be null</p>
     * @param request
     * @return
     * @throws ServletException
     */
    public String getPath(HttpServletRequest request) throws ServletException {
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

    public String getQueryString(HttpServletRequest request) {
    	return notNullString(request.getQueryString());
    }
//////////////////////////////////////
//    SENDJSON 0 {"rMsg":"","rToken":"","cargo":{"crDt":"2015-07-25T18:30:56-07:00","t
//    	rCl":["ClassType"],"crtr":"jackpark","lox":"37cbbf86-6861-43c0-85e7-facc95d2cf1e
//    	","sIco":"\/images\/cogwheel.sm.png","isPrv":"false","_ver":"1437874256133","lEd
//    	Dt":"2015-07-25T18:30:56-07:00","details":["Created to test the AngularApp code"
//    	],"label":["My test topic"],"lIco":"\/images\/cogwheel.png","inOf":"ClassType"}}
    /**
     * Ship JSON string out to the client
     * @param json
     * @param statusCode
     * @param response
     * @throws TopicSpacesException
     */
    public void sendJSON(String json, int statusCode, HttpServletResponse response) throws ServletException {
    	System.out.println("SENDJSON "+statusCode+" "+json);
    	try {
	    	response.setContentType("application/json; charset=UTF-8");
	    	response.setStatus(statusCode);
	    	//TODO: URL must come from a config file
	    	response.setHeader(" Access-Control-Allow-Origin", "http://localhost:8080/");
	    //	System.out.println("RESPONSE "+response.getContentType()+" "+response.getCharacterEncoding()+" "+statusCode);
	    //	System.out.println(json);
	   // 	environment.logDebug("BaseHandler.paintJSON "+json);
	        PrintWriter out = response.getWriter();
	    	out.write(json);
	    	out.close();
    	} catch (Exception e) {
    		throw new ServletException(e);
    	}
    }

    /**
     * Mostly for debugging
     * @param html
     * @param response
     * @throws Exception
     */
	public void sendHTML(String html, HttpServletResponse response) throws IOException {
    	response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
    	out.write(html);
    	out.close();
	}

	public String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }
	
	public String getIpAddr(HttpServletRequest request) {      
		   String ip = request.getHeader("x-forwarded-for");      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getHeader("Proxy-Client-IP");      
		   }      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getHeader("WL-Proxy-Client-IP");      
		   }      
		   if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
		       ip = request.getRemoteAddr();      
		   }      
		   return ip;      
	}
	
	//////////////////////////////////////
	// figuring out credentials
	//////////////////////////////////////
	
	private boolean isRole(ITicket ticket, String role) {
		boolean result = false;
		String roles = (String)ticket.getProperty(IUserSchema.USER_ROLE);
		if (roles != null)
			result = (roles.indexOf(role) > -1);
		return result;
		
	}
	public boolean isAdmin(ITicket ticket) {
		return isRole(ticket, ISecurity.ADMINISTRATOR_ROLE);
	}
	
	public boolean isOwner(ITicket ticket) {
		return isRole(ticket, ISecurity.OWNER_ROLE);
	}

	public boolean isUser(ITicket ticket) {
		return isRole(ticket, ISecurity.USER_ROLE);
	}
	
	public boolean isGuest(ITicket ticket) {
		return (ticket.getUserLocator().equals(ITQCoreOntology.GUEST_USER));
	}
	
	////////////////////////////////////
	// JSONObject support
	////////////////////////////////////
	
	public JSONObject newJSONObject() {
		return new JSONObject();
	}
	
	/**
	 * Convert <code>jsonString</code> to a {@link JSONObject}
	 * @param jsonString
	 * @return
	 * @throws ServletException
	 */
	public JSONObject jsonFromString(String jsonString) throws ServletException {
		JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		try {
			return (JSONObject)p.parse(jsonString);
		} catch (Exception e) {
			environment.logError(jsonString, e);
			throw new ServletException(e);
		}
	}
	
	///////////////////////////////////////
	// UUID
	///////////////////////////////////////
	public String newUUID() {
		return UUID.randomUUID().toString();
	}

	///////////////////////////////////////
	// Common to user and admin
	///////////////////////////////////////
	public JSONObject ticketToUser(ITicket t) {
		//name, email, avatar, homepage, geolocation, role
		JSONObject result = newJSONObject();
		result.put(IUserMicroformat.USER_EMAIL, t.getProperty(IUserSchema.USER_EMAIL));
		result.put(IUserMicroformat.USER_NAME, t.getUserLocator());
		String ava = "";
		List<String> avas = t.listAvatars();
		if (avas != null && avas.size() > 0)
			ava = avas.get(0); //FOR NOW, just show first one
		result.put(IUserMicroformat.USER_AVATAR, ava);
		result.put(IUserMicroformat.USER_HOMEPAGE, notNullString((String)t.getProperty(IUserSchema.USER_HOMEPAGE)));
		result.put(IUserMicroformat.USER_ROLE, t.getProperty(IUserSchema.USER_ROLE));
		result.put(IUserMicroformat.USER_GEOLOC, t.getProperty(IUserSchema.USER_GEOLOC));
		result.put(IUserMicroformat.USER_FULLNAME, t.getProperty(IUserSchema.USER_FULLNAME));
		return result;
	}

}
