package com.qa.xstream.tests;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.qa.xstream.base.BaseTest;
import com.qa.xstream.utils.CommonUtils;
import com.qa.xstream.utils.GenericFunctions;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.SlackNotifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.qa.xstream.factory.PlaywrightFactory.getBrowser;
import static com.qa.xstream.factory.PlaywrightFactory.getPage;

public class JhsSportScript extends BaseTest {

    SoftAssert softAssert;
    GenericFunctions generic;
    SlackNotifier notifier;

    private String tiles = "//li[contains(@class,'custom-portrait-relative-holder')]//a";
    private String tilesName = "//li[contains(@class,'custom-portrait-relative-holder')]//section";
    private String singleTile = "//section[@id='atm_potrait-tile-%d']";
    private String watchNowBtn = "button#play-content-button";
    private String railName = "//section[@name='Jiohotstar Live Sports']";
    private String tileImage = "//li[contains(@class,'custom-portrait-relative-holder')]//a//img[contains(@class,'custom-tile-image ')]";

    @BeforeMethod
    public void beforeMethod() {
        softAssert = new SoftAssert();
        generic = new GenericFunctions();
        notifier  = new SlackNotifier();
    }

    @Test(dataProvider = "subscribedUser", priority = 1, description = "Verify that the user can add the content(Movie) in Continue Watching if played for more than 1 min and Verify user can resume content from CW rail and Verify user can remove content from CW rail and Verify latest added content appears first in the CW rail")
    public void check(String userType) throws InterruptedException, IOException {

        List<String> expiredUrls = new ArrayList<>();
        List<String> tileUrls = new ArrayList<>();
        String testContentNames = "";
        String expiredUrl = "";

        getPage().navigate("https://www.airtelxstream.in/jiohotstar-live-sports/list/axaut_vvic37081726728184444?pa=LANDSCAPE_169");
        getPage().mouse().move(0, 0);
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
        slowScrollToBottom(getPage());

        if(!generic.isVisible(getPage(), "xpath", railName)){
            notifier.sendSlackMessage("No JHS Live Sports Content Present");
            return;
        }

        Locator tilesLocator = generic.locateByXpathOrCss(getPage(), tiles);
        Locator tilesNameLocator = generic.locateByXpathOrCss(getPage(), tilesName);

        int count = tilesLocator.count();
        System.out.println(count);

        for (int i = 0; i < count; i++) {
            String href = tilesLocator.nth(i).getAttribute("href");
            if (href != null && !href.isEmpty()) {
                tileUrls.add("https://www.airtelxstream.in" + href);
            }

            String matchName = tilesNameLocator.nth(i).getAttribute("atm-name").toLowerCase();
            if(matchName.contains("test")){
                testContentNames += tileUrls.get(i)+"\n";
            }
        }


        for(int i =0; i<tileUrls.size(); i++){
            getPage().navigate(tileUrls.get(i));
            getPage().waitForLoadState(LoadState.NETWORKIDLE);

            String previousUrl = getPage().url();
            generic.click(getPage(), "css selector", watchNowBtn);
            Thread.sleep(5000);
            getPage().waitForURL(url -> !url.equals(previousUrl),
                    new Page.WaitForURLOptions().setTimeout(15000));
            getPage().waitForLoadState(LoadState.LOAD);

            String currentUrl = getPage().url();
            System.out.println(currentUrl +"\n"+ previousUrl +" " + getPage().url().contains("https://www.hotstar.com/in/home"));

            if (currentUrl.contains("https://www.hotstar.com/in/home")) {
                expiredUrls.add(tileUrls.get(i));
                expiredUrl +=  tileUrls.get(i)+"\n";
            }
//            getPage().navigate("https://www.airtelxstream.in/jiohotstar-live-sports/list/axaut_vvic37081726728184444?pa=LANDSCAPE_169");
//            getPage().waitForLoadState(LoadState.NETWORKIDLE);
        }

        if (!expiredUrls.isEmpty()) {
            File excel = writeUrlsToExcel(expiredUrls, "expired-sports-urls.xlsx");
            notifier.sendSlackMessage("Expired List of JHS live sports content\n" + expiredUrl);
//            notifier.sendSlackMessage(excel.getAbsolutePath());
//            System.out.println("Expired List of JHS live sports content\n" + expiredUrl);
        }else{
            notifier.sendSlackMessage("No expired JHS live content present");
        }

        if(!testContentNames.isEmpty()){
            notifier.sendSlackMessage("JHS Sports Content that has test keyword\n" + testContentNames);
//            System.out.println("JHS Sports Content that has test keyword)\n" + testContentNames);
        }

    }

    public void slowScrollToBottom(Page page) {
        page.evaluate("""
        () => {
            return new Promise(resolve => {
                let totalHeight = 0;
                const distance = 100;
                const interval = setInterval(() => {
                    window.scrollBy(0, distance);
                    totalHeight += distance;

                    if (totalHeight >= document.body.scrollHeight) {
                        clearInterval(interval);
                        resolve();
                    }
                }, 200); // scroll every 200ms
            });
        }
    """);
    }

    public File writeUrlsToExcel(List<String> urls, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expired URLs");

        for (int i = 0; i < urls.size(); i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(urls.get(i));
        }

        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
        return file;
    }
}
