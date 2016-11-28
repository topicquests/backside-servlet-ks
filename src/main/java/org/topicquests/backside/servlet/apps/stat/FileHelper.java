/**
 * 
 */
package org.topicquests.backside.servlet.apps.stat;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.ks.api.ITicket;

import net.minidev.json.JSONObject;
/**
 * @author jackpark
 *
 */
public class FileHelper {
	private ServletEnvironment environment;
	private String fileBasePath;
	

	/**
	 * 
	 */
	public FileHelper(ServletEnvironment env) {
		environment = env;
		fileBasePath = environment.getStringProperty("FileBasePath");
	}
	
	public void handleGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = getPath(request);
		System.out.println("STATICPATH "+path);
		if (isScript(path))
			this.sendScript(path, response);
		else if (isImage(path))
			this.sendImage(path, response);
		else { //TODO prove it's html
			this.sendHtml(path, response);
		}
				
	}
	
    protected boolean isScript(String path) {
        return (path.endsWith(".js") || path.endsWith(".css"));
    }
    protected boolean isImage(String path) {
        return (path.endsWith("gif") || path.endsWith("GIF") ||
                        path.endsWith("x-png") || path.endsWith("png") ||
                        path.endsWith("PNG") ||
                        path.endsWith(".jpg") || path.endsWith(".JPG") ||
                        path.endsWith("tiff") || path.endsWith("tif")  ||
                        path.endsWith("ico"));
    }
    protected void sendScript(String scriptPath, HttpServletResponse response)
    		throws IOException {
	  String path = fileBasePath+scriptPath;
	  File f = new File(path);
	  int size = (int) f.length();
	  FileInputStream in = new FileInputStream(f) ;
	  byte[] data = new byte[size] ;
	  in.read(data, 0, size);
	  String content = new String(data) ;
	  in.close() ;
	  if (content == null)
	    throw new IOException("Missing script for "+path);
	  PrintWriter pw = response.getWriter();
	  pw.write(content);
	  pw.flush();
	  pw.close();
	}
    protected void sendHtml(String filePath, HttpServletResponse response)
    		throws IOException {
	  String path = fileBasePath+filePath;
	  File f = new File(path);
	  int size = (int) f.length();
	  FileInputStream in = new FileInputStream(f) ;
	  byte[] data = new byte[size] ;
	  in.read(data, 0, size);
	  String content = new String(data) ;
	  in.close() ;
	  if (content == null)
	    throw new IOException("Missing file for "+path);
	  PrintWriter pw = response.getWriter();
	  pw.write(content);
	  pw.flush();
	  pw.close();
	}
    protected void sendImage(String imagePath, HttpServletResponse response) 
    		throws IOException {
	  String path = fileBasePath+imagePath;//normalizePathString(imagePath);
	  File f = new File(path);
	  if (f == null || !f.exists())
	    environment.logDebug("Missing image for "+path);
	  else if (f.exists()) {
		  String mimeType = "images/";
          
		  if (path.endsWith("gif") || path.endsWith("GIF"))
			  mimeType +="gif";
		  else if (path.endsWith("x-png") || path.endsWith("png") || path.endsWith("PNG"))
			  mimeType += "png";
		  else if (path.endsWith(".jpg") || path.endsWith(".JPG"))
			  mimeType += "jpg";
		  else if (path.endsWith("tiff") || path.endsWith("tif"))
			  mimeType += "tif";
		  else if (path.endsWith("ico"))
			  mimeType += "ico";
           response.setContentType(mimeType);
	    int len = 0;
	    byte[] buf = new byte[4096];
	   // log.debug("TopicSpacesBaseServlet sentImage 2 " + len);
	    InputStream input = null;
	    OutputStream output = null;
	    try {
		    input = new BufferedInputStream(new FileInputStream(f));
		    output = response.getOutputStream();
            while ((len = input.read(buf)) > -1) {
                output.write(buf, 0, len);
            }
	    } catch (IOException e) {
	    	environment.logError(e.getMessage(), e);
	    } finally {
            if (input != null) input.close();
            if (output != null) output.close();
	    }
	  }
	} 
    /**
     * <p>Paths that come in without a trailing '/' can be null</p>
     * @param request
     * @return
     * @throws ServletException
     */
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


}
