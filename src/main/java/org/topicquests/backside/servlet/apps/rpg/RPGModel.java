/**
 * 
 */
package org.topicquests.backside.servlet.apps.rpg;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.rpg.api.IRPGModel;
import org.topicquests.backside.servlet.apps.tm.api.ISocialBookmarkLegend;
import org.topicquests.ks.tm.api.INodeTypes;

/**
 * @author jackpark
 *
 */
public class RPGModel implements IRPGModel {
	private ServletEnvironment environment;

	/**
	 * 
	 */
	public RPGModel(ServletEnvironment env) {
		environment = env;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.servlet.apps.rpg.api.IRPGModel#shutDown()
	 */
	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

}
