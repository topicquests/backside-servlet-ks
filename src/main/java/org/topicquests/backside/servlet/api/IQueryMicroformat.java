/**
 * 
 */
package org.topicquests.backside.servlet.api;

/**
 * @author Admin
 * <p>Query fields for sorting, paging, etc</p>
 */
public interface IQueryMicroformat {
	
	public static final String 
		SORT_START	= "start",
		SORT_COUNT	= "count",
		SORT_BY		= "sortBy",
		SORT_DIR	= "sortDir",
		SORT_DATE	= "crDt", 	//sortBy: "crDt"
		SORT_LABEL	= "label",	//sortBy: "label" --alphabetical sort
		SORT_VAL	= "val",	//sortBy: "val" -- a value number
		SORT_CREATOR= "crtr",	//sortBy: "crtr"
		ASC_DIR		= "asc",	//sortDir: "asc"
		DSC_DIR		= "desc";	//sortDir: "dsc"
		
		

}
