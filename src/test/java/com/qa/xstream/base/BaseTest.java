package com.qa.xstream.base;

import com.microsoft.playwright.Page;
import com.qa.xstream.factory.PlaywrightFactory;
import com.qa.xstream.utils.AuthFileGenerator;
import com.qa.xstream.utils.CommonUtils;
import com.qa.xstream.utils.UserProfile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import utils.LoginPage;

import java.lang.reflect.Method;
import java.util.Properties;

import static com.qa.xstream.factory.PlaywrightFactory.getPage;

public class BaseTest {
    protected static PlaywrightFactory pf;
    protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    protected static Properties prop;
    private LoginPage loginPage;

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("====== Starting Test Suite ======");
        prop = CommonUtils.init_prop();
//        AuthFileGenerator.createStorageStateForUser(UserProfile.SUBSCRIBED_USER1, prop);
//        AuthFileGenerator.createStorageStateForUser(UserProfile.NONAIRTEL_USER, prop);
        pf = new PlaywrightFactory();

    }

    @BeforeMethod
    public void setup(Method method, Object[] testData) {
        String userType = "GUEST_USER"; // default

        if (testData != null && testData.length > 0 && testData[0] instanceof String) {
            userType = (String) testData[0];
        }

        tlPage.set(pf.initBrowser(prop));
        loginPage = new LoginPage(getPage());
        login();

    }

    @AfterMethod
    public void tearDown() {
        System.out.println("====== Ending Test Suite ======");
        if (PlaywrightFactory.getBrowser() != null) {
            PlaywrightFactory.getBrowser().close();
        }
    }

    public void login() {

        String number= prop.getProperty("subscribed_user1.phone");
        String otp=prop.getProperty("subscribed_user1.otp");

        Assert.assertTrue(loginPage.isLoginSuccessful(number,otp),"Login unsuccessful");
    }


    @DataProvider(name = "subscribedUser")
    public Object[][] subscribedUser() {
        return new Object[][] {
                { "SUBSCRIBED_USER1" }
        };
    }

}
