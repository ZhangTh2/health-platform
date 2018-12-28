package edu.zju.ccnt.user.util;

import edu.zju.ccnt.user.model.User;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordUtil {
	//private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private String algorithmName = "md5";
	private int hashIterations = 2;

	public void encryptPassword(User user) {
		//String salt=randomNumberGenerator.nextBytes().toHex();
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),  ByteSource.Util.bytes(user.getUsername()), hashIterations).toHex();
		//String newPassword = new SimpleHash(algorithmName, user.getPassword()).toHex();
		user.setPassword(newPassword);

	}

	public static void main(String[] args) {
		PasswordUtil p = new PasswordUtil();
		User user = new User();
		user.setUsername("666");
		user.setPassword("123");
		p.encryptPassword(user);
		System.out.println(user.getPassword());
	}
}
