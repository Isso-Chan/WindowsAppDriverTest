import io.appium.java_client.windows.WindowsDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ThunderbirdTest {

    private static WindowsDriver thunderbirdSession = null;
    WebDriver driver;

    public static String getDate() {
        LocalDate date = LocalDate.now();
        return date.toString();
    }

    @BeforeClass
    public static void setUp() {
        try {
            //open cmd, first navigate to the folder, then run chrome.exe temporary on defined folder under D
            //Then chromeDriver will find opened Tab and work on it
            Runtime.getRuntime().exec("cmd /c start cmd.exe " +
                    "/K \"cd C:\\Program Files (x86)\\Google\\Chrome\\Application\" " +
                    "&& " +
                    "chrome.exe -remote-debugging-port=9222 --user-dta-dir=\"D:\\MyTest\\openedBrowser\"");

            Thread.sleep(2000);
            //open WindowsAppDriver on CMD
            Runtime.getRuntime().exec("cmd /c start cmd.exe " +
                    "/K \"cd C:\\Program Files (x86)\\Windows Application Driver " +
                    "&& " +
                    "WinAppDriver.exe");

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            //Open Thunderbird App to read mails
            DesiredCapabilities capabilities = new DesiredCapabilities();//
            capabilities.setCapability("app", "C:\\Program Files\\Mozilla Thunderbird\\thunderbird.exe");
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");
            thunderbirdSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            thunderbirdSession.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void cleanApp() {
        thunderbirdSession.quit();
//        setUp();
    }

    @AfterSuite
    public void tearDown() {
        thunderbirdSession.quit();
    }

    @Test
    public void checkDietersMail() {
        //First navigate to "C:\Program Files (x86)\Google\Chrome\Application" with CMD
        //Second: run -> chrome.exe -remote-debugging-port=9222 --user-dta-dir="D:\MyTest\openedBrowser"
        //Above 2 lines were done in SetUp(), apply them if you want to execute MANUALLY

        //chromeDriver.exe has to be supported by your chrome version on desktop!
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ioezcan\\OneDrive - " +
                "COMPUTACENTER\\Dokumente\\Projekte\\untitled1\\chromedriver_111.05.5563.64.exe");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "localhost:9222");
        driver = new ChromeDriver(options);
        Set<String> windowHandlesBefore = driver.getWindowHandles();

        thunderbirdSession.findElementByName("willgeld.dieter@oaman.de").click();
        thunderbirdSession.findElementByName("Read messages").click();
        thunderbirdSession.findElementByName("Sie wurden zum Fördervorhaben A - 204 § ab dem Jahr 2034 Zahlen in 86 " +
                "eingeladen").click();
//        notepadSession.findElementByName("=https://enroll.oaman-dev.computacenter.io/").click();
//        notepadSession.findElementByName("Link").click();// Name works with TagName() for Thunderbird
        thunderbirdSession.findElementByName("Link").click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Set<String> windowHandlesLater = driver.getWindowHandles();
        for (String window : windowHandlesLater) {
            if (!windowHandlesBefore.contains(window)) {
                driver.switchTo().window(window);
                String currentUrl = driver.getCurrentUrl();
                String expectedUrl = "https://enroll.oaman-dev.computacenter.io/login";
                driver.close();
                Assert.assertEquals(currentUrl, expectedUrl, " Url in new window is NOT verified!");
                break;
            }
        }


//    @Test
//    public void checkAboutWindow() {
//        notepadSession.findElementByName("Hilfe").click();
//        notepadSession.findElementByName("Info").click();
//        notepadSession.findElementByName("OK").click();
//    }
//    @Test
//    public void sendTestText(){
//        notepadSession.findElementByClassName("Edit").sendKeys(getDate());
//        notepadSession.findElementByClassName("Edit").clear();
//    }
//    @Test()
//    public void pressTimeAndDateButton(){
//        notepadSession.findElementByName("Bearbeiten").click();
////        notepadSession.findElementByName("Uhrzeit/Datum").click();
//        notepadSession.findElementByAccessibilityId("26").click();
//        Assert.assertNotNull(notepadSession.findElementByClassName("Edit"));
//        notepadSession.findElementByClassName("Edit").clear();
//    }
    }
}