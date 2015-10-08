/**
 * 
 */
package test;

import java.util.*;

import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.admin.AdminModel;
import org.topicquests.backside.servlet.apps.admin.api.IAdminModel;
import org.topicquests.common.api.IResult;

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
	public InviteTest() {
		try {
			environment = new ServletEnvironment(false);
			model = new AdminModel(environment);
			IResult r = model.addInvite(I1);
			if (r.hasError())
				System.out.println("A "+r.getErrorString());
			r = model.addInvite(I2);
			if (r.hasError())
				System.out.println("B "+r.getErrorString());
			r = model.addInvite(I3);
			if (r.hasError())
				System.out.println("C "+r.getErrorString());
			r = model.listInvites(0, -1);
			if (r.hasError())
				System.out.println("D "+r.getErrorString());
			System.out.println("E "+r.getResultObject());
			r = model.removeInvite(I2);
			if (r.hasError())
				System.out.println("F "+r.getErrorString());
			r = model.listInvites(0, -1);
			if (r.hasError())
				System.out.println("G "+r.getErrorString());
			System.out.println("H "+r.getResultObject());
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (environment != null)
				environment.shutDown();
			if (model != null)
				model.shutDown();
		}
	}

}
/**
 * First test failed:
 * E []
 * Latest test passed
 * E [sam@slow.com, sara@slow.com, bob@foo.org]
 * H [sam@slow.com, bob@foo.org]

 */
