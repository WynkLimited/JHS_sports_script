package utils;

import com.microsoft.playwright.Page;
import com.qa.xstream.utils.GenericFunctions;

public class LoginPage {
    private static Page page;
    private String loginModal_WE = "//div[contains(@class,'login-number')]";
    private String loginModalXstreamLogo_WE="//div[@class='modal-small__logo']";
    private String loginModalClose_Btn= "//button[@class='close']";
    private String loginModalHeading_WE="//h2[@id='login-heading']";
    private String loginModalSubHeading_WE="//h4[@id='login-sub-heading']";
    private String loginModalCountryCode_WE = "//input[@value='+91']";
    private String loginModalInputPhoneNumber_WE = "//input[@id='mobile-number']";
    private String loginModalContinue_Btn= "//div[@class='form-button']";
    private String loginModalTerms_Lnk="//a[contains(@title,'Terms')]";
    private String loginModalPrivacy_Lnk="//a[contains(@title,'Privacy')]";
    private static String mobileField_WE="//input[@id='mobile-number']";
    private static String continue_Btn="//button[@id='mobile-number-verify-btn']";
    private static String otpFirst_WE="//input[@id='otp-first-letter']";
    private static String otpSecond_WE="//input[@id='otp-second-letter']";
    private static String otpThird_WE="//input[@id='otp-third-letter']";
    private static String otpFourth_WE="//input[@id='otp-fourth-letter']";
    private static String verify_Btn ="//button[@id='otp-verify-btn']";
    private static String profileText_WE="//div[@class='profile-text' and text()='Profile']";
    private String homeTab_Btn="//a[@aria-label='Home']";
    private static String login_Btn="//div[@class='profile-text']";
    private String firstRailFirst_WE= "//*[@data-id='rail-id-5']//section[@id='atm_potrait-tile-1']";
    private String cdpLogin_Btn="//button[contains(@id,'play')]";

//    ProfilePage profilePage;
 GenericFunctions generic;

    public LoginPage(Page page) {
        this.page = page;
        generic = new GenericFunctions();
//        profilePage=new ProfilePage(page);
    }

    /**
     * This method checks the visibility of login modal
     * @return true if visible
     */
    public boolean verifyVisibilityOfLoginModal(){ return generic.isVisible(page,"Xpath", loginModal_WE); }

    /**
     * This method checks the visibility of the login modal Xstream logo.
     * @return true if the visible
     */
    public boolean verifyLoginModalXstreamLogo(){ return generic.isVisible(page,"Xpath",loginModalXstreamLogo_WE);}

    /**
     * This method checks the visibility of the login modal close button.
     * @return true if button is visible
     */
    public boolean verifyLoginModalCloseButton(){return generic.isVisible(page,"Xpath",loginModalClose_Btn);}

    /**
     * This method retrieves the heading text from the login modal.
     * @return the heading text of the login modal.
     */
    public String getLoginModalHeading(){return generic.getTextFromLocator(page,"Xpath",loginModalHeading_WE);}

    /**
     * This method retrieves the subheading text from the login modal.
     * @return the subheading text of the login modal.
     */
    public String getLoginModalSubHeading(){return generic.getTextFromLocator(page,"Xpath",loginModalSubHeading_WE);}

    /**
     * This method checks the visibility of the country code in the login modal.
     * @return true if is visible
     */
    public boolean verifyLoginModalCountryCode(){ return generic.isVisible(page,"Xpath",loginModalCountryCode_WE);}

    /**
     * This method checks the visibility of the phone number in the login modal.
     * @return true if visible
     */
    public boolean verifyLoginModalInputPhoneNumber(){ return generic.isVisible(page,"Xpath",loginModalInputPhoneNumber_WE);}

    /**
     * This method checks the visibility of the continue button on the login modal.
     * @return true if button is visible
     */
    public boolean verifyLoginModalContinueButton(){ return generic.isVisible(page,"Xpath",loginModalContinue_Btn);}

    /**
     * This method checks the redirection behavior of the Terms of Use link in the login modal.
     * @return true if it redirects correctly to the expected URL.
     */
    public boolean verifyTermsOfUsesLinkRedirection(){
        String actual=generic.getAttribute(page,"href",loginModalTerms_Lnk);
        generic.click(page,"Xpath",loginModalTerms_Lnk);
        String expected=generic.getUrl(page);
        return expected.contains(actual);
    }

    /**
     * This method checks the redirection behavior of the Privacy Policy link in the login modal.
     * @return true if it redirects correctly to the expected URL.
     */
    public boolean verifyPrivacyPolicyLinkRedirection(){
        String actual=generic.getAttribute(page,"href",loginModalPrivacy_Lnk);
        generic.click(page,"Xpath",loginModalPrivacy_Lnk);
        String expected=generic.getUrl(page);
        return expected.contains(actual);
    }

    /**
     * This method verifies the login button on CDP and clicks on it
     * @return true if visible
     */
    public boolean loginToWatch()
    {
        generic.goToTab(page,"xpath",homeTab_Btn);
        generic.click(page,"xpath",firstRailFirst_WE);
        generic.waitForVisibility(page, cdpLogin_Btn, 1000);
        boolean check = generic.isVisible(page,"xpath",cdpLogin_Btn);
        generic.click(page, "Xpath", cdpLogin_Btn);
        boolean isLoginModalVisible = verifyVisibilityOfLoginModal();
        return check && isLoginModalVisible;
    }

    /**
     * This method enters a mobile number into the login modal and clicks the continue button.
     * @param number the mobile number to be entered.
     */
    public void enterMobileNumber(String number) {
        try {
            generic.click(page,"Xpath",login_Btn);
            generic.fill(page, "Xpath", mobileField_WE, number);
            generic.click(page,"xpath",continue_Btn);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    /**
     * This method enters an OTP into the login modal and clicks the verify button.
     * @param otp the OTP to be entered (4 digits).
     */
    public void enterOtp(String otp) {
        generic.fill(page, "Xpath", otpFirst_WE, String.valueOf(otp.charAt(0)));
        generic.fill(page, "Xpath", otpSecond_WE, String.valueOf(otp.charAt(1)));
        generic.fill(page, "Xpath", otpThird_WE, String.valueOf(otp.charAt(2)));
        generic.fill(page, "Xpath", otpFourth_WE, String.valueOf(otp.charAt(3)));
        generic.click(page,"xpath", verify_Btn);
    }

    /**
     * This method checks if the login is successful by verifying the visibility of the profile text.
     * @return true if login is successful, false otherwise.
     */
    public boolean isLoginSuccessful(String number, String otp) {
//        if(generic.isVisible(page, "xpath", login_Btn)) generic.click(page, "xpath",login_Btn);
        enterMobileNumber(number);
        enterOtp(otp);
        generic.waitForVisibility(page, profileText_WE, 5000);
        return generic.isVisible(page,"xpath", profileText_WE);
    }

//    public ProfilePage clickOnProfile()
//    {
//        generic.click(page,"xpath",profileText);
//        return profilePage;
//    }
//

}