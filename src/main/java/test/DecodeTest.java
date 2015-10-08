/**
 * 
 */
package test;

import com.google.common.io.BaseEncoding;

/**
 * @author park
 *
 */
public class DecodeTest {

	/**
	 * 
	 */
	public DecodeTest() {
		String pwd = "Change#@$%*Me!";
		String bar = BaseEncoding.base64().encode(pwd.getBytes());
		byte [] foo = BaseEncoding.base64().decode(bar);
		String creds = new String(foo);
		System.out.println(creds);
	}

}
