package com.vanilla.trialscopeScripts;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.vanilla.MasterBase;
import com.vanilla.afour.afour.autolib.util.LogLevel;
import com.vanilla.afour.trialscope.pages.HomePage;

/**
 * Search for study using the search box in the upper right hand corner
 * 
 * @author Mayank B
 */
public class TC004_23375_SearchForStudyUsingTheSearchBoxInTheUpperRightHandCorner extends MasterBase
{

    private static HashMap<String, String> data = null;
    private static HomePage homePage = null;
    private static final String TEXTBOXNAME = "SearchBox";

    public TC004_23375_SearchForStudyUsingTheSearchBoxInTheUpperRightHandCorner() throws Exception
    {
        super();
        homePage = new HomePage();
        data = testDataHandler.readTestData(super.getTestCaseName() + ".xml").get("StudySearch");
    }

    @Test(description = "Search for study using the search box in the upper right hand corner")
    public void studySearchBox()
    {
        try
        {
            step1();
        }
        catch(Throwable e)
        {
            report.log("Failed in Test case", LogLevel.INFO);
            Assert.fail(e.getMessage());
        }
    }

    public void step1() throws Exception
    {
        // Step 1: Click in the "Search for Study" search box.
        homePage.clickStudySearchBox();

        // Verification 1: User is able to enter text in the search box.
        homePage.enterTextIntoTextBox(data.get("StudyID"), TEXTBOXNAME);
    }

}
