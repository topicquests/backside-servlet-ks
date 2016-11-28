/**
 * 
 */
package org.topicquests.backside.servlet.apps.upload;

/**
 * @author jackpark
 *
 */
public class FormPojo {
	
	public static String getHtmlForm(String trailer) {
		StringBuilder buf = new StringBuilder("<html>");
		buf.append("<head></head><body>");
		buf.append("<form enctype='multipart/form-data' method='post' ");
		buf.append("action='http://localhost:8080/upload/");
		buf.append(trailer+"'>");
		buf.append("<input type='file' name='uploader'/>");
		buf.append("<br/><br/>");
		buf.append("<input type='submit' value='Upload File'/>");
		buf.append("</form>");
		buf.append("</body></html>");
		return buf.toString();
	}

}
