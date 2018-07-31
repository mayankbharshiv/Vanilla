package com.vanilla.trialscopeScripts;

import java.util.HashMap;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.vanilla.MasterBase;
import com.vanilla.afour.afour.autolib.util.LogLevel;
import com.vanilla.afour.trialscope.pages.LoginPage;
import com.vanilla.afour.trialscope.pages.PharmaCMBasePage;

/**
 * Login to PharmaCM
 * @author mayank.b
 */
public class LoginToPharmaCM extends MasterBase
{
    public HashMap<String, String> config = null;

    public LoginToPharmaCM() throws Exception
    {
        super();
        config = PharmaCMBasePage.driver.getConfigData();
    }

    @Test(description = "Login To PharmaCM")
    public void loginToPharmaCM() throws Exception
    {
        try
        {
            login();

        }
        catch(Throwable e)
        {
            report.log("Failed in Test case", LogLevel.INFO);
            Assert.fail(e.getMessage());
        }

    }

    /**
     * Method to Login to PharmaCM
     */
    public void login() throws Exception
    {
        try
        {
            LoginPage login = new LoginPage();
            login.login();
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            throw new Exception("Failed to log in" + e.getMessage());
        }
    }
}
