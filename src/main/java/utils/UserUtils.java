package utils;

import com.qa.xstream.utils.UserProfile;

import java.util.Properties;

public class UserUtils {

    public static String getPhone(com.qa.xstream.utils.UserProfile user, Properties prop) {
        return prop.getProperty(user.getKey() + ".phone");
    }

    public static String getOtp(com.qa.xstream.utils.UserProfile user, Properties prop) {
        return prop.getProperty(user.getKey() + ".otp");
    }

    public static String getAuthFile(UserProfile user, Properties prop) {
        return "auth/" + prop.getProperty(user.getKey() + ".authfile");
    }
}

