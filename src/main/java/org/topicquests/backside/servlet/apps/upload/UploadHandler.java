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
package org.topicquests.backside.servlet.apps.upload;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.backside.servlet.apps.BaseHandler;
import org.topicquests.ks.api.ITicket;

import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;


/**
 * @author park
 * @see http://commons.apache.org/fileupload/using.html
 */
public class UploadHandler  extends BaseHandler {
	private final int MAX_SIZE = 32000; //TODO make this a config value

	/**
	 * 
	 */
	public UploadHandler(ServletEnvironment env, String basePath) {
		super(env,basePath);
	}

	//////////////////////////////////////////////
	// Path is going to include some verbs related to whether this is a
	//   Ace/OpenSherlock upload, a user upload, or a TMAServlet.Prospector
	//   upload. That verb must be set in a hidden div on the upload form
	//////////////////////////////////////////////
	/**
	 * Paint a file upload html page, which will return with a post command
	 * (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.BaseHandler#handleGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.topicquests.ks.api.ITicket, net.minidev.json.JSONObject)
	 */
	public void handleGet(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		String path = getPath(request);
		System.out.println("UPLOADGETPATH "+path);
		String verb = "uUpload";
		String cc = getCoreQuery(verb, credentials.getUserLocator(), "", (String)jsonObject.get(ICredentialsMicroformat.SESSION_TOKEN));
		String fp = FormPojo.getHtmlForm((String)jsonObject.get(ICredentialsMicroformat.VERB));
		//TODO
		super.sendHTML(fp, response);
	}
	
	/**
	 *  Accept the upload instructions
	 * (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.BaseHandler#handlePost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.topicquests.ks.api.ITicket, net.minidev.json.JSONObject)
	 */
	
	public void handlePost(HttpServletRequest request, HttpServletResponse response, ITicket credentials, JSONObject jsonObject) throws ServletException, IOException {
		System.out.println("UPLOADPOSTING-4");
	//	System.out.println("CONTENTLENGTH "+request.getHeader("Content-length"));
	//	System.out.println("CONTENTTYPE "+request.getContentType());
		String verb = request.getParameter("verb");     //getParameter("verb");
		//InputStream ins = request.getInputStream();
		//System.out.println("AVAILABLE "+ins.available());
		System.out.println("MULTI "+ ServletFileUpload.isMultipartContent(request));
		Map<String, String[]> m = request.getParameterMap();
		System.out.println("PARAMMAP "+m);
		String user = credentials.getUserLocator();
		System.out.println("FILEUPLOAD VERB "+verb+" "+user);
		environment.logDebug("UploadHandler.post "+verb+" "+user);
		String thePath = ""; //TODO calculate the directory path for this file
		/////////////////////////////
		//TODO: thePath will be dependent on a verb retrieved from request
		/////////////////////////////
		try {
			File repository = new File(thePath);
	    	String uploadPath = repository.getAbsolutePath()+"/";
	    	DiskFileItemFactory dfiFactory = dfiFactory = new DiskFileItemFactory();
	    	dfiFactory.setRepository(repository);
			ServletFileUpload servletFileUpload = new ServletFileUpload(dfiFactory);
			System.out.println("PARSING");
			List<FileItem> items = servletFileUpload.parseRequest(request);
			System.out.println("FILEUPLOADING "+verb+" | "+items.size());
 /*   		Iterator<FileItem> iter = items.iterator();
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
    		} */

		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			e.printStackTrace();
		}

		//Todo SOME KIND OF RETURN RESPONSE
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
}
