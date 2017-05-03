/**
 * 
 */
package test;
import org.topicquests.backside.servlet.ServletEnvironment;
import org.topicquests.backside.servlet.apps.tm.TopicMapModel;
import org.topicquests.backside.servlet.apps.tm.api.ITopicMapModel;
import org.topicquests.ks.TicketPojo;
import org.topicquests.ks.api.ITQCoreOntology;
import org.topicquests.ks.api.ITicket;
import org.topicquests.support.api.IResult;

/**
 * @author Admin
 *
 */
public class FullTextQueryTest {
	private static ServletEnvironment environment;
	private static ITopicMapModel model;
	private static ITicket credentials;

	private static String
		QUERY_STRING = "hot to trot"; // based on unit tests in the topic map
//	@BeforeClass
	public static void setUp() {
		try {
			environment = new ServletEnvironment(false);
			model = new TopicMapModel(environment);
			credentials = new TicketPojo(ITQCoreOntology.SYSTEM_USER);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
//	@AfterClass
	public static void tearDown() {
		environment.shutDown();
	}

//	@Ignore
//	@Test
	public void testSearch() {
		IResult r = model.listByFullTextQuery(QUERY_STRING, "en", 0, -1, null, null, credentials);
		System.out.println(r.getResultObject());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FullTextQueryTest test = new FullTextQueryTest();
		test.setUp();
		test.testSearch();
		test.tearDown();
		System.exit(0);
	}

}
