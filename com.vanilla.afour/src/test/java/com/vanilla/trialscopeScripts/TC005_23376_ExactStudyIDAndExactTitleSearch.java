package com.vanilla.trialscopeScripts;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.vanilla.MasterBase;
import com.vanilla.afour.trialscope.pages.HomePage;

/**
 * Search for study using the search box in the upper right hand corner
 * 
 * @author Mayank B
 *
 */
public class TC005_23376_ExactStudyIDAndExactTitleSearch extends MasterBase
{

    private static HashMap<String, String> data = null;
    private static HomePage homePage = null;
    private static final String TEXTBOXNAME = "SearchBox";

    public TC005_23376_ExactStudyIDAndExactTitleSearch() throws Exception
    {
        super();
        homePage = new HomePage();
        data = testDataHandler.readTestData(super.getTestCaseName() + ".xml").get("StudySearch");
    }

    @Test(priority = 0)
    public void exactStudySearchById() throws InterruptedException
    {
        final String studyAttribute = "StudySearchResultsByID";

        // Step 1: Type Study ID in study search box and click magnifying class (or press Enter).
        homePage.enterTextIntoTextBox(data.get("StudyID"), TEXTBOXNAME);
        homePage.clickStudySearchButton();

        // Verification: Study displays in the Search Results window.
        assertTrue(homePage.isStudyFoundInStudySearchResults(data.get("StudyID"), studyAttribute), "No Study exist with that Study ID");
        
    }

    @Test(priority = 1)
    public void exactStudySearchByTitle() throws InterruptedException
    {
        final String studyAttribute = "StudySearchResultsByTitle";

        // Step 2: Type Study Title in study search box and click magnifying class (or press Enter).
        homePage.clickCancelStudySearchButton();
        homePage.enterTextIntoTextBox(data.get("StudyTitle"), TEXTBOXNAME);
        homePage.clickStudySearchButton();

        // Verification: Study displays in the Search Results window.
        assertTrue(homePage.isStudyFoundInStudySearchResults(data.get("StudyTitle"), studyAttribute), "No Study exist with that Study Title");
    }

}
