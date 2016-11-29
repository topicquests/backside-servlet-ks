/**
 * 
 */
package org.topicquests.backside.servlet.apps.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 *
 */
public class UploadHelper {
	private ServletEnvironment environment;
	private String fileBasePath;

	/**
	 * 
	 */
	public UploadHelper(ServletEnvironment env) {
		environment = env;
		fileBasePath = environment.getStringProperty("FileBasePath");
	}

	public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getPath(request);
		System.out.println("UPLOADHELPERGETPATH "+path);
		//{"uName":"joe","sToken":"d8611be2-1da2-4c0f-975b-d82ef1d5ce17","verb":"uUpload","uIP":""}
		JSONObject jo = jsonFromString(path);
		String trailer = (String)jo.get(ICredentialsMicroformat.VERB)+"/"+
				(String)jo.get(ICredentialsMicroformat.USER_ID)+"/"+
				(String)jo.get(ICredentialsMicroformat.SESSION_TOKEN)+"/"; //TODO add USERIP
		// uUpload/joe/0628c396-5781-4451-9435-7f9a3d1ee794
		String fp = FormPojo.getHtmlForm(trailer);
		sendHTML(fp, response);
	}
	
	public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getPath(request);
		String thePath = fileBasePath+"uploads/"; //TODO calculate the directory path for this file
		/////////////////////////////
		//TODO: thePath will be dependent on a verb retrieved from request
		/////////////////////////////
		String verb = getVerb(path);
		String userId = "";
		if (verb.equals("uUpload") ||
			verb.equals("muUpload")) {
			System.out.println("UserUpload "+thePath);
			//user upload
			userId = getUserId(path);
			String tempPath = thePath+userId;
			File dir = new File(tempPath);
			if (!dir.exists()) {
				boolean foo = dir.mkdir();
				System.out.println("MADEDIR "+foo);
			}
			thePath = dir.getAbsolutePath();
		}
		try {
			System.out.println("UPLOADING TO "+verb+" | "+userId+" | "+thePath);
			File repository = new File(thePath);
	    	String uploadPath = repository.getAbsolutePath()+"/";
	    	DiskFileItemFactory dfiFactory = dfiFactory = new DiskFileItemFactory();
	    	dfiFactory.setRepository(repository);
			ServletFileUpload servletFileUpload = new ServletFileUpload(dfiFactory);
			System.out.println("PARSING");
			List<FileItem> items = servletFileUpload.parseRequest(request);
			System.out.println("FILEUPLOADING "+path+
					" | "+items.size());
			Iterator<FileItem> iter = items.iterator();
    		while (iter.hasNext()) {
    		    FileItem item = iter.next();
		    	String fileName = item.getName();

    		    if (item.isFormField()) {
    		        //TODO processFormField(item);
    		    } else {
    		        //TODOprocessUploadedFile(item);
    		    	File f = new File(uploadPath+fileName);
    		    	item.write(f);
    		    }
    		    //TODO would be nice to have a socket and show progress
    		} 		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			e.printStackTrace();
		}
		if (verb.equals("uUpload"))
			response.sendRedirect("/gui");
		else
			sendHTML("ok", response);
	}
	
	String getVerb(String path) {
		String [] vals = path.split("/");
		return new String(vals[0]);
	}
	
	String getUserId(String path) {
		String [] vals = path.split("/");
		return new String(vals[1]);
	}
	JSONObject jsonFromString(String jsonString) throws ServletException {
		JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		try {
			return (JSONObject)p.parse(jsonString);
		} catch (Exception e) {
			environment.logError("BadJSON: "+jsonString, e);
			throw new ServletException(e);
		}
	}
    protected String getPath(HttpServletRequest request) throws ServletException {
    	String path = notNullString(request.getPathInfo()).trim();

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
	 void sendHTML(String html, HttpServletResponse response) throws IOException {
    	response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
    	out.write(html);
    	out.close();
	}


}
