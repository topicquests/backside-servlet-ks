/**
 * 
 */
package org.topicquests.backside.servlet.ddp.api;

/**
 * @author park
 * @see https://github.com/meteor/meteor/blog/devel/packages/ddp/DDP.md
 */
public interface IDDPMicroformats {
	
	/** fields */
	public static final String
		DDP_MESSAGE 			= "msg",
		DDP_ID					= "id",
		DDP_NAME				= "name",
		DDP_PARAMS				= "params",
		DDP_METHOD				= "method", //also a verb
		DDP_METHODS				= "methods",
		DDP_COLLECTION			= "collection",
		DDP_SESSION				= "session",
		DDP_VERSION				= "version",
		DDP_SUPPORT				= "support",
		DDP_FIELDS				= "fields",
		DDP_BEFORE				= "before",
		DDP_RESULT				= "result",//also a verb
		DDP_ERROR				= "error",
		DDP_REASON				= "reason",
		DDP_OFFENDING_MESSAGE	= "offendingMessage",
		DDP_DETAILS				= "details",
		DDP_RANDOM_SEED			= "randomSeed";
	
	/** verbs (message values) */
	public static final String
		DDP_CONNECT				= "connect",
		DDP_PING				= "ping",
		DDP_PONG				= "pong",
		DDP_SUB					= "sub",
		DDP_UNSUB				= "unsub";
		
	/** mostly server to client */
	public static final String
		DDP_CONNECTED			= "connected",
		DDP_NOSUB				= "nosub",
		DDP_ADDED				= "added",
		DDP_CHANGED				= "changed",
		DDP_REMOVED				= "removed",
		DDP_UPDATED				= "updated",
		DDP_READY				= "ready",
		DDP_ADDED_BEFORE		= "addedBefore",
		DPD_MOVED_BEFORE		= "movedBefore";
	
	/** EJSON stuff */
	public static final String
		DDP_DATE				= "$date", //milliseconds since epoch
		DDP_BINARY				= "$binary", //Base 64 string
		DDP_ESCAPE				= "$escape", //
		DDP_TYPE				= "$type"; //
}