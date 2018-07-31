package com.vanilla;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import com.vanilla.afour.afour.autolib.util.BrowserType;
import com.vanilla.afour.afour.autolib.util.Log4J;
import com.vanilla.afour.afour.autolib.util.Reporting;
import com.vanilla.afour.afour.autolib.util.TestDataHandler;
import com.vanilla.afour.trialscope.pages.PharmaCMBasePage;

public class MasterBase
{

    public static String testName = null;
    private String testCaseName = null;
    public HashMap<String, HashMap<String, String>> data = null;
    protected TestDataHandler testDataHandler = null;
    PharmaCMBasePage pharmacmBasePage = null;
    String currentDir = System.getProperty("user.dir");
    public static Reporting report = new Log4J();

    @Parameters({"myBrowser"})
    @BeforeTest(alwaysRun = true)
    public void open(BrowserType myBrowser) throws Exception
    {
        PharmaCMBasePage.driver.openBrowser(myBrowser, PharmaCMBasePage.driver.config.get("url"));
    }

    public MasterBase() throws Exception
    {
        testDataHandler = new TestDataHandler();
        pharmacmBasePage = new PharmaCMBasePage();
    }

    @AfterTest(alwaysRun = true)
    @Parameters({"myBrowser"})
    public void close(BrowserType myBrowser) throws Exception
    {
        try
        {
            PharmaCMBasePage.driver.closeBrowser();
        }
        finally
        {
            PharmaCMBasePage.driver.closeBrowser();
        }
    }

    /**
     * Getter for test caseName
     * @return testCaseName
     */
    public String getTestCaseName()
    {
        testCaseName = this.getClass().getSimpleName();
        return testCaseName;
    }
    

    /**
     * Function returns next day date in m/dd/yyyy format (Considering current
     * date in "America/Los_Angeles" time zone )
     *
     * @return : Next day date in m/dd/yyyy format
     * @throws Exception
     */
    public String getNextDayDate() throws Exception
    {
        Date d1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String currentDate = sdf.format(d1);
        final Date date = sdf.parse(currentDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * Function returns any day date in m/dd/yyyy format (Considering current
     * date in "America/Los_Angeles" time zone ) Remember to pass paremeter with
     * - sign for old date ,0 for todays date and ,+ sign for future date
     *
     * @return : Next day date in m/dd/yyyy format
     * @throws Exception
     */
    public String getAnyDate(int dayCount) throws Exception
    {
        Date d1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String currentDate = sdf.format(d1);
        final Date date = sdf.parse(currentDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, dayCount);
        return sdf.format(calendar.getTime());

    }

    /**
     * Function returns any day date in m/dd/yyyy format (Considering current
     * date in "America/Los_Angeles" time zone ) Remember to pass paremeter with
     * - sign for old date ,0 for todays date and ,+ sign for future date
     *
     * @return : Next day date in m/dd/yyyy format
     * @throws Exception
     */
    public String getAnyTime(int timeInMinutes) throws Exception
    {
        Date d1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss'Z' a");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String currentDate = sdf.format(d1);
        final Date date = sdf.parse(currentDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, timeInMinutes);
        String t = sdf.format(calendar.getTime());
        String tArray[] = t.split(":");
        String h = tArray[0];
        String m = tArray[1];
        String am_pm[] = tArray[2].split(" ");
        String am_pm1 = am_pm[1];
        t = h + ":" + m + " " + am_pm1;
        return t;
    }
}
