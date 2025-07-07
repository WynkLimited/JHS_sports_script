package utils;

import com.microsoft.playwright.Page;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

import static com.qa.xstream.factory.PlaywrightFactory.getPage;

public class CommonUtils {
    private static Properties prop;

    /**
     * this method is used to initialize the properties from config file
     */
    public static Properties init_prop() {

        try {
            FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties");
            prop = new Properties();
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;

    }

    /**
     * This function is used to take screenshot
     * @return screenshot
     */
    public static String takeScreenshot() {
        String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
        byte[] buffer = getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(false));
        String base64Path = Base64.getEncoder().encodeToString(buffer);

        return base64Path;
    }


}
