import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class NotepadTest {

    private static WindowsDriver notepadSession = null;
    public static String getDate(){
        LocalDate date = LocalDate.now();
        return date.toString();
    }
    @BeforeClass
    public static void setUp() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            capabilities.setCapability("platformName","Windows");
            capabilities.setCapability("deviceName", "WindowsPC");
            notepadSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            notepadSession.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);} catch (Exception e) {
            e.printStackTrace();
        }
    }
    @AfterMethod
    public void cleanApp(){
        notepadSession.quit();
        setUp();
    }
    @AfterSuite
    public void tearDown(){
        notepadSession.quit();
    }
    @Test
    public void checkAboutWindow() {
        notepadSession.findElementByName("Hilfe").click();
        notepadSession.findElementByName("Info").click();
        notepadSession.findElementByName("OK").click();
    }
    @Test
    public void sendTestText(){
        notepadSession.findElementByClassName("Edit").sendKeys(getDate());
        notepadSession.findElementByClassName("Edit").clear();
    }
    @Test()
    public void pressTimeAndDateButton(){
        notepadSession.findElementByName("Bearbeiten").click();
//        notepadSession.findElementByName("Uhrzeit/Datum").click();
        notepadSession.findElementByAccessibilityId("26").click();
        Assert.assertNotNull(notepadSession.findElementByClassName("Edit"));
        notepadSession.findElementByClassName("Edit").clear();
    }
}
