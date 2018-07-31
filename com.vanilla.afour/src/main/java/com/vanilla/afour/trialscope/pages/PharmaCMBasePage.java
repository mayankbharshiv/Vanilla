/**

 */
package com.vanilla.afour.trialscope.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.vanilla.afour.afour.autolib.util.Log4J;
import com.vanilla.afour.afour.autolib.util.Reporting;
import com.vanilla.afour.afour.autolib.webActions.Driver;

/**
 * HomePage class contains static Driver reference and default object repository. The default object repository can be changed in the pages/classes extending HomePage class. Common methods can be
 * placed in this class.
 *
 * @author Afourtech
 */
public class PharmaCMBasePage {
    //	HashMap<String, HashMap<String, String>>	repository	= null;
    public static Driver driver;
    public static Reporting report = new Log4J();

    public PharmaCMBasePage() throws Exception {
        driver = new Driver();
        
        //this.repository =	new ObjectRepositoryLoader().getObjectRepository("web:Login_Page_Repo.xml");
    }


    public List getDropdown(HashMap<String, String> elementIdentifier) {
        List<String> dropValuesFromWeb = new ArrayList<String>();
        for (WebElement ele : driver.findAll(elementIdentifier)) {
            dropValuesFromWeb.add(ele.getText());
        }
        return dropValuesFromWeb;
    }

    public void switchNextWindow() {
        driver.SwichToNewWindow();
        String markCompleteWindow = driver.getWindowHandle();
        System.out.println("markCompleteWindow == " + markCompleteWindow);
    }

    // Getter for pageName
    public String getPageName() {
        String pageName = this.getClass().getSimpleName();
        return pageName;
    }
}
