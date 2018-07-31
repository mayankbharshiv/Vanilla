package com.vanilla.afour.afour.autolib.util;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;

import com.vanilla.afour.trialscope.pages.PharmaCMBasePage;

import java.io.File;

/*  TestNgListener will take & attach's the screenshot to TestNG report when Test case fails or skipped */
public class TestNgListener extends TestListenerAdapter {


    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String subPath = result.getEndMillis() +"___"+ result.getName()  + ".png";
            String fileScreenShotPath = System.getProperty("user.dir") + "\\Screenshots\\" + subPath;
            getScreenshot(fileScreenShotPath);
            String testNgReporterScreenshotPath = "<img src=\"file://" + fileScreenShotPath + "\" alt=\"\"/>";
            Reporter.log(testNgReporterScreenshotPath);

        } catch (Exception e) {
            System.out.println("Failed to add screenshot on failed test case");
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result)  {
        try {
            String subPath = result.getEndMillis() +"___"+ result.getName()  + ".png";
            String fileScreenShotPath = System.getProperty("user.dir") + "\\Screenshots\\" + subPath;
            getScreenshot(fileScreenShotPath);
            String testNgReporterScreenshotPath = "<img src=\"file://" + fileScreenShotPath + "\" alt=\"\"/>";
            Reporter.log(testNgReporterScreenshotPath);

        } catch (Exception e) {
            System.out.println("Failed to add screenshot on skipped test case");
            e.printStackTrace();
        }

    }

    private void getScreenshot(String screenShot) throws Exception {
        File scrFile = ((TakesScreenshot) PharmaCMBasePage.driver.getWebDriver()).getScreenshotAs(OutputType.FILE);
        //The below method will save the screen shot in d drive with name "screenshot.png"
        FileUtils.copyFile(scrFile, new File(screenShot));
    }


}





