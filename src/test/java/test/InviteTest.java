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

import java.util.*;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.admin.AdminModel;
import org.topicquests.backside.servlet.apps.admin.api.IAdminModel;
import org.topicquests.support.api.IResult;

/**
 * @author park
 *
 */
public class InviteTest {
	private ServletEnvironment environment;
	private IAdminModel model;
	private final String
		I1	= "sam@slow.com",
		I2	= "sara@slow.com",
		I3  = "bob@foo.org";

	/**
	 *
	 */
	public InviteTest(ServletEnvironment env, IAdminModel adminModel) {
		environment = env;
		model = adminModel;
		IResult r = model.addInvite(I1);
		if (r.hasError()) {
			System.out.println("A "+r.getErrorString());
			System.exit(1);
		}
		r = model.addInvite(I2);
		if (r.hasError()) {
			System.out.println("B "+r.getErrorString());
			System.exit(1);
		}
		r = model.addInvite(I3);
		if (r.hasError()) {
			System.out.println("C "+r.getErrorString());
			System.exit(1);
		}
		r = model.listInvites(0, -1);
		if (r.hasError()) {
			System.out.println("D "+r.getErrorString());
			System.exit(1);
		}
		System.out.println("E "+r.getResultObject());
		r = model.removeInvite(I2);
		if (r.hasError()) {
			System.out.println("F "+r.getErrorString());
			System.exit(1);
		}
		r = model.listInvites(0, -1);
		if (r.hasError()) {
			System.out.println("G "+r.getErrorString());
			System.exit(1);
		}
		System.out.println("H "+r.getResultObject());
	}

}
/**
 * First test failed:
 * E []
 * Latest test passed
 * E [sam@slow.com, sara@slow.com, bob@foo.org]
 * H [sam@slow.com, bob@foo.org]

 */
