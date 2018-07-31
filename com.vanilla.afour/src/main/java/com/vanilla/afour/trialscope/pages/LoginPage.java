package com.vanilla.afour.trialscope.pages;

import java.util.HashMap;

import com.vanilla.afour.afour.autolib.util.ObjectRepositoryLoader;

/**
 * Methods used for Logging into PharmaCM Application
 * @author Afourtech
 */
public class LoginPage extends PharmaCMBasePage
{
    HashMap<String, HashMap<String, String>> loginPageElement = null;

    public HashMap<String, String> config = null;

    public LoginPage() throws Exception
    {
        config = driver.getConfigData();
        this.loginPageElement = new ObjectRepositoryLoader().getObjectRepository("web:LoginPage.xml");
    }

    public boolean login() throws Exception
    {
        boolean islogin = false;
        try
        {
            driver.waitForElementToAppear(loginPageElement.get("UserName"), 10);
            driver.setText(loginPageElement.get("UserName"), config.get("userid"));
            driver.setText(loginPageElement.get("PassWord"), config.get("password"));
            driver.click(loginPageElement.get("LoginButton"));
            islogin = true;
        }
        catch(Exception e)
        {
            throw new Exception("Failed to login as"+"\n" + e.getMessage());
        }
        islogin = true;
        return islogin;
    }
}
