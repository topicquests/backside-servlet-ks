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
package test;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.api.ISecurity;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.common.api.IResult;

/**
 * @author park
 *
 */
public class UserTest {
	private ServletEnvironment environment;
	private IUserModel model;
	private final String
		I1	= "sam@slow.com",
		I2	= "sara@slow.com",
		I3  = "bob@foo.org",
		N1  = "sam",
		P1  = "sam!",
		N2  = "sara",
		P2	= "sara!",
		N3  = "bob",
		P3  = "bob#";
	

	/**
	 * 
	 */
	public UserTest() {
		try {
			environment = new ServletEnvironment(false);
			model = environment.getUserModel();
			
			//(String email, String userName, String password, String avatar, String role, String homepage, String geolocation)
			IResult r = model.insertUser(I1, N1, P1, "Sam Slow", "", ISecurity.USER_ROLE, "", "", true);
			if (r.hasError())
				System.out.println("A "+r.getErrorString());
			r =  model.insertUser(I2, N2, P2, "Sara Slow", "", ISecurity.ADMINISTRATOR_ROLE, "", "", true);
			if (r.hasError())
				System.out.println("B "+r.getErrorString());
			r =  model.insertUser(I3, N3, P3, "Bob Foo", "bobobo", ISecurity.USER_ROLE, "http://google.com/", "101,125", true);
			if (r.hasError())
				System.out.println("C "+r.getErrorString());
			r = model.listUserLocators();
			if (r.hasError())
				System.out.println("D "+r.getErrorString());
			System.out.println("E "+r.getResultObject());
			r = model.authenticate(I1, P1);
			if (r.hasError())
				System.out.println("F "+r.getErrorString());
			System.out.println("G "+r.getErrorString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (environment != null)
				environment.shutDown();
		}
	}

}
/**
 *First test
 * E [sam, sara, bob]
 */