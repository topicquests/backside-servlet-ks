/*
 * Copyright 2013, TopicQuests
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
package test;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.admin.AdminModel;
import org.topicquests.backside.servlet.apps.admin.api.IAdminModel;

import org.topicquests.util.LoggingPlatform;

/**
 * @author park
 *
 */
public class TestHarness1 {
	private static ServletEnvironment environment;
	private static IAdminModel adminModel;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("BOO!");
		LoggingPlatform logger = LoggingPlatform.getInstance("logger.properties");
		System.out.println("TestHarness Starting");
		try {
			environment = new ServletEnvironment(false);
			adminModel = new AdminModel(environment);
		} catch (Exception e) {
			e.printStackTrace();
			environment.shutDown();
			System.exit(1);
		}
		new InviteTest(environment, adminModel);
		new UserTest(environment);
		new DecodeTest();
		new ConversationTest1(environment);
		new FullTextSearchTest1(environment);
		new FullTextSearchTest2(environment);
		System.out.println("TestHarness Did");
		environment.shutDown();
		System.exit(0);
	}

}
// {"crDt":"2015-10-05T12:49:32-07:00","trCl":["TypeType","ClassType","NodeType","ConversationMapNodeType"],"crtr":"joe","lox":"MyFirstMap","sIco":"\/images\/ibis\/map_sm.png","isPrv":false,"_ver":"1444074572900","lEdDt":"2015-10-05T12:49:32-07:00","label":["My first map"],"lIco":"\/images\/ibis\/map.png","inOf":"ConversationMapNodeType"}
// {"crDt":"2015-10-05T12:49:32-07:00","trCl":["TypeType","ClassType","NodeType","IssueNodeType"],"crtr":"joe","lox":"MyFirstQuestion","sIco":"\/images\/ibis\/issue_sm.png","isPrv":false,"_ver":"1444074572986","lEdDt":"2015-10-05T12:49:32-07:00","label":["Why is the sky blue?"],"lIco":"\/images\/ibis\/issue.png","inOf":"IssueNodeType","pNL":["{\"contextLocator\":\"MyFirstMap\",\"smallImagePath\":\"\\\/images\\\/ibis\\\/map_sm.png\",\"subject\":\"My first map\",\"locator\":\"MyFirstMap\"}"]}
// {"crDt":"2015-10-05T12:49:33-07:00","trCl":["TypeType","ClassType","NodeType","PositionNodeType"],"crtr":"joe","lox":"MyFirstAnswer","sIco":"\/images\/ibis\/position_sm.png","isPrv":false,"_ver":"1444074573005","lEdDt":"2015-10-05T12:49:33-07:00","label":["Nobody really knows."],"lIco":"\/images\/ibis\/position.png","inOf":"PositionNodeType","pNL":["{\"contextLocator\":\"MyFirstMap\",\"smallImagePath\":\"\\\/images\\\/ibis\\\/issue_sm.png\",\"subject\":\"Why is the sky blue?\",\"locator\":\"MyFirstQuestion\"}"]}

