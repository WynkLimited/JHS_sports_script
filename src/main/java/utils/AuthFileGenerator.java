package utils;

import com.microsoft.playwright.*;
import com.qa.xstream.utils.GenericFunctions;
import com.qa.xstream.utils.UserProfile;
import com.qa.xstream.utils.UserUtils;

import java.nio.file.Paths;
import java.util.Properties;

public class AuthFileGenerator {

    public static void createStorageStateForUser(UserProfile user, Properties prop) {
        String authFilePath = UserUtils.getAuthFile(user, prop);

        com.qa.xstream.utils.GenericFunctions generic = new GenericFunctions();
        String login_Btn="//div[@class='profile-text']";
        String mobileField_WE="//input[@id='mobile-number']";
        String continue_Btn="//button[@id='mobile-number-verify-btn']";
        String otpFirst_WE="//input[@id='otp-first-letter']";
        String otpSecond_WE="//input[@id='otp-second-letter']";
        String otpThird_WE="//input[@id='otp-third-letter']";
        String otpFourth_WE="//input[@id='otp-fourth-letter']";
        String verify_Btn="//button[@id='otp-verify-btn']";
        String profileText_WE="//div[@class='profile-text' and text()='Profile']";

//        if (!Paths.get(authFilePath).toFile().exists()) {
            System.out.println("Creating auth storage file for: " + user.getKey());

            try (Playwright playwright = Playwright.create()) {
                Browser browser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions().setHeadless(false));
                BrowserContext context = browser.newContext();

                Page page = context.newPage();

                page.navigate(prop.getProperty("XstreamURL"));
                generic.waitForVisibility(page, login_Btn, 1000);

                // Perform login for user using phone + OTP
                generic.click(page, "Xpath", login_Btn);
                generic.fill(page, "Xpath", mobileField_WE, UserUtils.getPhone(user, prop));
                generic.click(page, "Xpath", continue_Btn);
                generic.fill(page, "Xpath",otpFirst_WE, "1");
                generic.fill(page, "Xpath",otpSecond_WE, "1");
                generic.fill(page, "Xpath",otpThird_WE, "1");
                generic.fill(page, "Xpath",otpFourth_WE, "1");
                generic.click(page, "Xpath",verify_Btn);

                generic.isVisible(page,"xpath",profileText_WE);
                generic.click(page, "Xpath",profileText_WE);

                //save auth state
                context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(authFilePath)));

                context.close();
                browser.close();
            }
//        } else {
//            System.out.println("Auth file already exists for user: " + user.getKey());
//        }
    }
}
