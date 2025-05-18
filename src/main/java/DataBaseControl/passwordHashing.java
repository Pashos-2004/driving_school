package DataBaseControl;

import org.mindrot.jbcrypt.BCrypt;

public class passwordHashing {

	public static String GetPasswordHash(String passwd) {
		return BCrypt.hashpw(passwd, BCrypt.gensalt(12));
	}
	
	public static boolean CheckPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
	
}
